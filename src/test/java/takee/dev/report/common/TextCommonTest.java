package takee.dev.report.common;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import takee.dev.report.dto.TransactionDto;

import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
                .date(LocalDate.of(2025,10,20))
                .dateTime(LocalDateTime.of(2025,10,25,22,12,23))
                .build();

        var tempPath = Files.createTempDirectory("TEMP_PATH");

        var result = textCommon.generateFileTextOrCsv(
                tempPath.toString(),
                "FILENAME",
                "TEXT",
                "|",
                new ArrayList<>(List.of(mockData))
        );

        List<String> line = Files.readAllLines(result);
        assertTrue(Files.exists(result));
        assertEquals("รหัส|ชื่อ|จำนวนเงิน|วันที่|วันที่และเวลา",line.getFirst());
        assertEquals("ID|NAME|1.00|2025-10-20|2025-10-25 22:12:23",line.getLast());
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
                        "EXTENSION",
                        "DELIMITER",
                        null
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
        var list = new ArrayList<>(List.of(mockDate));
        assertThrows(Exception.class, () ->
                textCommon.generateFileTextOrCsv(
                        "PATH",
                        "FILENAME",
                        "EXTENSION",
                        "DELIMITER",
                        list
                )
        );
    }

}