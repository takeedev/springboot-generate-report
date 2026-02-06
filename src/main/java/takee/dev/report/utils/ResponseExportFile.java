package takee.dev.report.utils;

import lombok.SneakyThrows;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import takee.dev.report.common.dto.GeneratedFile;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

@Service
public class ResponseExportFile {

    @SneakyThrows
    public ResponseEntity<byte[]> toResponseEntity(GeneratedFile file) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename(file.getFilename(),StandardCharsets.UTF_8)
                        .build()
        );

        if (file.getContent() != null) {
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.parseMediaType(file.getContentType()))
                    .headers(headers)
                    .body(file.getContent());
        } else if (file.getPath() != null){
            var path = Path.of(file.getPath());
            var bytes = FileCopyUtils.copyToByteArray(new FileInputStream(path.toFile()));
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.parseMediaType(file.getContentType()))
                    .headers(headers)
                    .body(bytes);
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    @SneakyThrows
    public ResponseEntity<Resource> toStreamResponse(GeneratedFile file) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename(file.getFilename(),StandardCharsets.UTF_8)
                        .build()
        );

        if (file.getPath() == null)
            throw new IllegalArgumentException("File path is required for streaming response");

        var path = Path.of(file.getFilename());
        var resource = new InputStreamResource(new FileInputStream(path.toFile()));
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .headers(headers)
                .body(resource);
    }

}
