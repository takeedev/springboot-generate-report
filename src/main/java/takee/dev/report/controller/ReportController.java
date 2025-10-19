package takee.dev.report.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import takee.dev.report.entity.DailyReport;
import takee.dev.report.entity.Reports;
import takee.dev.report.service.ReportService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/report")
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/add-report")
    public ResponseEntity<String> addReport(@RequestBody Reports req) {
        var result = reportService.saveReport(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/get-report")
    public ResponseEntity<List<Reports>> getReport() {
        var result = reportService.getReport();
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(result);
    }

    @PostMapping("/add-report-daily")
    public ResponseEntity<String> addReportDaily(@RequestBody DailyReport req) {
        var result = reportService.saveDailyReport(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/get-daily-report")
    public ResponseEntity<List<DailyReport>> getDailyReport() {
        var result = reportService.getDailyReport();
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(result);
    }

}
