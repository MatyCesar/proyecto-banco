package com.ejemplo.backendclientes.controller;

import com.ejemplo.backendclientes.model.Cuenta;
import com.ejemplo.backendclientes.model.Movimiento;
import com.ejemplo.backendclientes.repository.CuentaRepository;
import com.ejemplo.backendclientes.repository.MovimientoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/movimientos")
@CrossOrigin(origins = "http://localhost:5173")
public class MovimientoController {

    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;

    public MovimientoController(MovimientoRepository movimientoRepository, CuentaRepository cuentaRepository) {
        this.movimientoRepository = movimientoRepository;
        this.cuentaRepository = cuentaRepository;
    }

    @GetMapping("/cuenta/{cuentaId}")
    public List<Movimiento> obtenerPorCuenta(@PathVariable Long cuentaId) {
        return movimientoRepository.findByCuentaIdOrderByFechaDesc(cuentaId);
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Movimiento movimiento) {
        Optional<Cuenta> cuentaOpt = cuentaRepository.findById(movimiento.getCuenta().getId());
        if (cuentaOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Cuenta no encontrada");
        }

        Cuenta cuenta = cuentaOpt.get();
        Double saldoActual = cuenta.getSaldoInicial();
        List<Movimiento> movimientos = movimientoRepository.findByCuentaIdOrderByFechaDesc(cuenta.getId());

        if (!movimientos.isEmpty()) {
            saldoActual = movimientos.get(0).getSaldo();
        }

        if (movimiento.getTipo().equalsIgnoreCase("RETIRO")) {
            if (saldoActual < movimiento.getValor()) {
                return ResponseEntity.badRequest().body("Saldo insuficiente para retiro");
            }
            saldoActual -= movimiento.getValor();
        } else {
            saldoActual += movimiento.getValor();
        }

        movimiento.setSaldo(saldoActual);
        movimiento.setFecha(LocalDate.now());
        movimiento.setCuenta(cuenta);

        Movimiento guardado = movimientoRepository.save(movimiento);
        return ResponseEntity.ok(guardado);
    }
}
