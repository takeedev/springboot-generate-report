package takee.dev.report.common;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import takee.dev.report.dto.TransactionDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ExcelCommonTest {

    @InjectMocks
    private ExcelCommon excelCommon;

    @Test
    @SneakyThrows
    @DisplayName("should generate excel file success and verify content")
    void shouldGenerateExcelFileSuccessAndVerifyContent() {
        var temp = Files.createTempDirectory("TEMP_DIR");
        var mockData = TransactionDto.builder()
                .id("ID")
                .name("NAME")
                .amount(1)
                .date(LocalDate.of(2025, 10, 20))
                .dateTime(LocalDateTime.of(2025, 10, 25, 22, 12, 23))
                .build();

        Map<String, List<TransactionDto>> dataList = new HashMap<>();
        dataList.put("SHEET1", List.of(mockData));

        var path = Path.of(temp.toString()).toString();
        var result = excelCommon.generateMultiSheetExcel(
                path,
                "FILENAME",
                dataList
        );

        assertEquals("FILENAME", result.getFilename());

    }

}