package takee.dev.report.service;

import takee.dev.report.common.dto.GeneratedFile;

public interface DownloadReportService {

   GeneratedFile genByReport(String reportNo);

}
