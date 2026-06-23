package takee.dev.report.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import takee.dev.report.common.dto.ExcelStyleTemplate;
import takee.dev.report.common.dto.GeneratedFile;
import takee.dev.report.common.interfece.CsvColumn;
import takee.dev.report.enums.ExtensionEnum;
import takee.dev.report.enums.FileStorageMode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ExcelCommon {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @SneakyThrows
    public <T> GeneratedFile generateMultiSheetExcelForLargeFiles(
            String filename,
            Map<String, List<T>> dataMap,
            int rowsSize
    ) {
        return generateMultiSheetExcelForLargeFiles(filename, dataMap, rowsSize, null);
    }

    @SneakyThrows
    public <T> GeneratedFile generateMultiSheetExcelForLargeFiles(
            String filename,
            Map<String, List<T>> dataMap,
            int rowsSize,
            String template
    ) {

        if (dataMap.isEmpty()) {
            throw new IllegalArgumentException("The data is empty");
        }

        SXSSFWorkbook workbook = new SXSSFWorkbook(rowsSize);
        try (workbook) {
            ExcelStyleTemplate styleTemplate = resolveStyleTemplate(template);
            WorkbookStyles workbookStyles = new WorkbookStyles(workbook, styleTemplate);
            for (var entry : dataMap.entrySet()) {
                var sheetName = entry.getKey();
                List<T> dataList = entry.getValue();

                if (dataList == null || dataList.isEmpty()) continue;

                Class<?> clazz = dataList.getFirst().getClass();
                Field[] fields = clazz.getDeclaredFields();

                Sheet sheet = workbook.createSheet(sheetName);
                Row headerRow = sheet.createRow(0);
                setHeader(fields, headerRow, workbookStyles);
                setData(dataList, sheet, fields, clazz, workbookStyles);
                applySheetOptions(sheet, fields, styleTemplate);
            }
            return getGeneratedFileToDisk(filename, workbook);
        } finally {
            workbook.dispose();
        }
    }

    @SneakyThrows
    public <T> GeneratedFile generateMultiSheetExcel(
            String filename,
            Map<String, List<T>> dataMap
    ) {
        return generateMultiSheetExcel(filename, dataMap, null);
    }

    @SneakyThrows
    public <T> GeneratedFile generateMultiSheetExcel(
            String filename,
            Map<String, List<T>> dataMap,
            String template
    ) {

        if (dataMap.isEmpty()) {
            throw new IllegalArgumentException("The data is empty");
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            ExcelStyleTemplate styleTemplate = resolveStyleTemplate(template);
            WorkbookStyles workbookStyles = new WorkbookStyles(workbook, styleTemplate);
            for (var entry : dataMap.entrySet()) {
                var sheetName = entry.getKey();
                List<T> dataList = entry.getValue();

                if (dataList == null || dataList.isEmpty()) continue;

                Class<?> clazz = dataList.getFirst().getClass();
                Field[] fields = clazz.getDeclaredFields();

                Sheet sheet = workbook.createSheet(sheetName);
                Row headerRow = sheet.createRow(0);
                setHeader(fields, headerRow, workbookStyles);
                setData(dataList, sheet, fields, clazz, workbookStyles);
                applySheetOptions(sheet, fields, styleTemplate);
                setAutoSizeColumn(fields, sheet);
            }
            return getGeneratedFileInMemory(filename, workbook);
        }
    }

    private static void setAutoSizeColumn(Field[] fields, Sheet sheet) {
        for (int i = 0; i < fields.length; i++) sheet.autoSizeColumn(i);
    }

    private static void setHeader(Field[] fields, Row headerRow, WorkbookStyles workbookStyles) {
        for (int i = 0; i < fields.length; i++) {
            CsvColumn anno = fields[i].getAnnotation(CsvColumn.class);
            var header = anno != null ? anno.header() : fields[i].getName();
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(header);
            cell.setCellStyle(workbookStyles.headerStyle());
        }
    }

    private static <T> void setData(
            List<T> dataList,
            Sheet sheet,
            Field[] fields,
            Class<?> clazz,
            WorkbookStyles workbookStyles
    ) {
        for (int i = 0; i < dataList.size(); i++) {
            T obj = dataList.get(i);
            Row row = sheet.createRow(i + 1);
            for (int c = 0; c < fields.length; c++) {
                Field field = fields[c];
                Object val = getValueViaGetter(obj, clazz, field.getName());
                Cell cell = row.createCell(c);
                if (val instanceof Number n) cell.setCellValue(n.doubleValue());
                else if (val instanceof LocalDate date) cell.setCellValue(date);
                else if (val instanceof LocalDateTime dateTime) cell.setCellValue(dateTime);
                else cell.setCellValue(val != null ? val.toString() : "");
                cell.setCellStyle(workbookStyles.dataStyle(field, i));
            }
        }
    }

    private static void applySheetOptions(Sheet sheet, Field[] fields, ExcelStyleTemplate styleTemplate) {
        if (styleTemplate.isFreezeHeader()) {
            sheet.createFreezePane(0, 1);
        }
        if (styleTemplate.isAutoFilter()) {
            sheet.setAutoFilter(new CellRangeAddress(0, 0, 0, fields.length - 1));
        }
    }

    private static ExcelStyleTemplate resolveStyleTemplate(String template) {
        if (template == null || template.isBlank()) {
            return new ExcelStyleTemplate();
        }

        String value = template.trim();
        if (value.startsWith("{")) {
            try {
                return OBJECT_MAPPER.readValue(value, ExcelStyleTemplate.class);
            } catch (Exception e) {
                log.warn("Cannot parse excel style template, use default style. template={}", template);
                return new ExcelStyleTemplate();
            }
        }

        return loadStyleTemplateFromResource(value);
    }

    private static ExcelStyleTemplate loadStyleTemplateFromResource(String template) {
        String templateName = template.startsWith("file:") ? template.substring("file:".length()) : template.toLowerCase();
        if (templateName.contains("/") || templateName.contains("\\") || templateName.contains("..")) {
            log.warn("Unsupported excel template resource name: {}", template);
            return new ExcelStyleTemplate();
        }
        if (!templateName.endsWith(".json")) {
            templateName = templateName + ".json";
        }

        String resourcePath = "templates/excel/" + templateName;
        try (InputStream inputStream = ExcelCommon.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                log.warn("Excel template resource not found: {}", resourcePath);
                return new ExcelStyleTemplate();
            }
            return OBJECT_MAPPER.readValue(inputStream, ExcelStyleTemplate.class);
        } catch (IOException e) {
            log.warn("Cannot parse excel template resource: {}", resourcePath, e);
            return new ExcelStyleTemplate();
        }
    }

    private static Object getValueViaGetter(
            Object object,
            Class<?> clazz,
            String fileName
    ) {
        try {
            var methodName = "get" + Character.toUpperCase(fileName.charAt(0)) + fileName.substring(1);
            Method getter = clazz.getMethod(methodName);
            return getter.invoke(object);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private static GeneratedFile getGeneratedFileInMemory(String filename, Workbook workbook) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            workbook.write(out);
            return GeneratedFile.builder()
                    .filename(filename)
                    .extension(ExtensionEnum.XLSX)
                    .contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    .content(out.toByteArray())
                    .fileStorageMode(FileStorageMode.MEMORY)
                    .createAt(LocalDateTime.now())
                    .build();
        }
    }

    private static GeneratedFile getGeneratedFileToDisk(String filename, Workbook workbook) throws IOException {
        Path tempFile = Files.createTempFile(filename + "_", ".xlsx");
        try (OutputStream out = Files.newOutputStream(tempFile)) {
            workbook.write(out);
        }
        log.info("Export using disk mode, file path {}", tempFile);
        return GeneratedFile.builder()
                .filename(filename)
                .extension(ExtensionEnum.XLSX)
                .contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .path(tempFile.toString())
                .fileStorageMode(FileStorageMode.DISK_TEMP)
                .createAt(LocalDateTime.now())
                .build();
    }

    private static class WorkbookStyles {
        private final Workbook workbook;
        private final CellStyle headerStyle;
        private final CellStyle bodyStyle;
        private final CellStyle oddRowStyle;
        private final CellStyle evenRowStyle;
        private final boolean hasOddRowStyle;
        private final boolean hasEvenRowStyle;
        private final Map<String, CellStyle> formattedStyles = new HashMap<>();

        WorkbookStyles(Workbook workbook, ExcelStyleTemplate template) {
            this.workbook = workbook;
            this.headerStyle = createStyle(workbook, template.getHeader());
            this.bodyStyle = createStyle(workbook, template.getBody());
            this.oddRowStyle = createStyle(workbook, template.getOddRow());
            this.evenRowStyle = createStyle(workbook, template.getEvenRow());
            this.hasOddRowStyle = hasStyleDefinition(template.getOddRow());
            this.hasEvenRowStyle = hasStyleDefinition(template.getEvenRow());
        }

        CellStyle headerStyle() {
            return headerStyle;
        }

        CellStyle dataStyle(Field field, int rowIndex) {
            CsvColumn annotation = field.getAnnotation(CsvColumn.class);
            String format = annotation != null ? annotation.format() : "";
            CellStyle selectedStyle = rowIndex % 2 == 0 && hasOddRowStyle ? oddRowStyle : bodyStyle;
            if (rowIndex % 2 != 0 && hasEvenRowStyle) selectedStyle = evenRowStyle;
            if (format == null || format.isBlank()) {
                return selectedStyle;
            }

            CellStyle baseStyle = selectedStyle;
            String key = System.identityHashCode(baseStyle) + ":" + format;
            return formattedStyles.computeIfAbsent(key, ignored -> {
                CellStyle style = workbook.createCellStyle();
                style.cloneStyleFrom(baseStyle);
                style.setDataFormat(workbook.createDataFormat().getFormat(format));
                return style;
            });
        }
    }

    private static CellStyle createStyle(Workbook workbook, ExcelStyleTemplate.Style styleTemplate) {
        CellStyle cellStyle = workbook.createCellStyle();
        if (styleTemplate == null) {
            return cellStyle;
        }

        applyBackgroundColor(cellStyle, styleTemplate.getBackgroundColor());
        applyAlignment(cellStyle, styleTemplate.getHorizontalAlignment());
        if (styleTemplate.getWrapText() != null) {
            cellStyle.setWrapText(styleTemplate.getWrapText());
        }

        if (styleTemplate.getBold() != null || isNotBlank(styleTemplate.getFontColor())) {
            Font font = workbook.createFont();
            if (styleTemplate.getBold() != null) {
                font.setBold(styleTemplate.getBold());
            }
            applyFontColor(font, styleTemplate.getFontColor());
            cellStyle.setFont(font);
        }

        return cellStyle;
    }

    private static void applyBackgroundColor(CellStyle cellStyle, String color) {
        if (!isNotBlank(color) || !(cellStyle instanceof XSSFCellStyle xssfStyle)) {
            return;
        }
        try {
            xssfStyle.setFillForegroundColor(toXssfColor(color));
            xssfStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        } catch (IllegalArgumentException e) {
            log.warn("Unsupported excel background color: {}", color);
        }
    }

    private static void applyFontColor(Font font, String color) {
        if (!isNotBlank(color) || !(font instanceof org.apache.poi.xssf.usermodel.XSSFFont xssfFont)) {
            return;
        }
        try {
            xssfFont.setColor(toXssfColor(color));
        } catch (IllegalArgumentException e) {
            log.warn("Unsupported excel font color: {}", color);
        }
    }

    private static void applyAlignment(CellStyle cellStyle, String alignment) {
        if (!isNotBlank(alignment)) {
            return;
        }
        try {
            cellStyle.setAlignment(HorizontalAlignment.valueOf(alignment.toUpperCase()));
        } catch (IllegalArgumentException e) {
            log.warn("Unsupported excel horizontal alignment: {}", alignment);
        }
    }

    private static XSSFColor toXssfColor(String color) {
        String hex = color.startsWith("#") ? color.substring(1) : color;
        return new XSSFColor(hexToRgb(hex), new DefaultIndexedColorMap());
    }

    private static byte[] hexToRgb(String hex) {
        if (hex.length() != 6) {
            throw new IllegalArgumentException("Excel color must be RGB hex format");
        }
        return new byte[]{
                (byte) Integer.parseInt(hex.substring(0, 2), 16),
                (byte) Integer.parseInt(hex.substring(2, 4), 16),
                (byte) Integer.parseInt(hex.substring(4, 6), 16)
        };
    }

    private static boolean hasStyleDefinition(ExcelStyleTemplate.Style style) {
        return style != null && (
                isNotBlank(style.getBackgroundColor())
                        || isNotBlank(style.getFontColor())
                        || style.getBold() != null
                        || style.getWrapText() != null
                        || isNotBlank(style.getHorizontalAlignment())
        );
    }

    private static boolean isNotBlank(String value) {
        return value != null && !value.isBlank();
    }
}
