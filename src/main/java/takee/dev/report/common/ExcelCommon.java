package takee.dev.report.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import takee.dev.report.common.dto.GeneratedFile;
import takee.dev.report.common.interfece.CsvColumn;
import takee.dev.report.enums.ExtensionEnum;

@Slf4j
@Component
public class ExcelCommon {

    private static final long MEMORY_THRESHOLD_BYTES = 10 * 1024 * 1024;

    @SneakyThrows
    public <T> GeneratedFile generateMultiSheetExcel(
            String directoryOut,
            String filename,
            Map<String, List<T>> dataMap
    ) {

        var outputPath = Path.of(directoryOut, filename + ".xlsx");
        log.info("output path {}", outputPath);

        try (Workbook workbook = new XSSFWorkbook()) {
            for (var entry : dataMap.entrySet()) {
                var sheetName = entry.getKey();
                List<T> dataList = entry.getValue();

                if (dataList == null || dataList.isEmpty()) continue;

                Class<?> clazz = dataList.getFirst().getClass();
                Field[] fields = clazz.getDeclaredFields();

                Sheet sheet = workbook.createSheet(sheetName);
                Row headerRow = sheet.createRow(0);
                setHeader(fields, headerRow);
                setData(dataList, sheet, fields, clazz);
                setAutoSizeColumn(fields, sheet);
            }
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                workbook.write(out);
                log.info("Multi-Sheet Excel Generated {}", outputPath);
                long sizeFile = out.size();
                if (sizeFile <= MEMORY_THRESHOLD_BYTES) {
                    return GeneratedFile.builder()
                            .filename(filename)
                            .extension(ExtensionEnum.XLSX)
                            .contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                            .content(out.toByteArray())
                            .createAt(LocalDateTime.now())
                            .build();
                } else {
                    Path tempFile = Files.createTempFile(filename + "_", ".xlsx");
                    try (OutputStream outs = Files.newOutputStream(tempFile)) {
                        workbook.write(outs);
                    }
                    log.info("Export using disk mode, file size {} MB", sizeFile / 1_000_000);
                    return GeneratedFile.builder()
                            .filename(filename)
                            .extension(ExtensionEnum.XLSX)
                            .contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                            .path(tempFile.toString())
                            .createAt(LocalDateTime.now())
                            .build();
                }
            }
        }
    }

    private static void setAutoSizeColumn(Field[] fields, Sheet sheet) {
        for (int i = 0; i < fields.length; i++) sheet.autoSizeColumn(i);
    }

    private static void setHeader(Field[] fields, Row headerRow) {
        for (int i = 0; i < fields.length; i++) {
            CsvColumn anno = fields[i].getAnnotation(CsvColumn.class);
            var header = anno != null ? anno.header() : fields[i].getName();
            headerRow.createCell(i).setCellValue(header);
        }
    }

    private static <T> void setData(List<T> dataList, Sheet sheet, Field[] fields, Class<?> clazz) {
        for (int i = 0; i < dataList.size(); i++) {
            T obj = dataList.get(i);
            Row row = sheet.createRow(i + 1);
            for (int c = 0; c < fields.length; c++) {
                Object val = getValueViaGetter(obj, clazz, fields[i].getName());
                Cell cell = row.createCell(i);
                if (val instanceof Number n) cell.setCellValue(n.doubleValue());
                else cell.setCellValue(val != null ? val.toString() : "");
            }
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

}
