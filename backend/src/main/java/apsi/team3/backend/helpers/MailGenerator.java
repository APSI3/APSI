package apsi.team3.backend.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

public class MailGenerator {

    private static final String templatePath = "templates/mailTemplate.html";

    public static String generateTicket(Map<String, String> ticketData) {
        try {
            String templateContent = readResourceFile(templatePath);
            for (Map.Entry<String, String> entry : ticketData.entrySet()) {
                templateContent = templateContent.replace("$" + entry.getKey(), entry.getValue());
            }
            return templateContent;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getDateString(LocalDate fromDate, LocalDate toDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy"); // todo: should include time of day
        String strFromDate = fromDate.format(formatter);
        String strToDate = toDate.format(formatter);
        return String.format("%s - %s", strFromDate, strToDate);
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
