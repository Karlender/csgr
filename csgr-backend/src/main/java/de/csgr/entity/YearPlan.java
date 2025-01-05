package de.csgr.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "year_plan", schema = "csgr")
public class YearPlan {

    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    private String uuid;

    private int year;

    @Enumerated(EnumType.STRING)
    private Rotation rotation;

    @Column(name = "employees", nullable = false)
    @Convert(converter = StringListConverter.class)
    private List<String> employees;

    @OneToMany(mappedBy = "yearPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments;
}
