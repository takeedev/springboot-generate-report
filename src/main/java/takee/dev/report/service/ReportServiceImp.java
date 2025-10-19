package takee.dev.report.service;

import takee.dev.report.entity.DailyReport;
import takee.dev.report.entity.Reports;

import java.util.List;

public interface ReportServiceImp {

    String saveReport(Reports object);

    String saveDailyReport(DailyReport object);

    List<Reports> getReport();

    List<DailyReport> getDailyReport();
}
