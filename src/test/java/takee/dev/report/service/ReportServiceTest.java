package takee.dev.report.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import takee.dev.report.entity.Reports;
import takee.dev.report.enums.ReportTypeEnum;
import takee.dev.report.repository.DailyReportRepository;
import takee.dev.report.repository.ReportsRepository;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @InjectMocks
    ReportService reportService;

    @MockitoBean
    ReportsRepository reportsRepository;

    @MockitoBean
    DailyReportRepository dailyReportRepository;

    @Test
    @SneakyThrows
    @DisplayName("save data is success")
    void saveDataSuccess() {
        Reports reports = new Reports();
        reports.setReportNo("0001");
        reports.setReportName("0001 report");
        reports.setTypeReport(ReportTypeEnum.EXCEL);
        reports.setActive(true);

        Mockito.when(reportsRepository.save(reports));
        reportService.saveReport(reports);


    }

}