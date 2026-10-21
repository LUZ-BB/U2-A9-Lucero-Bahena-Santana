package utez.edu.mx.A9.service.lector;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.A9.controller.lector.dto.RequestCrearLectorDTO;
import utez.edu.mx.A9.exception.BadRequestExeption;
import utez.edu.mx.A9.model.lector.Lector;
import utez.edu.mx.A9.repository.lector.LectorRepository;

import java.util.List;

@Service
public class LectorService {

    private final LectorRepository lectorRepository;

    public LectorService(LectorRepository lectorRepository) {
        this.lectorRepository = lectorRepository;
    }

    public List<Lector> obtenerTodos() {
        return lectorRepository.findAll();
    }

    @Transactional
    public Lector crearLector(RequestCrearLectorDTO payload) {
        Lector lector = new Lector();
        lector.setNombre(payload.getNombre());
        lector.setActivo(true);
        lector.setMembresiaPremium(payload.isMembresiaPremium());
        return lectorRepository.save(lector);
    }

    @Transactional
    public Lector rehabilitarLector(Long id) {
        Lector lector = lectorRepository.findById(id)
                .orElseThrow(() -> new BadRequestExeption("El lector no existe"));

        lector.setActivo(true);
        return lectorRepository.save(lector);
    }
}