package utez.edu.mx.A9.controller.libro.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestCrearLibroDTO {

    @NotBlank(message = "El titulo del libro es obligatorio")
    private String titulo;

    @Min(value = 1, message = "Debe registrar al menos 1 ejemplar disponible")
    private int ejemplaresDisponibles;
}