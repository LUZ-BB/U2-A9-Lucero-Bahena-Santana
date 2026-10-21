package utez.edu.mx.A9.controller.prestamo;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.A9.controller.prestamo.dto.RequestSolicitarPrestamoDTO;
import utez.edu.mx.A9.model.prestamo.Prestamo;
import utez.edu.mx.A9.service.prestamo.PrestamoService;

import java.util.List;

@RestController
@RequestMapping("/prestamo")
@CrossOrigin({"*"})
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @PostMapping("/solicitar")
    public ResponseEntity<Prestamo> solicitarPrestamo(@RequestBody @Valid RequestSolicitarPrestamoDTO payload) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(prestamoService.solicitarPrestamo(payload));
    }

    @PatchMapping("/{id}/devolver")
    public ResponseEntity<Prestamo> devolverLibro(@PathVariable Long id) {
        return ResponseEntity.ok(prestamoService.devolverLibro(id));
    }

    @PatchMapping("/{id}/renovar")
    public ResponseEntity<Prestamo> renovarPrestamo(@PathVariable Long id) {
        return ResponseEntity.ok(prestamoService.renovarPrestamo(id));
    }

    @GetMapping("/lector/{lectorId}/activos")
    public ResponseEntity<List<Prestamo>> listarPrestamosActivosLector(@PathVariable Long lectorId) {
        return ResponseEntity.ok(prestamoService.listarPrestamosActivosLector(lectorId));
    }
}