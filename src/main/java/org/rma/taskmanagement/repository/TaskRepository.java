package org.rma.taskmanagement.repository;

import org.rma.taskmanagement.model.Employee;
import org.rma.taskmanagement.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByEmployee(Employee employee);
}