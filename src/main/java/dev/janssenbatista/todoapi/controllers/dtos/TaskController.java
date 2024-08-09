package dev.janssenbatista.todoapi.controllers.dtos;

import dev.janssenbatista.todoapi.entities.TaskEntity;
import dev.janssenbatista.todoapi.services.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/tarefas")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping()
    public ResponseEntity<TaskEntity> createTask(@RequestBody CreateTaskDto taskDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.save(taskDto));
    }
}
