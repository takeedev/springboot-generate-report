package takee.dev.report.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import takee.dev.report.entity.DailyReport;
import takee.dev.report.entity.Reports;
import takee.dev.report.repository.DailyReportRepository;
import takee.dev.report.repository.ReportsRepository;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ManageReportServiceImpTest {

    @InjectMocks
    private ManageReportServiceImp manageReportServiceImp;

    @Mock
    private ReportsRepository reportsRepository;

    @Mock
    private DailyReportRepository dailyReportRepository;

    @Test
    @DisplayName("should save report successfully")
    void shouldSaveReportSuccessfully() {

        var mockResult = Reports.builder()
                .id(UUID.randomUUID())
                .reportNo("REPORT_NO")
                .build();

        Mockito.when(reportsRepository.save(any())).thenReturn(mockResult);

        var result = manageReportServiceImp.saveReport(mockResult);

        Assertions.assertEquals("",result);

    }

    @Test
    @DisplayName("should save daily report successfully")
    void shouldSaveDailyReportSuccessfully() {

        var mockResult = DailyReport.builder()
                .id(UUID.randomUUID())
                .reportName("REPORT_NAME")
                .build();

        Mockito.when(dailyReportRepository.save(any())).thenReturn(mockResult);

        var result = manageReportServiceImp.saveDailyReport(mockResult);

        Assertions.assertEquals("",result);
    }

    @Test
    @DisplayName("should get report successfully")
    void shouldGetReportSuccessfully() {

        var mockResult = List.of(
                Reports.builder()
                        .id(UUID.randomUUID())
                        .reportNo("REPORT_NO")
                        .build()
        );

        Mockito.when(reportsRepository.findAll()).thenReturn(mockResult);

        var result = manageReportServiceImp.getReport();

        Assertions.assertEquals("REPORT_NO",result.getFirst().getReportNo());
    }

    @Test
    @DisplayName("should get daily report successfully")
    void shouldGetDailyReportSuccessfully() {

        var mockResult = List.of(
                DailyReport.builder()
                        .id(UUID.randomUUID())
                        .reportName("REPORT_NAME")
                        .build()
        );

        Mockito.when(dailyReportRepository.findAll()).thenReturn(mockResult);

        var result = manageReportServiceImp.getDailyReport();

        Assertions.assertEquals("REPORT_NAME", result.getFirst().getReportName());
    }

}