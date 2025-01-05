package de.csgr.util;

import de.csgr.entity.Appointment;
import de.csgr.entity.AppointmentGroup;
import de.csgr.entity.YearPlan;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EncounterMatrixUtil {

    public static Map<String, Map<String, Integer>> createEncounterMatrix(YearPlan yearPlan) {
        Map<String, Map<String, Integer>> encounterMatrix = initializeEncounterMatrix(yearPlan.getEmployees());

        for (Appointment appointment : yearPlan.getAppointments()) {
            for (AppointmentGroup group : appointment.getGroups()) {
                updateEncounterMatrix(Collections.singletonList(group.getEmployees()), encounterMatrix);
            }
        }

        return encounterMatrix;
    }

    static Map<String, Map<String, Integer>> initializeEncounterMatrix(List<String> employees) {
        Map<String, Map<String, Integer>> matrix = new HashMap<>();
        for (String e1 : employees) {
            matrix.put(e1, new HashMap<>());
            for (String e2 : employees) {
                if (!e2.equals(e1)) {
                    matrix.get(e1).put(e2, 0);
                }
            }
        }
        return matrix;
    }

    static void updateEncounterMatrix(List<List<String>> groups, Map<String, Map<String, Integer>> encounterMatrix) {
        for (List<String> group : groups) {
            for (int i = 0; i < group.size(); i++) {
                for (int j = i + 1; j < group.size(); j++) {
                    String e1 = group.get(i);
                    String e2 = group.get(j);

                    encounterMatrix.get(e1).put(e2, encounterMatrix.get(e1).getOrDefault(e2, 0) + 1);
                    encounterMatrix.get(e2).put(e1, encounterMatrix.get(e2).getOrDefault(e1, 0) + 1);
                }
            }
        }
    }
}
