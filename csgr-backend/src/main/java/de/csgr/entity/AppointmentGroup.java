package de.csgr.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "appointment_group", schema = "csgr")
public class AppointmentGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;

    @ManyToOne
    @JoinColumn(name = "appointment_uuid", nullable = false)
    private Appointment appointment;

    @Column(name = "employees", nullable = false)
    @Convert(converter = StringListConverter.class)
    private List<String> employees;
}
