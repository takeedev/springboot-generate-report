package takee.dev.report.common.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import takee.dev.report.enums.ExtensionEnum;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneratedFile {

    private String filename;
    private ExtensionEnum extension;
    private String contentType;
    private byte[] content;
    private LocalDateTime createAt;

}
