package dev.janssenbatista.todoapi.controllers.dtos;

import jakarta.validation.constraints.NotBlank;

public record UpdateTaskDto(
        @NotBlank(message = "O título não pode estar em branco")
        String title,
        String description
) {
}
