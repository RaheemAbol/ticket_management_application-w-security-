package org.rma.taskmanagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.rma.taskmanagement.model.Employee;
import org.rma.taskmanagement.model.Task;
import org.rma.taskmanagement.repository.TaskRepository;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



@ActiveProfiles("test")
@SpringBootTest
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task;
    private Employee employee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        employee = new Employee();
        employee.setId(1L);
        employee.setName("John Doe");

        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Task Description");
        task.setEmployee(employee);
        task.setStatus("Pending");
    }

    @Test
    void getAllTasks_ReturnsListOfTasks() {
        // Arrange
        List<Task> tasks = Arrays.asList(task);
        when(taskRepository.findAll()).thenReturn(tasks);

        // Act
        List<Task> result = taskService.getAllTasks();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(task.getTitle(), result.get(0).getTitle());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void getTaskById_ValidId_ReturnsTask() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // Act
        Task result = taskService.getTaskById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(task.getId(), result.getId());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void getTaskById_InvalidId_ReturnsNull() {
        // Arrange
        when(taskRepository.findById(2L)).thenReturn(Optional.empty());

        // Act
        Task result = taskService.getTaskById(2L);

        // Assert
        assertNull(result);
        verify(taskRepository, times(1)).findById(2L);
    }

    @Test
    void saveTask_SavesTask() {
        // Arrange
        when(taskRepository.save(task)).thenReturn(task);

        // Act
        Task result = taskService.saveTask(task);

        // Assert
        assertNotNull(result);
        assertEquals(task.getId(), result.getId());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void deleteTask_ValidId_DeletesTask() {

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void getTasksByEmployee_ReturnsListOfTasks() {
        // Arrange
        List<Task> tasks = Arrays.asList(task);
        when(taskRepository.findByEmployee(employee)).thenReturn(tasks);

        // Act
        List<Task> result = taskService.getTasksByEmployee(employee);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(task.getTitle(), result.get(0).getTitle());
        verify(taskRepository, times(1)).findByEmployee(employee);
    }

    @Test
    void updateTaskStatus_UpdatesStatus() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // Act
        taskService.updateTaskStatus(1L, "In Progress");

        // Assert
        assertEquals("In Progress", task.getStatus());
        verify(taskRepository, times(1)).save(task);
    }
}