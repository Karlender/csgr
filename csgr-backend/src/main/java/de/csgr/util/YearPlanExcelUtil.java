package de.csgr.util;

import de.csgr.entity.Appointment;
import de.csgr.entity.AppointmentGroup;
import de.csgr.entity.YearPlan;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YearPlanExcelUtil {

    public static byte[] generateYearPlanExcel(YearPlan yearPlan) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            // Sheet 1: Chat Roulette Year Plan
            Sheet yearPlanSheet = workbook.createSheet("Chat Roulette Jahresplan");

            int rowNum = 0;

            Row titleRow = yearPlanSheet.createRow(rowNum++);
            titleRow.createCell(0).setCellValue("Chat Roulette Jahresplan für " + yearPlan.getYear());

            Row headerRow = yearPlanSheet.createRow(rowNum++);
            headerRow.createCell(0).setCellValue("Datum");

            // Dynamically create group headers based on the number of groups
            int maxGroups = yearPlan.getAppointments().stream()
                    .mapToInt(appointment -> appointment.getGroups().size())
                    .max()
                    .orElse(0);

            for (int i = 0; i < maxGroups; i++) {
                headerRow.createCell(i + 1).setCellValue("Gruppe " + (i + 1));
            }

            // Create wrapping styles for alternating row colors
            CellStyle defaultWrapStyle = workbook.createCellStyle();
            defaultWrapStyle.setWrapText(true);
            CellStyle greyWrapStyle = workbook.createCellStyle();
            greyWrapStyle.setWrapText(true);

            XSSFColor lightGreyColor = new XSSFColor(new java.awt.Color(230, 230, 230), null); // Light grey
            ((XSSFCellStyle) greyWrapStyle).setFillForegroundColor(lightGreyColor);
            greyWrapStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            for (Appointment appointment : yearPlan.getAppointments()) {
                Row dataRow = yearPlanSheet.createRow(rowNum++);

                // Determine row style (alternating colors)
                CellStyle rowStyle = ((rowNum - 1) % 2 == 0) ? defaultWrapStyle : greyWrapStyle;

                Cell dateCell = dataRow.createCell(0);
                dateCell.setCellValue(appointment.getFromDate().format(dateFormatter));
                dateCell.setCellStyle(rowStyle);

                List<AppointmentGroup> groups = appointment.getGroups();
                for (int i = 0; i < groups.size(); i++) {
                    String groupEmployees = String.join("\n", groups.get(i).getEmployees()); // Multiline text
                    Cell cell = dataRow.createCell(i + 1);
                    cell.setCellValue(groupEmployees);
                    cell.setCellStyle(rowStyle);

                    // Adjust row height dynamically based on the number of lines
                    int lineCount = groupEmployees.split("\n").length;
                    dataRow.setHeightInPoints(Math.max(dataRow.getHeightInPoints(), lineCount * yearPlanSheet.getDefaultRowHeightInPoints()));
                }
            }

            for (int i = 0; i <= maxGroups; i++) {
                yearPlanSheet.autoSizeColumn(i);
            }

            // Sheet 2: Encounter Matrix
            Map<String, Map<String, Integer>> encounterMatrix = EncounterMatrixUtil.createEncounterMatrix(yearPlan);

            Sheet matrixSheet = workbook.createSheet("Begegnungsstatistik");

            rowNum = 0;

            Row matrixTitleRow = matrixSheet.createRow(rowNum++);
            matrixTitleRow.createCell(0).setCellValue("Begegnungsstatistik");

            // Get the list of employees (keys of the matrix)
            List<String> employees = new ArrayList<>(encounterMatrix.keySet());

            // Create the header row for the matrix
            Row matrixHeaderRow = matrixSheet.createRow(rowNum++);
            matrixHeaderRow.createCell(0).setCellValue(""); // Top-left corner cell
            for (int i = 0; i < employees.size(); i++) {
                matrixHeaderRow.createCell(i + 1).setCellValue(employees.get(i));
            }

            // Create styles for alternating row colors
            CellStyle defaultStyle = workbook.createCellStyle();
            CellStyle greyStyle = workbook.createCellStyle();

            ((XSSFCellStyle) greyStyle).setFillForegroundColor(lightGreyColor);
            greyStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            for (int i = 0; i < employees.size(); i++) {
                // Apply alternating background color
                CellStyle rowStyle = (i % 2 == 0) ? defaultStyle : greyStyle;

                String employee = employees.get(i);
                Row matrixRow = matrixSheet.createRow(rowNum++);
                Cell employeeNameCell = matrixRow.createCell(0);
                employeeNameCell.setCellValue(employee);
                employeeNameCell.setCellStyle(rowStyle);

                // Fill encounter data
                for (int j = 0; j < employees.size(); j++) {
                    String otherEmployee = employees.get(j);
                    Integer count = encounterMatrix.getOrDefault(employee, new HashMap<>()).getOrDefault(otherEmployee, 0);

                    Cell cell = matrixRow.createCell(j + 1);
                    cell.setCellValue(count);
                    cell.setCellStyle(rowStyle);
                }
            }

            for (int i = 0; i <= employees.size(); i++) {
                matrixSheet.autoSizeColumn(i);
            }

            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                workbook.write(out);
                return out.toByteArray();
            }
        }
    }

}

