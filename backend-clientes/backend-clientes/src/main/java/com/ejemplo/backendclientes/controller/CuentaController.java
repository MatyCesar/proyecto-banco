package com.ejemplo.backendclientes.controller;

import com.ejemplo.backendclientes.model.Cliente;
import com.ejemplo.backendclientes.model.Cuenta;
import com.ejemplo.backendclientes.repository.ClienteRepository;
import com.ejemplo.backendclientes.repository.CuentaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cuentas")
@CrossOrigin(origins = "http://localhost:5173")
public class CuentaController {

    private final CuentaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;

    public CuentaController(CuentaRepository cuentaRepository, ClienteRepository clienteRepository) {
        this.cuentaRepository = cuentaRepository;
        this.clienteRepository = clienteRepository;
    }

    @GetMapping
    public List<Cuenta> obtenerTodas() {
        return cuentaRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Cuenta cuenta) {
        Optional<Cliente> cliente = clienteRepository.findById(cuenta.getCliente().getId());
        if (cliente.isPresent()) {
            cuenta.setCliente(cliente.get());
            return ResponseEntity.ok(cuentaRepository.save(cuenta));
        } else {
            return ResponseEntity.badRequest().body("Cliente no encontrado");
        }
    }

    @PutMapping("/{id}")
public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Cuenta cuenta) {
    return cuentaRepository.findById(id)
        .map(cuentaExistente -> {
            cuentaExistente.setNumero(cuenta.getNumero());
            cuentaExistente.setTipo(cuenta.getTipo());
            cuentaExistente.setSaldoInicial(cuenta.getSaldoInicial());
            cuentaExistente.setEstado(cuenta.isEstado());

            Optional<Cliente> cliente = clienteRepository.findById(cuenta.getCliente().getId());
            if (cliente.isPresent()) {
                cuentaExistente.setCliente(cliente.get());
                return ResponseEntity.ok(cuentaRepository.save(cuentaExistente));
            } else {
                return ResponseEntity.badRequest().body("Cliente no encontrado");
            }
        })
        .orElse(ResponseEntity.notFound().build());
}

@DeleteMapping("/{id}")
public ResponseEntity<?> eliminar(@PathVariable Long id) {
    if (cuentaRepository.existsById(id)) {
        cuentaRepository.deleteById(id);
        return ResponseEntity.ok().build();
    } else {
        return ResponseEntity.notFound().build();
    }
}


}
