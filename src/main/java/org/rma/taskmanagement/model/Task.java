package org.rma.taskmanagement.model;

import jakarta.persistence.*;
import lombok.Data;



@Entity
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private String status;
}