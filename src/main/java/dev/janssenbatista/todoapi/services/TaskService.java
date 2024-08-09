package dev.janssenbatista.todoapi.services;

import com.fasterxml.jackson.databind.util.BeanUtil;
import dev.janssenbatista.todoapi.controllers.dtos.CreateTaskDto;
import dev.janssenbatista.todoapi.controllers.dtos.UpdateTaskDto;
import dev.janssenbatista.todoapi.entities.TaskEntity;
import dev.janssenbatista.todoapi.exceptions.BadRequestException;
import dev.janssenbatista.todoapi.exceptions.NotFoundException;
import dev.janssenbatista.todoapi.repositories.TaskRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<TaskEntity> listAll(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    public TaskEntity update(Long id, UpdateTaskDto taskDto) {
        var foundTask = taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Tarefa não encontrada"));
        BeanUtils.copyProperties(taskDto, foundTask);
        return taskRepository.save(foundTask);
    }


}
