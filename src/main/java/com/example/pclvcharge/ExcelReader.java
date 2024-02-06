package com.example.pclvcharge;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {
    private List<Object[]> dataWithDateTime = new ArrayList<>();

    public ExcelReader(String filePath) {
        loadData(filePath);
    }

    private void loadData(String filePath) {
        try {
            FileInputStream file = new FileInputStream(new File(filePath));
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(2); // Zugriff auf das dritte Blatt
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

            for (int colIndex = 2; colIndex <= 7; colIndex++) {
                int dayOfMonth = colIndex - 1; // Tag des Monats basierend auf Spaltenindex
                for (int rowIndex = 1; rowIndex <= 24; rowIndex++) {
                    Row row = sheet.getRow(rowIndex);
                    if (row != null) {
                        Cell cell = row.getCell(colIndex);
                        if (cell != null) {
                            String cellValue = getCellValue(cell);
                            LocalDateTime dateTime = LocalDateTime.of(2024, 1, dayOfMonth, rowIndex - 1, 0);
                            dataWithDateTime.add(new Object[]{cellValue, dateTime});
                        }
                    }
                }
            }
            workbook.close();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Object[]> getPrognosedaten() {
        return dataWithDateTime;
    }

    private static String getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf(Math.round(cell.getNumericCellValue()));
                }
            case BOOLEAN:
                return Boolean.toString(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}
