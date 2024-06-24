package apsi.team3.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apsi.team3.backend.DTOs.EventReportDTO;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.interfaces.IReportService;

@RestController
@RequestMapping("/reports")
@CrossOrigin(origins = { "http://localhost:3000" }, allowCredentials = "true")
public class ReportController {
    private final IReportService reportService;

    @Autowired
    public ReportController(IReportService reportService){
        this.reportService = reportService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventReportDTO> getEventReport(@PathVariable("id") Long eventId) throws ApsiValidationException {
        var dto = reportService.getEventReport(eventId);
        return ResponseEntity.ok(dto);
    }
}
