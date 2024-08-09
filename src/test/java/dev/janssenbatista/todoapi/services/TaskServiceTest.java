package dev.janssenbatista.todoapi.services;

import dev.janssenbatista.todoapi.controllers.dtos.CreateTaskDto;
import dev.janssenbatista.todoapi.controllers.dtos.UpdateTaskDto;
import dev.janssenbatista.todoapi.entities.TaskEntity;
import dev.janssenbatista.todoapi.exceptions.BadRequestException;
import dev.janssenbatista.todoapi.exceptions.NotFoundException;
import dev.janssenbatista.todoapi.repositories.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    public void shouldSaveTask() {
        var newTask = new TaskEntity(1L, "task title", "task description", false);
        var taskDto = new CreateTaskDto("task title", "task description");
        when(taskRepository.findByTitle(taskDto.title())).thenReturn(null);
        when(taskRepository.save(Mockito.any(TaskEntity.class))).thenReturn(newTask);
        var createdTask = taskService.save(taskDto);
        assertThat(createdTask.getId()).isNotNull();
        assertThat(createdTask.getTitle()).isEqualTo(newTask.getTitle());
        assertThat(createdTask.getDescription()).isEqualTo(newTask.getDescription());
        assertThat(createdTask.isCompleted()).isFalse();
    }

    @Test
    public void shouldThrowBadRequestExceptionWhenSaveTask() {
        var foundTask = new TaskEntity(1L, "task title", "task description", false);
        var taskDto = new CreateTaskDto("task title", "task description");
        when(taskRepository.findByTitle(taskDto.title())).thenReturn(foundTask);
        assertThatThrownBy(() -> {
            taskService.save(taskDto);
        }).isInstanceOf(BadRequestException.class).hasMessageContaining("Essa tarefa já existe");
        verify(taskRepository, never()).save(any(TaskEntity.class));
    }

    @Test
    public void shouldListAllTasks() {
        var task = new TaskEntity(1L, "task title", "task description", false);
        var taskPage = new PageImpl<>(List.of(task));
        var pageable = PageRequest.of(0, 1);
        when(taskRepository.findAll(pageable)).thenReturn(taskPage);
        var page = taskService.listAll(pageable);
        assertThat(page).isNotNull();
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getSize()).isEqualTo(1);
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent().get(0)).isEqualTo(task);
    }

    @Test()
    public void shouldUpdateATask() {
        var foundTask = new TaskEntity(1L, "task title", "task description", false);
        var taskId = 1L;
        var updateTaskDto = new UpdateTaskDto("new task title", "new task description");
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(foundTask));
        when(taskRepository.save(Mockito.any(TaskEntity.class))).thenReturn(foundTask);
        var updatedTask = taskService.update(taskId, updateTaskDto);
        assertThat(updatedTask).isNotNull();
        assertThat(updatedTask.getId()).isEqualTo(foundTask.getId());
        assertThat(updatedTask.getTitle()).isEqualTo(foundTask.getTitle());
        assertThat(updatedTask.getDescription()).isEqualTo(foundTask.getDescription());
    }

    @Test()
    public void shouldThrowBadRequestExceptionWhenUpdateATask() {
        var taskId = 1L;
        var updateTaskDto = new UpdateTaskDto("new task title", "new task description");
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> {
            taskService.update(taskId, updateTaskDto);
        }).isInstanceOf(NotFoundException.class).hasMessageContaining("Tarefa não encontrada");
        verify(taskRepository, never()).save(Mockito.any(TaskEntity.class));
    }

    @Test()
    public void shouldDeleteATask() {
        var foundTask = new TaskEntity(1L, "task title", "task description", false);
        var taskId = 1L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(foundTask));
        taskService.delete(taskId);
        verify(taskRepository, times(1)).delete(Mockito.any(TaskEntity.class));
    }

    @Test()
    public void shouldThrowNotFoundExceptionWhenDeleteATask() {
        var taskId = 1L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> {
            taskService.delete(taskId);
        }).isInstanceOf(NotFoundException.class).hasMessageContaining("Tarefa não encontrada");
        verify(taskRepository, never()).delete(Mockito.any(TaskEntity.class));
    }
}