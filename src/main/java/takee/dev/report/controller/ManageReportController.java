package takee.dev.report.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import takee.dev.report.common.dto.GeneratedFile;
import takee.dev.report.entity.DailyReport;
import takee.dev.report.entity.Reports;
import takee.dev.report.service.DownloadReportServiceImp;
import takee.dev.report.service.ManageReportServiceImp;
import takee.dev.report.utils.ResponseExportFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/manage/report")
public class ManageReportController {

    private final ManageReportServiceImp manageReportService;
    private final DownloadReportServiceImp downloadReportServiceImp;
    private final ResponseExportFile responseExportFile;

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

    @GetMapping("/gen-normal-report")
    public ResponseEntity<byte[]> generateNormalReport(String reportNo) {
        GeneratedFile result = downloadReportServiceImp.genByReport(reportNo);
        return responseExportFile.toResponseEntity(result);
    }

    @GetMapping("/gen-large-report")
    public ResponseEntity<byte[]> generateLargeReport(String reportNo) {
        GeneratedFile result = downloadReportServiceImp.genByReportLargeFiles(reportNo);
        return responseExportFile.toResponseEntity(result);
    }
}
