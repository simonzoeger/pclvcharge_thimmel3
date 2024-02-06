package com.example.pclvcharge;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ZeitraumPruefung {
    public static void main(String[] args) {
        ExcelReader reader = new ExcelReader("Input_StudVers.xlsx");
        List<Object[]> prognosedaten = reader.getPrognosedaten();

        String[][] zeitraeume = {
                {"01.01.24 14:40", "02.01.24 06:00"},
                {"02.01.24 12:45", "03.01.24 06:10"},
                {"03.01.24 14:00", "04.01.24 11:05"},
                {"04.01.24 19:10", "06.01.24 17:20"}
        };

        List<List<Object[]>> werteNachZeitraeumen = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

        int zeitraumNummer = 1;
        for (String[] zeitraum : zeitraeume) {
            LocalDateTime start = LocalDateTime.parse(zeitraum[0], formatter);
            LocalDateTime ende = LocalDateTime.parse(zeitraum[1], formatter);
            List<Object[]> werteInDiesemZeitraum = new ArrayList<>();

            for (Object[] prognose : prognosedaten) {
                LocalDateTime prognoseDatum = (LocalDateTime) prognose[1];
                double wert = Double.parseDouble(prognose[0].toString());

                if (!prognoseDatum.isBefore(start) && !prognoseDatum.isAfter(ende) && wert > 0) {
                    werteInDiesemZeitraum.add(new Object[]{prognoseDatum, wert});
                }
            }

            if (!werteInDiesemZeitraum.isEmpty()) {
                werteNachZeitraeumen.add(werteInDiesemZeitraum);
                System.out.println("Ergebnisse für Zeitraum " + zeitraumNummer + ":");
                for (Object[] wertDatum : werteInDiesemZeitraum) {
                    LocalDateTime datum = (LocalDateTime) wertDatum[0];
                    double wert = (double) wertDatum[1];
                    System.out.println("Datum: " + datum.format(formatter) + ", Wert: " + wert);
                }
                zeitraumNummer++;
            }
        }
    }
}
