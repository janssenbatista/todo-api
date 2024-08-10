package dev.janssenbatista.todoapi.controllers.dtos;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.janssenbatista.todoapi.controllers.TaskController;
import dev.janssenbatista.todoapi.entities.TaskEntity;
import dev.janssenbatista.todoapi.exceptions.BadRequestException;
import dev.janssenbatista.todoapi.exceptions.NotFoundException;
import dev.janssenbatista.todoapi.services.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    TaskService taskService;

    @Test
    public void shouldCreateATaskAndReturn201() throws Exception {
        var dto = new CreateTaskDto("task title", "task description");
        var createdTask = new TaskEntity(1L, dto.title(), dto.description(), false);
        when(taskService.save(dto)).thenReturn(createdTask);
        mockMvc.perform(post("/api/tarefas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldNotCreateATaskAndReturn400() throws Exception {
        var dto = new CreateTaskDto("task title", "task description");
        when(taskService.save(dto)).thenThrow(new BadRequestException("Essa tarefa já existe"));
        var response = mockMvc.perform(post("/api/tarefas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertThat(response.getResponse().getContentAsString()).isEqualTo("Essa tarefa já existe");
    }

    @Test
    public void shouldListAllTasks() throws Exception {
        mockMvc.perform(get("/api/tarefas"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldUpdateATaskAndReturn200() throws Exception {
        var dto = new UpdateTaskDto("new task title", "new task description");
        var task = new TaskEntity(1L, "task title", "task description", false);
        when(taskService.update(task.getId(), dto)).thenReturn(task);
        var response = mockMvc.perform(put("/api/tarefas/" + task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn();
        var taskEntity = mapper.readValue(response.getResponse().getContentAsString(), TaskEntity.class);
        assertThat(taskEntity).isNotNull();
    }

    @Test
    public void shouldNotUpdateATaskAndReturn404() throws Exception {
        var taskId = 1L;
        var dto = new UpdateTaskDto("new task title", "new task description");
        when(taskService.update(taskId, dto)).thenThrow(new NotFoundException("Tarefa não encontrada"));
        var response = mockMvc.perform(put("/api/tarefas/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andReturn();
        assertThat(response.getResponse().getContentAsString()).isEqualTo("Tarefa não encontrada");
    }

    @Test
    public void shouldDeleteATaskAndReturn204() throws Exception {
        var taskId = 1L;
        mockMvc.perform(delete("/api/tarefas/" + taskId))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldNotDeleteATaskAndReturn404() throws Exception {
        var taskId = 1L;
        doThrow(new NotFoundException("Tarefa não encontrada"))
                .when(taskService).delete(taskId);
        var response = mockMvc.perform(delete("/api/tarefas/" + taskId))
                .andExpect(status().isNotFound())
                .andReturn();
        assertThat(response.getResponse().getContentAsString()).isEqualTo("Tarefa não encontrada");
    }

}