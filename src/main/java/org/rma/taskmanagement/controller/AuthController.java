package org.rma.taskmanagement.controller;

import org.rma.taskmanagement.model.Employee;
import org.rma.taskmanagement.model.Role;
import org.rma.taskmanagement.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired
    private EmployeeService employeeService;

    // Show the login form
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    // Show the registration page
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("employee", new Employee());  // Pass an empty Employee object to the form
        return "register";
    }

    // Handle registration form submission
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("employee") Employee employee) {
        // Assign the role based on the form selection (either EMPLOYEE or ADMIN)
        employeeService.saveEmployee(employee);  // Save the employee with their selected role
        return "redirect:/login";  // Redirect to the login page
    }


}