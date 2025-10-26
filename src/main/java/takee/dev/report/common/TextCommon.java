package takee.dev.report.common;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import takee.dev.report.common.interfece.CsvColumn;

import java.io.BufferedWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
                    var filed = fields[i];
                    var fileName = filed.getName();
                    Object value = getValueViaGetter(obj, clazz, fileName);
                    line.append(value != null ? value.toString() : "");
                    if (i < fields.length - 1) line.append(delimiter);
                }
                writer.write(line.toString());
                writer.newLine();
            }
        }
        return outputPath;
    }

    private static Object getValueViaGetter(
            Object obj,
            Class<?> clazz,
            String fieldName
    ) {
        try {
            String methodName = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
            Method getter = clazz.getMethod(methodName);
            return getter.invoke(obj);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

}
