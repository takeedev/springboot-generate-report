package takee.dev.report.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import takee.dev.report.common.dto.GeneratedFile;
import takee.dev.report.service.DownloadReportServiceImp;
import takee.dev.report.utils.ResponseExportFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/download-file")
public class DownloadReportController {

    private final DownloadReportServiceImp downloadReportServiceImp;
    private final ResponseExportFile responseExportFile;

    @GetMapping("/gen-normal-report-excel")
    public ResponseEntity<byte[]> generateNormalReport(String reportNo) {
        GeneratedFile result = downloadReportServiceImp.genByReport(reportNo);
        return responseExportFile.toResponseEntity(result);
    }

    @GetMapping("/gen-large-report-excel")
    public ResponseEntity<byte[]> generateLargeReport(String reportNo) {
        GeneratedFile result = downloadReportServiceImp.genByReportLargeFiles(reportNo);
        return responseExportFile.toResponseEntity(result);
    }

    @GetMapping("/gen-report-csv")
    public ResponseEntity<byte[]> generateReportCsv(String reportNo) {
        GeneratedFile result = downloadReportServiceImp.genByReportCsv(reportNo);
        return responseExportFile.toResponseEntity(result);
    }

    @GetMapping("/gen-report-text")
    public ResponseEntity<byte[]> generateReportText(String reportNo) {
        GeneratedFile result = downloadReportServiceImp.genByReportText(reportNo);
        return responseExportFile.toResponseEntity(result);
    }

    @GetMapping("/gen-report-csv-disk")
    public GeneratedFile generateReportCsvToDisk(String reportNo) {
        return downloadReportServiceImp.genByReportCsvToDisk(reportNo);
    }

    @GetMapping("/gen-report-text-disk")
    public GeneratedFile generateReportTextToDisk(String reportNo) {
        return downloadReportServiceImp.genByReportTextToDisk(reportNo);
    }
}
