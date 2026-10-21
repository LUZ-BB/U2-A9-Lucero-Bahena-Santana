package utez.edu.mx.A9.service.prestamo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.A9.controller.prestamo.dto.RequestSolicitarPrestamoDTO;
import utez.edu.mx.A9.exception.BadRequestExeption;
import utez.edu.mx.A9.model.lector.Lector;
import utez.edu.mx.A9.model.libro.Libro;
import utez.edu.mx.A9.model.prestamo.EstadoPrestamo;
import utez.edu.mx.A9.model.prestamo.Prestamo;
import utez.edu.mx.A9.repository.lector.LectorRepository;
import utez.edu.mx.A9.repository.libro.LibroRepository;
import utez.edu.mx.A9.repository.prestamo.PrestamoRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final LectorRepository lectorRepository;
    private final LibroRepository libroRepository;

    public PrestamoService(PrestamoRepository prestamoRepository, LectorRepository lectorRepository, LibroRepository libroRepository) {
        this.prestamoRepository = prestamoRepository;
        this.lectorRepository = lectorRepository;
        this.libroRepository = libroRepository;
    }

    @Transactional
    public Prestamo solicitarPrestamo(RequestSolicitarPrestamoDTO payload) {
        Lector lector = lectorRepository.findById(payload.getLectorId())
                .orElseThrow(() -> new BadRequestExeption("El lector no existe"));

        if (!lector.isActivo()) {
            throw new BadRequestExeption("El lector no esta activo, no se puede solicitar prsstamos");
        }

        Libro libro = libroRepository.findById(payload.getLibroId())
                .orElseThrow(() -> new BadRequestExeption("El libro no existe"));

        if (libro.getEjemplaresDisponibles() < 1) {
            throw new BadRequestExeption("El libro no cuenta con ejemplares disponibles");
        }

        long prestamosActivos = prestamoRepository.countByLectorIdAndEstadoIn(
                lector.getId(),
                List.of(EstadoPrestamo.PRESTADO, EstadoPrestamo.VENCIDO)
        );

        int limiteMaximo = lector.isMembresiaPremium() ? 5 : 3;
        if (prestamosActivos >= limiteMaximo) {
            throw new BadRequestExeption("El lector ha alcanzado su limite de prestamos activos ");
        }

        libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() - 1);
        libroRepository.save(libro);

        Prestamo prestamo = new Prestamo();
        prestamo.setLector(lector);
        prestamo.setLibro(libro);
        prestamo.setFechaPrestamo(LocalDate.now());
        prestamo.setFechaDevolucionMaxima(LocalDate.now().plusDays(14));
        prestamo.setEstado(EstadoPrestamo.PRESTADO);

        return prestamoRepository.save(prestamo);
    }

    @Transactional
    public Prestamo devolverLibro(Long prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new BadRequestExeption("El prestamo no existe"));

        if (prestamo.getEstado() == EstadoPrestamo.DEVUELTO) {
            throw new BadRequestExeption("El libro ya ha sido devuelto previamente");
        }

        prestamo.setEstado(EstadoPrestamo.DEVUELTO);

        Libro libro = prestamo.getLibro();
        libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() + 1);
        libroRepository.save(libro);

        if (LocalDate.now().isAfter(prestamo.getFechaDevolucionMaxima())) {
            Lector lector = prestamo.getLector();
            lector.setActivo(false);
            lectorRepository.save(lector);
        }

        return prestamoRepository.save(prestamo);
    }

    @Transactional
    public Prestamo renovarPrestamo(Long prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new BadRequestExeption("El prestamo no existe"));

        if (prestamo.getEstado() == EstadoPrestamo.VENCIDO || prestamo.getEstado() == EstadoPrestamo.DEVUELTO) {
            throw new BadRequestExeption("No se puede renovar un prestamo en estado VENCIDO o DEVUELTO");
        }

        if (LocalDate.now().isAfter(prestamo.getFechaDevolucionMaxima())) {
            throw new BadRequestExeption("No se puede renovar un prestamo que ha sobrepasado la fecha de devolucion");
        }

        prestamo.setFechaDevolucionMaxima(prestamo.getFechaDevolucionMaxima().plusDays(7));
        return prestamoRepository.save(prestamo);
    }

    public List<Prestamo> listarPrestamosActivosLector(Long lectorId) {
        if (!lectorRepository.existsById(lectorId)) {
            throw new BadRequestExeption("El lector no existe");
        }
        return prestamoRepository.findByLectorIdAndEstadoIn(
                lectorId,
                List.of(EstadoPrestamo.PRESTADO, EstadoPrestamo.VENCIDO)
        );
    }
}