package takee.dev.report.common.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import takee.dev.report.enums.ExtensionEnum;
import takee.dev.report.enums.FileStorageMode;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneratedFile {

    private String filename;
    private ExtensionEnum extension;
    private String contentType;
    private byte[] content;
    private String path;
    private FileStorageMode fileStorageMode;
    private LocalDateTime createAt;

}
