package takee.dev.report.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import takee.dev.report.common.dto.GeneratedFile;
import takee.dev.report.service.DownloadReportService;
import takee.dev.report.utils.ResponseExportFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/download-file")
public class DownloadReportController {

    private final DownloadReportService downloadReportService;
    private final ResponseExportFile responseExportFile;

    @GetMapping("/gen-normal-report-excel")
    public ResponseEntity<StreamingResponseBody> generateNormalReport(@RequestParam String reportNo) {
        GeneratedFile result = downloadReportService.genByReport(reportNo);
        return responseExportFile.toResponseEntity(result);
    }

    @GetMapping("/gen-large-report-excel")
    public ResponseEntity<StreamingResponseBody> generateLargeReport(@RequestParam String reportNo) {
        GeneratedFile result = downloadReportService.genByReportLargeFiles(reportNo);
        return responseExportFile.toResponseEntity(result);
    }

    @GetMapping("/gen-report-csv")
    public ResponseEntity<StreamingResponseBody> generateReportCsv(@RequestParam String reportNo) {
        GeneratedFile result = downloadReportService.genByReportCsv(reportNo);
        return responseExportFile.toResponseEntity(result);
    }

    @GetMapping("/gen-report-text")
    public ResponseEntity<StreamingResponseBody> generateReportText(@RequestParam String reportNo) {
        GeneratedFile result = downloadReportService.genByReportText(reportNo);
        return responseExportFile.toResponseEntity(result);
    }

    @GetMapping("/gen-report-csv-disk")
    public GeneratedFile generateReportCsvToDisk(@RequestParam String reportNo) {
        return downloadReportService.genByReportCsvToDisk(reportNo);
    }

    @GetMapping("/gen-report-text-disk")
    public GeneratedFile generateReportTextToDisk(@RequestParam String reportNo) {
        return downloadReportService.genByReportTextToDisk(reportNo);
    }
}
