package takee.dev.report.common;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import takee.dev.report.common.dto.GeneratedFile;
import takee.dev.report.enums.FileStorageMode;

import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class CleanUpTempFileCommon {

    @SneakyThrows
    public static void cleanUpTempFile(GeneratedFile file) {
        if (file.getFileStorageMode() == FileStorageMode.DISK_TEMP
                && file.getPath() != null
        ) {
            Files.deleteIfExists(Path.of(file.getPath()));
        }
    }

}
