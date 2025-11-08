package takee.dev.report.common;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import takee.dev.report.common.interfece.CsvColumn;

@Slf4j
@Component
public class ExcelCommon {

    @SneakyThrows
    public <T> Path generateMultiSheetExcel(
            String directoryOut,
            String filename,
            Map<String, List<T>> dataMap
    ) {

        var outputPath = Path.of(directoryOut,filename + ".xlsx");
        log.info("output path {}", outputPath);

        try (Workbook workbook = new XSSFWorkbook()) {
            CreationHelper helper = workbook.getCreationHelper();
            for (var entry : dataMap.entrySet()) {
                var sheetName = entry.getKey();
                List<T> dataList = entry.getValue();

                if (dataList == null || dataList.isEmpty()) continue;

                Class<?> clazz = dataList.getFirst().getClass();
                Field[] fields = clazz.getDeclaredFields();

                Sheet sheet = workbook.createSheet(sheetName);
                Row headerRow = sheet.createRow(0);

                for (int i = 0; i < fields.length; i++ ) {
                    CsvColumn anno = fields[i].getAnnotation(CsvColumn.class);
                    var header = anno != null ? anno.header() : fields[i].getName();
                    headerRow.createCell(i).setCellValue(header);
                }

                for (int i = 0; i < dataList.size(); i++) {
                    T obj = dataList.get(i);
                    Row row = sheet.createRow(i + 1);
                    for (int c = 0; c < fields.length; c++) {

                    }
                }


            }

        }

        return null;
    }

    private static Object getValueViaGetter(
            Object object,
            Class<?> clazz,
            String fileName
    ) {
        return null;
    }

}
