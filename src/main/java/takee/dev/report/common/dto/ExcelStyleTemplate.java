package takee.dev.report.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExcelStyleTemplate {

    private Style header = new Style();
    private Style body = new Style();
    private Style oddRow = new Style();
    private Style evenRow = new Style();
    private boolean freezeHeader;
    private boolean autoFilter;

    @Getter
    @Setter
    public static class Style {
        private String backgroundColor;
        private String fontColor;
        private Boolean bold;
        private Boolean wrapText;
        private String horizontalAlignment;
    }
}
