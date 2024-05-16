package apsi.team3.backend.helpers;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class MailGenerator {

    private static final String templatePath = "backend/src/main/resources/templates/mailTemplate.html";

    public static String generateTicket(Map<String, String> ticketData) {
        try {
            String templateContent = new String(Files.readAllBytes(Paths.get(templatePath)));
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
}
