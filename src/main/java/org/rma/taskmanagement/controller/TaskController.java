package org.rma.taskmanagement.controller;

import org.rma.taskmanagement.model.Employee;
import org.rma.taskmanagement.model.Task;
import org.rma.taskmanagement.service.EmployeeService;
import org.rma.taskmanagement.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.Principal;
import java.util.ArrayList;
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

    // Display the CSV upload form
    @GetMapping("/upload")
    public String showUploadForm() {
        return "task-upload";  // This will render the "task-upload.html" Thymeleaf view
    }

    // Handle the CSV file upload and parsing
    @PostMapping("/upload")
    public String uploadTasksCSV(@RequestParam("file") MultipartFile file, Model model) {
        if (file.isEmpty()) {
            model.addAttribute("error", "Please select a CSV file to upload.");
            return "task-upload";
        }

        try {
            List<Task> tasks = parseCSV(file);  // Parse the CSV file
            taskService.saveTasks(tasks);  // Save the parsed tasks
            model.addAttribute("success", "Tasks successfully uploaded.");
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while processing the CSV file.");
            e.printStackTrace();
        }

        return "redirect:/tasks";  // Redirect to the task list view
    }

    // Helper method to parse the CSV file
    private List<Task> parseCSV(MultipartFile file) throws Exception {
        List<Task> tasks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean isFirstRow = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstRow) {
                    isFirstRow = false;  // Skip the header row
                    continue;
                }

                String[] data = line.split(",");
                if (data.length != 4) {
                    throw new IllegalArgumentException("CSV file format is incorrect. Expected columns: title, description, employeeId, status.");
                }

                // Create a new task based on CSV data
                Task task = new Task();
                task.setTitle(data[0]);
                task.setDescription(data[1]);

                try {
                    Long employeeId = Long.parseLong(data[2]);
                    Employee employee = employeeService.getEmployeeById(employeeId);
                    task.setEmployee(employee);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid employee ID format: " + data[2]);
                }

                task.setStatus(data[3]);
                tasks.add(task);
            }
        }
        return tasks;
    }
}


