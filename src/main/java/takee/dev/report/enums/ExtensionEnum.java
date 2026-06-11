package takee.dev.report.enums;

import lombok.Getter;

@Getter
public enum ExtensionEnum {
    XLSX("xlsx"),
    PDF("pdf"),
    TXT("txt"),
    CSV("csv");

    private final String extension;

    ExtensionEnum(String extension) {
        this.extension = extension;
    }

}
