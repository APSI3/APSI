package apsi.team3.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
public class MailStructure {
    private String subject;
    private String QRCodeContent;
    private Map<String, String> mailContentParameters;
}