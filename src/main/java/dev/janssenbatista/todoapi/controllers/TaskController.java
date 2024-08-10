package dev.janssenbatista.todoapi.controllers;

import dev.janssenbatista.todoapi.controllers.dtos.CreateTaskDto;
import dev.janssenbatista.todoapi.entities.TaskEntity;
import dev.janssenbatista.todoapi.services.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @GetMapping()
    public ResponseEntity<Page<TaskEntity>> getAllTasks(@RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
                                            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok(taskService.listAll(pageable));
    }
}
