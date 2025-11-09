package takee.dev.report.common;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import takee.dev.report.dto.TransactionDto;
import takee.dev.report.enums.ExtensionEnum;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class TextCommonTest {

    @InjectMocks
    private TextCommon textCommon;

    @Test
    @SneakyThrows
    @DisplayName("generate text file is success")
    void generateTextFileSuccess() {

        var mockData = TransactionDto.builder()
                .id("ID")
                .name("NAME")
                .amount(1)
                .date(LocalDate.of(2025, 10, 20))
                .dateTime(LocalDateTime.of(2025, 10, 25, 22, 12, 23))
                .build();

        var tempPath = Files.createTempDirectory("TEMP_PATH");

        var result = textCommon.generateFileTextOrCsv(
                tempPath.toString(),
                "FILENAME",
                ExtensionEnum.TXT,
                "|",
                new ArrayList<>(List.of(mockData)),
                true,
                StandardCharsets.UTF_8,
                false
        );

        assertEquals("FILENAME", result.getFilename());
        assertEquals(ExtensionEnum.TXT, result.getExtension());
    }

    @Test
    @SneakyThrows
    @DisplayName("should throw IllegalArumentException when object list null")
    void generateFileNullObjectThrowException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> textCommon.generateFileTextOrCsv(
                        "PATH",
                        "FILENAME",
                        ExtensionEnum.TXT,
                        "DELIMITER",
                        null,
                        true,
                        StandardCharsets.UTF_8,
                        true
                )
        );
        assertEquals("Object is null", ex.getMessage());
    }

    @Test
    @SneakyThrows
    @DisplayName("should throw exception when directory is invalid")
    void generateFileInvalidDirectoryThrowException() {
        class DummyDto {
            private String getName() {
                return "X";
            }
        }
        var mockDate = new DummyDto();
        assertThrows(Exception.class, () ->
                textCommon.generateFileTextOrCsv(
                        "PATH",
                        "FILENAME",
                        ExtensionEnum.TXT,
                        "DELIMITER",
                        List.of(mockDate),
                        true,
                        StandardCharsets.UTF_8,
                        true
                )
        );
    }

    @Test
    @SneakyThrows
    @DisplayName("generate csv file is success")
    void generateCsvFileSuccess() {

        var mockData = TransactionDto.builder()
                .id("ID")
                .name("NAME")
                .amount(1)
                .date(LocalDate.of(2025, 10, 20))
                .dateTime(LocalDateTime.of(2025, 10, 25, 22, 12, 23))
                .build();

        var tempPath = Files.createTempDirectory("TEMP_PATH");

        var result = textCommon.generateFileTextOrCsv(
                tempPath.toString(),
                "FILENAME",
                ExtensionEnum.CSV,
                ",",
                new ArrayList<>(List.of(mockData)),
                true,
                StandardCharsets.UTF_8,
                false
        );

        assertEquals("FILENAME", result.getFilename());
        assertEquals(ExtensionEnum.CSV, result.getExtension());
    }

    @Test
    @SneakyThrows
    @DisplayName("generate csv file is success no header")
    void generateCsvFileSuccessNoHeader() {

        var mockData = TransactionDto.builder()
                .id("ID")
                .name("NAME")
                .amount(1)
                .date(LocalDate.of(2025, 10, 20))
                .dateTime(LocalDateTime.of(2025, 10, 25, 22, 12, 23))
                .build();

        var tempPath = Files.createTempDirectory("TEMP_PATH");

        var result = textCommon.generateFileTextOrCsv(
                tempPath.toString(),
                "FILENAME",
                ExtensionEnum.CSV,
                ",",
                new ArrayList<>(List.of(mockData)),
                false,
                StandardCharsets.UTF_8,
                false
        );

        assertEquals("FILENAME", result.getFilename());
        assertEquals(ExtensionEnum.CSV, result.getExtension());
    }

    @Test
    @SneakyThrows
    @DisplayName("generate csv file is success no header with encoding")
    void generateCsvFileSuccessNoHeaderWithEncoding() {

        var mockData = TransactionDto.builder()
                .id("ID")
                .name("NAME")
                .amount(1)
                .date(LocalDate.of(2025, 10, 20))
                .dateTime(LocalDateTime.of(2025, 10, 25, 22, 12, 23))
                .build();

        var tempPath = Files.createTempDirectory("TEMP_PATH");

        var result = textCommon.generateFileTextOrCsv(
                tempPath.toString(),
                "FILENAME",
                ExtensionEnum.CSV,
                ",",
                new ArrayList<>(List.of(mockData)),
                false,
                StandardCharsets.UTF_8,
                true
        );

        assertEquals("FILENAME", result.getFilename());
        assertEquals(ExtensionEnum.CSV, result.getExtension());
    }
}