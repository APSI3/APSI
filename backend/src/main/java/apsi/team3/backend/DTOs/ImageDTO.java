package apsi.team3.backend.DTOs;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ImageDTO implements Serializable {
    Long id;
    Long eventId;
    String image;
    boolean sectionMap;
}
