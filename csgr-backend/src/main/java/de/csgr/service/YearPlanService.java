package de.csgr.service;

import de.csgr.dto.YearPlanCreateDto;
import de.csgr.entity.Appointment;
import de.csgr.entity.AppointmentGroup;
import de.csgr.entity.YearPlan;
import de.csgr.repository.YearPlanRepository;
import de.csgr.util.YearPlanUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class YearPlanService {

    private final YearPlanRepository yearPlanRepository;

    public List<YearPlan> listYearPlans() {
        return yearPlanRepository.findAll();
    }

    public Optional<YearPlan> getYearPlan(String uuid) {
        return yearPlanRepository.findById(uuid);
    }

    public String createGroupChatYearPlan(YearPlanCreateDto yearPlanDto) {
        Map<LocalDate, List<List<String>>> balancedYearPlan = YearPlanUtil.createBalancedYearPlan(yearPlanDto.getEmployees(), yearPlanDto.getGroupCount(), yearPlanDto.getRotation(), yearPlanDto.getYear());
        YearPlan yearPlan = new YearPlan();
        yearPlan.setYear(yearPlanDto.getYear());
        yearPlan.setRotation(yearPlanDto.getRotation());
        yearPlan.setEmployees(yearPlanDto.getEmployees());

        List<Appointment> appointments = new ArrayList<>();
        balancedYearPlan.keySet().forEach(localDate -> {
            Appointment appointment = new Appointment();
            appointment.setYearPlan(yearPlan);
            appointment.setFromDate(localDate);

            List<AppointmentGroup> groups = new ArrayList<>();
            balancedYearPlan.get(localDate).forEach(groupEmployees -> {
                AppointmentGroup appointmentGroup = new AppointmentGroup();
                appointmentGroup.setAppointment(appointment);
                appointmentGroup.setEmployees(groupEmployees);
                groups.add(appointmentGroup);
            });
            appointment.setGroups(groups);

            appointments.add(appointment);
        });
        yearPlan.setAppointments(appointments);

        YearPlan savedYearPlan = yearPlanRepository.save(yearPlan);
        return savedYearPlan.getUuid();
    }
}
