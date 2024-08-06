package dev.janssenbatista.todoapi.services;

import dev.janssenbatista.todoapi.controllers.dtos.CreateTaskDto;
import dev.janssenbatista.todoapi.entities.TaskEntity;
import dev.janssenbatista.todoapi.exceptions.BadRequestException;
import dev.janssenbatista.todoapi.repositories.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskEntity save(CreateTaskDto taskDto) {
        var foundTask = taskRepository.findByTitle(taskDto.title());
        if (foundTask != null) {
            throw new BadRequestException("Essa tarefa já existe");
        }
        var task = new TaskEntity(null, taskDto.title(), taskDto.description(), false);
        return taskRepository.save(task);
    }


}
