package apsi.team3.backend.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

public class MailGenerator {

    private static final String templatePath = "templates/mailTemplate.html";
    private static final String templateNoLocationPath = "templates/mailTemplateNoLocation.html";

    public static String generateTicket(Map<String, String> ticketData) {
        try {
            String templateContent = ticketData.containsKey("description") ?
                    readResourceFile(templatePath) : readResourceFile(templateNoLocationPath);
            for (Map.Entry<String, String> entry : ticketData.entrySet()) {
                templateContent = templateContent.replace("$" + entry.getKey(), entry.getValue());
            }
            return templateContent;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getDateString(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return date.format(formatter);
    }

    public static String getTimeString(LocalTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return time.format(formatter);
    }

    public static String getNumber(String buildingNr, String apartmentNr) {
        if (apartmentNr == null) {
            return buildingNr;
        }
        return String.format("%s / %s", buildingNr, apartmentNr);
    }

    public static String readResourceFile(String fileName) {
        // Get the class loader
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        // Read the file using InputStream
        try (InputStream inputStream = classLoader.getResourceAsStream(fileName)) {
            assert inputStream != null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
