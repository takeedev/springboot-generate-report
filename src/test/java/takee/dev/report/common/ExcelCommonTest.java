package takee.dev.report.common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import takee.dev.report.dto.TransactionDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExcelCommonTest {

    private final ExcelCommon excelCommon = new ExcelCommon();

    @Test
    @SneakyThrows
    @DisplayName("should generate excel file success and verify content")
    void shouldGenerateExcelFileSuccessAndVerifyContent() {
        var mockData = TransactionDto.builder()
                .id("ID")
                .name("NAME")
                .amount(1)
                .date(LocalDate.of(2025, 10, 20))
                .dateTime(LocalDateTime.of(2025, 10, 25, 22, 12, 23))
                .build();

        Map<String, List<TransactionDto>> dataList = new HashMap<>();
        dataList.put("SHEET1", List.of(mockData));

        var result = excelCommon.generateMultiSheetExcel(
                "FILENAME",
                dataList
        );

        assertEquals("FILENAME", result.getFilename());

    }

    @Test
    @SneakyThrows
    @DisplayName("should generate excel file with resource template")
    void shouldGenerateExcelFileWithResourceTemplate() {
        var mockData = TransactionDto.builder()
                .id("ID")
                .name("NAME")
                .amount(1)
                .date(LocalDate.of(2025, 10, 20))
                .dateTime(LocalDateTime.of(2025, 10, 25, 22, 12, 23))
                .build();

        Map<String, List<TransactionDto>> dataList = new HashMap<>();
        dataList.put("SHEET1", List.of(mockData));

        var result = excelCommon.generateMultiSheetExcel(
                "FILENAME",
                dataList,
                "file:blue"
        );

        assertEquals("FILENAME", result.getFilename());
    }

    @Test
    @DisplayName("should generate excel file for exception")
    void shouldGenerateExcelFileForException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> excelCommon.generateMultiSheetExcel(
                        "FileName",
                        new HashMap<>()
                )
        );
        assertEquals("The data is empty", ex.getMessage());
    }

    @Test
    @DisplayName("should generate excel large file")
    void shouldGenerateExcelLargeFile() {
        var mockData = TransactionDto.builder()
                .id("ID")
                .name("NAME")
                .amount(1)
                .date(LocalDate.of(2025, 10, 20))
                .dateTime(LocalDateTime.of(2025, 10, 25, 22, 12, 23))
                .build();

        Map<String, List<TransactionDto>> dataList = new HashMap<>();
        dataList.put("SHEET1", List.of(mockData));

        var result = excelCommon.generateMultiSheetExcelForLargeFiles(
                "FILENAME",
                dataList,
                100
        );

        assertEquals("FILENAME", result.getFilename());
        CleanUpTempFileCommon.cleanUpTempFile(result);
    }

    @Test
    @DisplayName("should generate excel large file for exception")
    void shouldGenerateExcelLargeFileForException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> excelCommon.generateMultiSheetExcelForLargeFiles(
                        "FILENAME",
                        new HashMap<>(),
                        100
                )
        );

        assertEquals("The data is empty", ex.getMessage());

    }
}
