package takee.dev.report.utils;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import takee.dev.report.common.dto.GeneratedFile;
import takee.dev.report.enums.ExtensionEnum;
import takee.dev.report.enums.FileStorageMode;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ResponseExportFileTest {

    private final ResponseExportFile responseExportFile = new ResponseExportFile();

    @Test
    @SneakyThrows
    @DisplayName("should generate file in memory successfully")
    void shouldGenerateFileInMemorySuccessfully() {

        var mockData = GeneratedFile.builder()
                .filename("FILENAME")
                .extension(ExtensionEnum.CSV)
                .contentType("text/csv")
                .content("BYTE".getBytes())
                .path(null)
                .fileStorageMode(FileStorageMode.MEMORY)
                .createAt(LocalDateTime.of(2025, 10, 25, 22, 12, 23))
                .build();

        var result = responseExportFile.toResponseEntity(mockData);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());

    }

    @Test
    @SneakyThrows
    @DisplayName("should generate file in memory from path")
    void shouldGenerateFileInMemoryFromPath() {

        var mockPath = Files.createTempDirectory("TEMP");
        var tempFile = mockPath.resolve("FILE.CSV");

        Files.writeString(tempFile, "BYTE");

        var mockData = GeneratedFile.builder()
                .filename("FILENAME")
                .extension(ExtensionEnum.CSV)
                .contentType("text/csv")
                .content(null)
                .path(tempFile.toString())
                .fileStorageMode(FileStorageMode.DISK_TEMP)
                .createAt(LocalDateTime.of(2025, 10, 25, 22, 12, 23))
                .build();

        var result = responseExportFile.toResponseEntity(mockData);
        var outputStream = new ByteArrayOutputStream();
        result.getBody().writeTo(outputStream);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("BYTE", outputStream.toString());
        assertFalse(Files.exists(tempFile));

    }

    @Test
    @DisplayName("should generate file for exception")
    void shouldGenerateFileForException() {

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> responseExportFile.toResponseEntity(null)
        );

        assertEquals("file empty", ex.getMessage());

    }

    @Test
    @SneakyThrows
    @DisplayName("should generate file when file is not found")
    void shouldGenerateFileWhenFileIsNotFound() {

        var mockData = GeneratedFile.builder()
                .filename("FILENAME")
                .extension(ExtensionEnum.CSV)
                .contentType("text/csv")
                .content(null)
                .path(null)
                .fileStorageMode(FileStorageMode.MEMORY)
                .createAt(LocalDateTime.of(2025, 10, 25, 22, 12, 23))
                .build();

        var result = responseExportFile.toResponseEntity(mockData);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

    }

    @Test
    @SneakyThrows
    @DisplayName("should generate file as stream response successfully")
    void shouldGenerateFileAsStreamResponseSuccessfully() {

        var tempPath = Files.createTempDirectory("PATH");
        var tempFile = tempPath.resolve("FILENAME.CSV");

        Files.writeString(tempFile, "BYTE");

        var mockData = GeneratedFile.builder()
                .filename("FILENAME")
                .extension(ExtensionEnum.CSV)
                .contentType("text/csv")
                .content(null)
                .path(tempFile.toString())
                .fileStorageMode(FileStorageMode.DISK_TEMP)
                .createAt(LocalDateTime.of(2025, 10, 25, 22, 12, 23))
                .build();

        var result = responseExportFile.toStreamResponse(mockData);
        var outputStream = new ByteArrayOutputStream();
        result.getBody().writeTo(outputStream);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("BYTE", outputStream.toString());
        assertFalse(Files.exists(tempFile));

    }

    @Test
    @DisplayName("should generate file when path is empty")
    void shouldGenerateFileWhenPathIsEmpty() {

        var mockData = GeneratedFile.builder()
                .filename("FILENAME")
                .extension(ExtensionEnum.CSV)
                .contentType("text/csv")
                .content("BYTE".getBytes())
                .path(null)
                .fileStorageMode(FileStorageMode.MEMORY)
                .createAt(LocalDateTime.of(2025, 10, 25, 22, 12, 23))
                .build();

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> responseExportFile.toStreamResponse(mockData)
        );

        assertEquals("file path is required for streaming response", ex.getMessage());

    }
    
}
