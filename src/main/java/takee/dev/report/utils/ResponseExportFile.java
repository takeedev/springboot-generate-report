package takee.dev.report.utils;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import takee.dev.report.common.CleanUpTempFileCommon;
import takee.dev.report.common.dto.GeneratedFile;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class ResponseExportFile {

    @SneakyThrows
    public ResponseEntity<StreamingResponseBody> toResponseEntity(GeneratedFile file) {

        if (file == null) {
            throw new IllegalArgumentException("file empty");
        }

        var fullFilename = file.getFilename() + "." + file.getExtension().getExtension();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename(fullFilename, StandardCharsets.UTF_8)
                        .build()
        );

        if (file.getContent() != null && file.getPath() == null) {
            StreamingResponseBody body = outputStream -> outputStream.write(file.getContent());
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.parseMediaType(file.getContentType()))
                    .headers(headers)
                    .body(body);
        } else if (file.getPath() != null && file.getContent() == null){
            var path = Path.of(file.getPath());
            if (!Files.exists(path)) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(null);
            }
            StreamingResponseBody body = outputStream -> writeFileAndCleanUp(file, path, outputStream);
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.parseMediaType(file.getContentType()))
                    .headers(headers)
                    .body(body);
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    @SneakyThrows
    public ResponseEntity<StreamingResponseBody> toStreamResponse(GeneratedFile file) {

        if (file.getPath() == null) {
            throw new IllegalArgumentException("file path is required for streaming response");
        }

        var fullFilename = file.getFilename() + "." + file.getExtension().getExtension();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename(fullFilename, StandardCharsets.UTF_8)
                        .build()
        );

        var path = Path.of(file.getPath());
        if (!Files.exists(path)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
        StreamingResponseBody body = outputStream -> writeFileAndCleanUp(file, path, outputStream);
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .headers(headers)
                .body(body);
    }

    private static void writeFileAndCleanUp(
            GeneratedFile file,
            Path path,
            OutputStream outputStream
    ) throws java.io.IOException {
        try {
            Files.copy(path, outputStream);
        } finally {
            CleanUpTempFileCommon.cleanUpTempFile(file);
        }
    }

}
