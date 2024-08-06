package dev.janssenbatista.todoapi.repositories;

import dev.janssenbatista.todoapi.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    TaskEntity findByTitle(String title);
}
