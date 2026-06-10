package takee.dev.report.enums;

public enum ExtensionEnum {
    XLSX("xlsx"),
    PDF("pdf"),
    TXT("txt"),
    CSV("csv");

    private final String extension;

    ExtensionEnum(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }
}
