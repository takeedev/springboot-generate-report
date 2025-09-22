package takee.dev.report.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import takee.dev.report.entity.Reports;
import takee.dev.report.repository.DailyReportRepository;
import takee.dev.report.repository.ReportsRepository;

@Service
@RequiredArgsConstructor
public class ReportService implements ReportServiceImp {

    private final ReportsRepository reportsRepository;
    private final DailyReportRepository dailyReportRepository;

    @Override
    public String saveReport(Reports object) {
        reportsRepository.save(object);
        return "";
    }
}
