package dev.janssenbatista.todoapi.controllers.dtos;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.janssenbatista.todoapi.controllers.TaskController;
import dev.janssenbatista.todoapi.entities.TaskEntity;
import dev.janssenbatista.todoapi.exceptions.BadRequestException;
import dev.janssenbatista.todoapi.services.TaskService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

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
        mockMvc.perform(MockMvcRequestBuilders.post("/api/tarefas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void shouldNotCreateATaskAndReturn400() throws Exception {
        var dto = new CreateTaskDto("task title", "task description");
        when(taskService.save(dto)).thenThrow(new BadRequestException("Essa tarefa já existe"));
        var response = mockMvc.perform(MockMvcRequestBuilders.post("/api/tarefas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
        assertThat(response.getResponse().getContentAsString()).isEqualTo("Essa tarefa já existe");
    }

}