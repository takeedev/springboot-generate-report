package takee.dev.report.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import takee.dev.report.entity.DailyReport;
import takee.dev.report.entity.Reports;
import takee.dev.report.repository.DailyReportRepository;
import takee.dev.report.repository.ReportsRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManageManageReportService implements ManageReportServiceImp {

    private final ReportsRepository reportsRepository;
    private final DailyReportRepository dailyReportRepository;

    @Override
    public String saveReport(Reports object) {
        reportsRepository.save(object);
        return "";
    }

    @Override
    public String saveDailyReport(DailyReport object) {
       dailyReportRepository.save(object);
        return "";
    }

    @Override
    public List<Reports> getReport() {
        return reportsRepository.findAll();
    }

    @Override
    public List<DailyReport> getDailyReport() {
        return dailyReportRepository.findAll();
    }

}
