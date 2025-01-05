package de.csgr.util;

import de.csgr.entity.Appointment;
import de.csgr.entity.AppointmentGroup;
import de.csgr.entity.YearPlan;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.immutable.ImmutableCalScale;
import net.fortuna.ical4j.model.property.immutable.ImmutableVersion;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class YearPlanIcsUtil {

    public static byte[] generateYearPlanICS(YearPlan yearPlan) throws Exception {
        Calendar calendar = new Calendar();
        calendar.add(new ProdId(String.format("-//Jahresplan %d//iCal4j 1.0//EN", yearPlan.getYear())));
        calendar.add(ImmutableVersion.VERSION_2_0);
        calendar.add(ImmutableCalScale.GREGORIAN);

        for (Appointment appointment : yearPlan.getAppointments()) {
            LocalDate fromDate = appointment.getFromDate();

            // Build event summary with group information
            StringBuilder summaryBuilder = new StringBuilder("Gruppen: ");
            List<AppointmentGroup> groups = appointment.getGroups();
            for (int i = 0; i < groups.size(); i++) {
                List<String> employees = groups.get(i).getEmployees();
                summaryBuilder.append("Gruppe ").append(i + 1).append(" (").append(String.join(", ", employees)).append(")");
                if (i < groups.size() - 1) {
                    summaryBuilder.append("; ");
                }
            }

            VEvent event = new VEvent(
                    fromDate,
                    summaryBuilder.toString()
            );

            // Add a unique identifier for the event
            event.add(new Uid(UUID.randomUUID().toString()));

            calendar.add(event);
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            calendar.validate();
            CalendarOutputter outputter = new CalendarOutputter();
            outputter.output(calendar, outputStream);
            return outputStream.toByteArray();
        }
    }
}

