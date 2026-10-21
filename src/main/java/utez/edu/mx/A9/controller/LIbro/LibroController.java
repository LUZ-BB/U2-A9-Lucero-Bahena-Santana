package utez.edu.mx.A9.controller.libro;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.A9.controller.libro.dto.RequestCrearLibroDTO;
import utez.edu.mx.A9.model.libro.Libro;
import utez.edu.mx.A9.service.libro.LibroService;

import java.util.List;

@RestController
@RequestMapping("/libro")
@CrossOrigin({"*"})
public class LibroController {

    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    @GetMapping
    public ResponseEntity<List<Libro>> obtenerTodos() {
        return ResponseEntity.ok(libroService.obtenerTodos());
    }

    @PostMapping
    public ResponseEntity<Libro> crearLibro(@RequestBody @Valid RequestCrearLibroDTO payload) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(libroService.crearLibro(payload));
    }
}