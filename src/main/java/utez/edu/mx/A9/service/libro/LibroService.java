package utez.edu.mx.A9.service.libro;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.A9.controller.libro.dto.RequestCrearLibroDTO;
import utez.edu.mx.A9.model.libro.Libro;
import utez.edu.mx.A9.repository.libro.LibroRepository;

import java.util.List;

@Service
public class LibroService {

    private final LibroRepository libroRepository;

    public LibroService(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    public List<Libro> obtenerTodos() {
        return libroRepository.findAll();
    }

    @Transactional
    public Libro crearLibro(RequestCrearLibroDTO payload) {
        Libro libro = new Libro();
        libro.setTitulo(payload.getTitulo());
        libro.setEjemplaresDisponibles(payload.getEjemplaresDisponibles());
        return libroRepository.save(libro);
    }
}