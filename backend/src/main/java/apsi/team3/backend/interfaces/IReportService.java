package apsi.team3.backend.interfaces;

import apsi.team3.backend.DTOs.EventReportDTO;
import apsi.team3.backend.exceptions.ApsiValidationException;

public interface IReportService {
    EventReportDTO getEventReport(Long id) throws ApsiValidationException;
}
