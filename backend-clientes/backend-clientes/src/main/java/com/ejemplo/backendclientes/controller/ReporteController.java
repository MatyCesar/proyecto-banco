package com.ejemplo.backendclientes.controller;

import com.ejemplo.backendclientes.model.Movimiento;
import com.ejemplo.backendclientes.model.Cuenta;
import com.ejemplo.backendclientes.model.Cliente;
import com.ejemplo.backendclientes.repository.ClienteRepository;
import com.ejemplo.backendclientes.repository.CuentaRepository;
import com.ejemplo.backendclientes.repository.MovimientoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "http://localhost:5173")
public class ReporteController {

    private final ClienteRepository clienteRepository;
    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;

    public ReporteController(ClienteRepository clienteRepository, CuentaRepository cuentaRepository, MovimientoRepository movimientoRepository) {
        this.clienteRepository = clienteRepository;
        this.cuentaRepository = cuentaRepository;
        this.movimientoRepository = movimientoRepository;
    }

    @GetMapping("/estado-cuenta")
    public ResponseEntity<?> generarReporte(
            @RequestParam Long clienteId,
            @RequestParam String desde,
            @RequestParam String hasta,
            @RequestParam(defaultValue = "json") String formato
    ) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(clienteId);
        if (clienteOpt.isEmpty()) return ResponseEntity.badRequest().body("Cliente no encontrado");

        LocalDate fechaInicio = LocalDate.parse(desde);
        LocalDate fechaFin = LocalDate.parse(hasta);

        List<Cuenta> cuentas = cuentaRepository.findByClienteId(clienteId);
        List<Map<String, Object>> resumen = new ArrayList<>();

        for (Cuenta cuenta : cuentas) {
            List<Movimiento> movimientos = movimientoRepository
                    .findByCuentaIdOrderByFechaDesc(cuenta.getId()).stream()
                    .filter(m -> !m.getFecha().isBefore(fechaInicio) && !m.getFecha().isAfter(fechaFin))
                    .collect(Collectors.toList());

            double totalDebitos = movimientos.stream()
                    .filter(m -> m.getTipo().equalsIgnoreCase("RETIRO"))
                    .mapToDouble(Movimiento::getValor)
                    .sum();

            double totalCreditos = movimientos.stream()
                    .filter(m -> m.getTipo().equalsIgnoreCase("INGRESO"))
                    .mapToDouble(Movimiento::getValor)
                    .sum();

            double saldo = movimientos.isEmpty() ? cuenta.getSaldoInicial() : movimientos.get(0).getSaldo();

            List<Map<String, Object>> movimientosDTO = movimientos.stream().map(m -> {
                Map<String, Object> map = new HashMap<>();
                map.put("fecha", m.getFecha());
                map.put("tipo", m.getTipo());
                map.put("valor", m.getValor());
                map.put("saldo", m.getSaldo());
                return map;
            }).collect(Collectors.toList());

            Map<String, Object> cuentaResumen = new HashMap<>();
            cuentaResumen.put("numeroCuenta", cuenta.getNumero());
            cuentaResumen.put("tipo", cuenta.getTipo());
            cuentaResumen.put("saldo", saldo);
            cuentaResumen.put("saldoInicial", cuenta.getSaldoInicial());
            cuentaResumen.put("totalDebitos", totalDebitos);
            cuentaResumen.put("totalCreditos", totalCreditos);
            cuentaResumen.put("movimientos", movimientosDTO);

            resumen.add(cuentaResumen);
        }

        if (formato.equalsIgnoreCase("pdf")) {
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                Document document = new Document();
                PdfWriter.getInstance(document, out);
                document.open();

                document.add(new Paragraph("Reporte Estado de Cuenta"));
                document.add(new Paragraph("Cliente: " + clienteOpt.get().getNombre()));
                document.add(new Paragraph("Rango: " + desde + " a " + hasta));
                document.add(Chunk.NEWLINE);

                PdfPTable table = new PdfPTable(9);
                table.addCell("Fecha");
                table.addCell("Cliente");
                table.addCell("Número");
                table.addCell("Cuenta");
                table.addCell("Tipo");
                table.addCell("Saldo Inicial");
                table.addCell("Estado");
                table.addCell("Movimiento");
                table.addCell("Saldo Disponible");

                for (Map<String, Object> c : resumen) {
                    table.addCell(desde + " - " + hasta);
                    table.addCell(clienteOpt.get().getNombre());
                    table.addCell(String.valueOf(c.get("numeroCuenta")));
                    table.addCell("Corriente");
                    table.addCell(String.valueOf(c.get("tipo")));
                    table.addCell(String.valueOf(c.get("saldoInicial")));
                    table.addCell("Activa");
                    table.addCell("Débitos: " + c.get("totalDebitos") + " / Créditos: " + c.get("totalCreditos"));
                    table.addCell(String.valueOf(c.get("saldo")));
                }

                document.add(table);
                document.close();

                String base64 = Base64.getEncoder().encodeToString(out.toByteArray());
                Map<String, Object> response = new HashMap<>();
                response.put("cliente", clienteOpt.get().getNombre());
                response.put("rango", desde + " - " + hasta);
                response.put("pdf", base64);
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Error generando PDF");
            }
        }

        return ResponseEntity.ok(resumen);
    }
}
