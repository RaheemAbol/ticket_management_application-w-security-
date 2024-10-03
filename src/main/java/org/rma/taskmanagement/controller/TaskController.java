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
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private EmployeeService employeeService;

    // Admin view for listing all tasks
    @GetMapping
    public String listAllTasks(Model model) {
        List<Task> tasks = taskService.getAllTasks();
        model.addAttribute("tasks", tasks);
        return "task-list";  // View for listing tasks
    }

    // Admin creating or editing a task
    @GetMapping("/new")
    public String showCreateTaskForm(Model model) {
        model.addAttribute("task", new Task());  // Pass an empty Task object to the form
        model.addAttribute("employees", employeeService.getAllEmployees());  // Pass employees for assignment
        return "task-form";  // Return the form view for task creation
    }

    // Admin creating or editing a task
    @GetMapping("/edit/{id}")
    public String editTask(@PathVariable Long id, Model model) {
        Task task = taskService.getTaskById(id);
        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("task", task);
        model.addAttribute("employees", employees);  // Provide employees for task assignment
        return "task-form";  // View for creating or editing a task
    }

    // Admin saving the task
    @PostMapping("/save")
    public String saveTask(@ModelAttribute("task") Task task) {
        taskService.saveTask(task);
        return "redirect:/tasks";  // Redirect back to task list
    }

    // Admin deleting the task
    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/tasks";  // Redirect back to task list
    }


}