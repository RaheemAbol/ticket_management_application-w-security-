package org.rma.taskmanagement.controller;

import org.rma.taskmanagement.model.Employee;
import org.rma.taskmanagement.model.Task;
import org.rma.taskmanagement.service.EmployeeService;
import org.rma.taskmanagement.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private TaskService taskService;

    // List all employees (Admin-only)
    @GetMapping
    public String viewEmployees(Model model) {
        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);
        return "employee-list";  // View for listing employees
    }

    // Show the form to create a new employee (Admin-only)
    @GetMapping("/new")
    public String showCreateEmployeeForm(Model model) {
        Employee employee = new Employee();
        model.addAttribute("employee", employee);
        return "employee-form";  // View for creating or editing an employee
    }

    // Save a new or updated employee (Admin-only)
    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute("employee") Employee employee) {
        employeeService.saveEmployee(employee);
        return "redirect:/employees";  // Redirect back to employee list
    }

    // Show the form to edit an existing employee (Admin-only)
    @GetMapping("/edit/{id}")
    public String showEditEmployeeForm(@PathVariable Long id, Model model) {
        Employee employee = employeeService.getEmployeeById(id);
        model.addAttribute("employee", employee);
        return "employee-form";  // View for editing an employee
    }

    // Delete an employee (Admin-only)
    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return "redirect:/employees";  // Redirect back to employee list
    }

    // Employee-specific task hub
    @GetMapping("/tasks")
    public String employeeTaskHub(Principal principal, Model model) {
        // Get the logged-in employee's email from the Principal object
        Employee employee = employeeService.findByEmail(principal.getName());

        // Fetch the tasks for the logged-in employee
        List<Task> tasks = taskService.getTasksByEmployee(employee);

        // Add tasks and employee data to the model
        model.addAttribute("tasks", tasks);
        model.addAttribute("employee", employee);

        return "task-hub";  // Return the Thymeleaf view for displaying employee tasks
    }

    // Employee updating the task status
    @PostMapping("/tasks/update-status")
    public String updateTaskStatus(@RequestParam Long taskId, @RequestParam String status) {
        taskService.updateTaskStatus(taskId, status);
        return "redirect:/employees/tasks";  // Redirect back to employee's task hub
    }
}
