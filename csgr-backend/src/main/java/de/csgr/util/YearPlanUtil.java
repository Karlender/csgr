package de.csgr.util;

import de.csgr.entity.Rotation;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.function.UnaryOperator;

public class YearPlanUtil {

    public static Map<LocalDate, List<List<String>>> createBalancedYearPlan(
            List<String> employees, int numberOfGroups, Rotation rotation, int year) {
        Map<LocalDate, List<List<String>>> schedule = new LinkedHashMap<>();
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);

        // StepFunction vorbereiten
        UnaryOperator<LocalDate> stepFunction = switch (rotation) {
            case DAILY -> date -> date.plusDays(1);
            case WEEKLY -> date -> date.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
            case MONTHLY -> date -> date.plusMonths(1);
        };

        Map<String, Map<String, Integer>> encounterMatrix = EncounterMatrixUtil.initializeEncounterMatrix(employees);

        for (LocalDate date = startDate; !date.isAfter(endDate); date = stepFunction.apply(date)) {
            List<List<String>> groups = createBalancedGroups(employees, numberOfGroups, encounterMatrix);
            schedule.put(date, groups);

            EncounterMatrixUtil.updateEncounterMatrix(groups, encounterMatrix);
        }

        return schedule;
    }

    private static List<List<String>> createBalancedGroups(
            List<String> employees, int numberOfGroups, Map<String, Map<String, Integer>> encounterMatrix) {
        List<String> availableEmployees = new ArrayList<>(employees);
        Collections.shuffle(availableEmployees); // Shuffle the list randomly so the first employee is not always in the first group
        List<List<String>> groups = new ArrayList<>();

        int groupSize = employees.size() / numberOfGroups;
        int remainingEmployees = employees.size() % numberOfGroups;  // Calculate how many extra employees there are

        for (int i = 0; i < numberOfGroups; i++) {
            List<String> group = new ArrayList<>();

            // Fill each group with the base group size, and distribute the remaining employees to the first few groups
            int currentGroupSize = groupSize + (i < remainingEmployees ? 1 : 0);

            while (group.size() < currentGroupSize && !availableEmployees.isEmpty()) {
                String nextEmployee = selectEmployeeWithLeastPairwiseEncounters(group, availableEmployees, encounterMatrix);
                group.add(nextEmployee);
                availableEmployees.remove(nextEmployee);
            }

            groups.add(group);
        }

        return groups;
    }

    private static String selectEmployeeWithLeastPairwiseEncounters(
            List<String> currentGroup, List<String> candidates, Map<String, Map<String, Integer>> encounterMatrix) {
        String bestCandidate = null;
        int minEncounters = Integer.MAX_VALUE;

        for (String candidate : candidates) {
            int totalEncounters = 0;

            for (String groupMember : currentGroup) {
                totalEncounters += encounterMatrix.get(candidate).getOrDefault(groupMember, 0);
            }

            if (totalEncounters < minEncounters) {
                minEncounters = totalEncounters;
                bestCandidate = candidate;
            }
        }

        return bestCandidate;
    }

}
