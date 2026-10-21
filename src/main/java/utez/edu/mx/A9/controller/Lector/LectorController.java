package utez.edu.mx.A9.controller.lector;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.A9.controller.lector.dto.RequestCrearLectorDTO;
import utez.edu.mx.A9.model.lector.Lector;
import utez.edu.mx.A9.service.lector.LectorService;

import java.util.List;

@RestController
@RequestMapping("/lector")
@CrossOrigin({"*"})
public class LectorController {

    private final LectorService lectorService;

    public LectorController(LectorService lectorService) {
        this.lectorService = lectorService;
    }

    @GetMapping
    public ResponseEntity<List<Lector>> obtenerTodos() {
        return ResponseEntity.ok(lectorService.obtenerTodos());
    }

    @PostMapping
    public ResponseEntity<Lector> crearLector(@RequestBody @Valid RequestCrearLectorDTO payload) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(lectorService.crearLector(payload));
    }

    @PatchMapping("/{id}/rehabilitar")
    public ResponseEntity<Lector> rehabilitarLector(@PathVariable Long id) {
        return ResponseEntity.ok(lectorService.rehabilitarLector(id));
    }
}