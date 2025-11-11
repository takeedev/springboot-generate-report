package takee.dev.report.controller;

import java.util.List;
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
import takee.dev.report.service.ManageManageReportService;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/manage/report")
public class ManageReportController {

    private final ManageManageReportService manageReportService;

    @PostMapping("/add-report")
    public ResponseEntity<String> addReport(@RequestBody Reports req) {
        var result = manageReportService.saveReport(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/get-report")
    public ResponseEntity<List<Reports>> getReport() {
        var result = manageReportService.getReport();
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("/add-report-daily")
    public ResponseEntity<String> addReportDaily(@RequestBody DailyReport req) {
        var result = manageReportService.saveDailyReport(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/get-daily-report")
    public ResponseEntity<List<DailyReport>> getDailyReport() {
        var result = manageReportService.getDailyReport();
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/gen-report")
    public ResponseEntity<String> generateReport() {
        return ResponseEntity.status(HttpStatus.CREATED).body("generate success");
    }

}
