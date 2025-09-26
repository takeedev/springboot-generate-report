package takee.dev.report.service;

import takee.dev.report.entity.DailyReport;
import takee.dev.report.entity.Reports;

public interface ReportServiceImp {

    String saveReport(Reports object);

    String saveDailyReport(DailyReport object);
}
