package takee.dev.report.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import takee.dev.report.entity.Reports;
import takee.dev.report.enums.ReportTypeEnum;
import takee.dev.report.repository.ReportsRepository;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @InjectMocks
    ReportService reportService;

    @MockitoBean
    ReportsRepository reportsRepository;

    @Test
    @SneakyThrows
    @DisplayName("save data is success")
    void saveDataSuccess() {
        Reports reports = new Reports();
        reports.setReportNo(001);
        reports.setReportName("01 report");
        reports.setTypeReport(ReportTypeEnum.EXCEL);
        var report = reportsRepository.save(reports);
        assertEquals("001", report.getReportNo());
    }

}