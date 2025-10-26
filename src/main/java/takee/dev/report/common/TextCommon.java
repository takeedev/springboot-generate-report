package takee.dev.report.common;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import takee.dev.report.common.interfece.CsvColumn;

import java.io.BufferedWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TextCommon {

    @SneakyThrows
    public <T> Path generateFileTextOrCsv(
            String directoryOut,
            String filename,
            String extension,
            String delimiter,
            List<T> object
    ) {

        if (object == null) {
            throw new IllegalArgumentException("Object is null");
        }

        Class<?> clazz = object.getFirst().getClass();
        Field[] fields = clazz.getDeclaredFields();

        Path outputPath = Path.of(directoryOut,filename + "." + extension);
        try (BufferedWriter writer = Files.newBufferedWriter(outputPath)) {
            String header = Arrays.stream(fields)
                    .map(field -> {
                        CsvColumn annotation = field.getAnnotation(CsvColumn.class);
                        return annotation != null ? annotation.header() : field.getName();
                    })
                    .collect(Collectors.joining(delimiter));
            writer.write(header);
            writer.newLine();
            for (T obj : object) {
                StringBuilder line = new StringBuilder();
                for (int i = 0; i < fields.length; i++) {
                    Field filed = fields[i];
                    var fileName = filed.getName();
                    Object value = getValueViaGetter(obj, clazz, fileName);
                    var formatted = formatValue(filed,value);
                    line.append(formatted != null ? formatted : "");
                    if (i < fields.length - 1) line.append(delimiter);
                }
                writer.write(line.toString());
                writer.newLine();
            }
        }
        return outputPath;
    }

    @SneakyThrows
    private static Object getValueViaGetter(
            Object obj,
            Class<?> clazz,
            String fieldName
    ) {
            String methodName = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
            Method getter = clazz.getMethod(methodName);
            return getter.invoke(obj);
    }

    private static String formatValue(
            Field field,
            Object object
    ) {
        CsvColumn annotation = field.getAnnotation(CsvColumn.class);
        if (annotation != null && !annotation.format().isEmpty()) {
            String pattern = annotation.format();
            log.info("pattern {}", pattern);
            if (object instanceof Number number) {
                DecimalFormat df = new DecimalFormat(pattern);
                return df.format(number);
            }
            if (object instanceof LocalDate date) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.of("Asia/Bangkok"));
                return date.format(formatter);
            }
            if (object instanceof LocalDateTime dateTime) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.of("Asia/Bangkok"));
                return dateTime.format(formatter);
            }
        }
        return object.toString();
    }

}
