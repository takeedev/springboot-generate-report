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
