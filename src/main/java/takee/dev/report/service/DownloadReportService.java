package takee.dev.report.service;

import takee.dev.report.common.dto.GeneratedFile;

public interface DownloadReportService {

   GeneratedFile genByReport(String reportNo);
   GeneratedFile genByReportLargeFiles(String reportNo);

   GeneratedFile genByReportCsv(String reportNo);
   GeneratedFile genByReportText(String reportNo);

   GeneratedFile genByReportCsvToDisk(String reportNo);
   GeneratedFile genByReportTextToDisk(String reportNo);
}
