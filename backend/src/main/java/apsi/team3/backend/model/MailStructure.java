package apsi.team3.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.ByteArrayResource;

@Setter
@Getter
@AllArgsConstructor
public class MailStructure {
    private String subject;
    private String message;
    private String attachment;
}