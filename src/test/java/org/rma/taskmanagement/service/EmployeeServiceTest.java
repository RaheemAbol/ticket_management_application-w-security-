package org.rma.taskmanagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.rma.taskmanagement.model.Employee;
import org.rma.taskmanagement.model.Role;
import org.rma.taskmanagement.repository.EmployeeRepository;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        employee = new Employee();
        employee.setId(1L);
        employee.setName("John Doe");
        employee.setEmail("john.doe@example.com");
        employee.setRole(Role.EMPLOYEE);
        employee.setPassword("password123");
    }

    @Test
    void getAllEmployees_ReturnsListOfEmployees() {
        // Arrange
        List<Employee> employees = Arrays.asList(employee);
        when(employeeRepository.findAll()).thenReturn(employees);

        // Act
        List<Employee> result = employeeService.getAllEmployees();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(employee.getName(), result.get(0).getName());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void getEmployeeById_ValidId_ReturnsEmployee() {
        // Arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        // Act
        Employee result = employeeService.getEmployeeById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(employee.getId(), result.getId());
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void getEmployeeById_InvalidId_ThrowsException() {
        // Arrange
        when(employeeRepository.findById(2L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> employeeService.getEmployeeById(2L));
        verify(employeeRepository, times(1)).findById(2L);
    }

    @Test
    void saveEmployee_EncodesPasswordAndSavesEmployee() {
        // Arrange
        when(passwordEncoder.encode(employee.getPassword())).thenReturn("encodedPassword");
        when(employeeRepository.save(employee)).thenReturn(employee);

        // Act
        employeeService.saveEmployee(employee);

        // Assert
        assertEquals("encodedPassword", employee.getPassword());
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void deleteEmployee_ValidId_DeletesEmployee() {
        // Act
        employeeService.deleteEmployee(1L);

        // Assert
        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @Test
    void findByEmail_ReturnsEmployee() {
        // Arrange
        when(employeeRepository.findByEmail("john.doe@example.com")).thenReturn(employee);

        // Act
        Employee result = employeeService.findByEmail("john.doe@example.com");

        // Assert
        assertNotNull(result);
        assertEquals(employee.getEmail(), result.getEmail());
        verify(employeeRepository, times(1)).findByEmail("john.doe@example.com");
    }
}