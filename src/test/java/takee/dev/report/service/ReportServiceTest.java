package takee.dev.report.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import takee.dev.report.entity.Reports;
import takee.dev.report.enums.ReportTypeEnum;
import takee.dev.report.repository.DailyReportRepository;
import takee.dev.report.repository.ReportsRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @InjectMocks
    ReportService reportService;

    @Mock
    ReportsRepository reportsRepository;

    @Mock
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

        Mockito.when(reportsRepository.save(reports)).thenReturn(reports);
        var result = reportService.saveReport(reports);

        verify(reportsRepository).save(reports);
        assertEquals("", result);
    }

    @Test
    @SneakyThrows
    @DisplayName("save daily report is success")
    void saveDailyReportSuccess() {
       Mockito.when(dailyReportRepository.save(any())).thenReturn(null);
       var result = reportService.saveDailyReport(any());
       verify(dailyReportRepository).save(any());
       assertEquals("", result);
    }

}