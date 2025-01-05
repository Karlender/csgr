package de.csgr.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "appointment", schema = "csgr")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;

    @ManyToOne
    @JoinColumn(name = "year_plan_uuid", nullable = false)
    private YearPlan yearPlan;

    private LocalDate fromDate;

    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AppointmentGroup> groups;
}
