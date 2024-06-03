package com.example.mkjli;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    public static String getCurrentDayAndMonth() {
        // Obtenez la date actuelle
        Date currentDate = new Date();

        // Définir le format de date souhaité
        SimpleDateFormat dateFormat = new SimpleDateFormat("d/MMM", Locale.getDefault());

        // Formater la date actuelle selon le format spécifié
        return dateFormat.format(currentDate);
    }

}