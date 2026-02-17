package takee.dev.report.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import takee.dev.report.common.ExcelCommon;
import takee.dev.report.common.TextCommon;
import takee.dev.report.common.dto.GeneratedFile;
import takee.dev.report.entity.Reports;
import takee.dev.report.enums.FileStorageMode;
import takee.dev.report.enums.ReportTypeEnum;
import takee.dev.report.repository.ReportsRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;

@ExtendWith(MockitoExtension.class)
class DownloadReportServiceImpTest {

    @InjectMocks
    private DownloadReportServiceImp downloadReportServiceImp;

    @Mock
    private TextCommon textCommon;

    @Mock
    private ExcelCommon excelCommon;

    @Mock
    private ReportsRepository reportsRepository;

    @Test
    @DisplayName("should generate by report")
    void shouldGenerateByReport() {

        var mockReportNo = "001";
        var mockResult = Reports.builder()
                .id(UUID.randomUUID())
                .reportNo("001")
                .reportName("REPORT_NAME")
                .typeReport(ReportTypeEnum.CSV)
                .template("TEMPALTE")
                .pathOut("PATH_OUT")
                .isActive(true)
                .createdAt(Instant.ofEpochSecond(2024-01-01))
                .updatedAt(Instant.ofEpochSecond(2024-01-01))
                .build();

        var resultGenFile = GeneratedFile.builder()
                .filename("REPORT_NAME")
                .path("PATH")
                .fileStorageMode(FileStorageMode.MEMORY)
                .contentType("CONTENT_TYPE")
                .createAt(LocalDateTime.now())
                .build();

        Mockito.when(reportsRepository.findByReportNo(any())).thenReturn(mockResult);
        Mockito.when(excelCommon.generateMultiSheetExcel(any(), any())).thenReturn(resultGenFile);

        var result = downloadReportServiceImp.genByReport(mockReportNo);

        assertEquals("REPORT_NAME", result.getFilename());

    }

    @Test
    @DisplayName("should generate by report null")
    void shouldGenerateByReportNull() {

        var mockReportNo = "001";
        Mockito.when(reportsRepository.findByReportNo(any())).thenReturn(null);
        var result = downloadReportServiceImp.genByReport(mockReportNo);

        assertNull(result);
    }

    @Test
    @DisplayName("should generate by report csv")
    void shouldGenerateByReportCsv() {

        var mockReportNo = "001";
        var mockResult = Reports.builder()
                .id(UUID.randomUUID())
                .reportNo("001")
                .reportName("REPORT_NAME")
                .typeReport(ReportTypeEnum.CSV)
                .template("TEMPALTE")
                .pathOut("PATH_OUT")
                .isActive(true)
                .createdAt(Instant.ofEpochSecond(2024-01-01))
                .updatedAt(Instant.ofEpochSecond(2024-01-01))
                .build();

        var resultGenFile = GeneratedFile.builder()
                .filename("REPORT_NAME")
                .path("PATH")
                .fileStorageMode(FileStorageMode.MEMORY)
                .contentType("CONTENT_TYPE")
                .createAt(LocalDateTime.now())
                .build();

        Mockito.when(reportsRepository.findByReportNo(any())).thenReturn(mockResult);
        Mockito.when(excelCommon.generateMultiSheetExcelForLargeFiles(any(), anyMap(), anyInt())).thenReturn(resultGenFile);

        var result = downloadReportServiceImp.genByReportLargeFiles(mockReportNo);

        assertEquals("REPORT_NAME", result.getFilename());

    }

    @Test
    @DisplayName("should generate by report for csv")
    void shouldGenerateByReportForCsv() {

        var mockReportNo = "001";
        var mockResult = Reports.builder()
                .id(UUID.randomUUID())
                .reportNo("001")
                .reportName("REPORT_NAME")
                .typeReport(ReportTypeEnum.CSV)
                .template("TEMPALTE")
                .pathOut("PATH_OUT")
                .isActive(true)
                .createdAt(Instant.ofEpochSecond(2024-01-01))
                .updatedAt(Instant.ofEpochSecond(2024-01-01))
                .build();

        var resultGenFile = GeneratedFile.builder()
                .filename("REPORT_NAME")
                .path("PATH")
                .fileStorageMode(FileStorageMode.MEMORY)
                .contentType("CONTENT_TYPE")
                .createAt(LocalDateTime.now())
                .build();

        Mockito.when(reportsRepository.findByReportNo(any())).thenReturn(mockResult);
        Mockito.when(textCommon.generateCsvInMemory(any(), any(), any(), anyList(),anyBoolean(),any(), anyBoolean())).thenReturn(resultGenFile);

        var result = downloadReportServiceImp.genByReportCsv(mockReportNo);

        assertEquals("REPORT_NAME", result.getFilename());
    }

    @Test
    @DisplayName("should generate by report for text")
    void shouldGenerateByReportForText() {

        var mockReportNo = "001";
        var mockResult = Reports.builder()
                .id(UUID.randomUUID())
                .reportNo("001")
                .reportName("REPORT_NAME")
                .typeReport(ReportTypeEnum.CSV)
                .template("TEMPALTE")
                .pathOut("PATH_OUT")
                .isActive(true)
                .createdAt(Instant.ofEpochSecond(2024-01-01))
                .updatedAt(Instant.ofEpochSecond(2024-01-01))
                .build();

        var resultGenFile = GeneratedFile.builder()
                .filename("REPORT_NAME")
                .path("PATH")
                .fileStorageMode(FileStorageMode.MEMORY)
                .contentType("CONTENT_TYPE")
                .createAt(LocalDateTime.now())
                .build();

        Mockito.when(reportsRepository.findByReportNo(any())).thenReturn(mockResult);
        Mockito.when(textCommon.generateCsvInMemory(any(), any(), any(), anyList(),anyBoolean(),any(), anyBoolean())).thenReturn(resultGenFile);

        var result = downloadReportServiceImp.genByReportText(mockReportNo);

        assertEquals("REPORT_NAME", result.getFilename());
    }

}