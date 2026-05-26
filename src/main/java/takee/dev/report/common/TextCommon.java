package takee.dev.report.common;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import takee.dev.report.common.dto.GeneratedFile;
import takee.dev.report.common.interfece.CsvColumn;
import takee.dev.report.enums.ExtensionEnum;
import takee.dev.report.enums.FileStorageMode;

@Slf4j
@Component
public class TextCommon {

    /**
     * Generate a text (.txt) or CSV (.csv) file form a list of object
     *
     * @Param directory the directory path where the generate file will be created
     * @Param filename the base name of the output file
     * @Param extension the file extension, e.g., "txt" or "csv"
     * @Param delimiter the delimiter use to separate colum, e.g., "," or "|"
     * @Param object the list of object to be written into the file
     * @Param reqHeader whether to include a header row (true = include header)
     *
     * @Return
     */

    @SneakyThrows
    public <T> GeneratedFile generateCsvInMemory(
            String filename,
            ExtensionEnum extension,
            String delimiter,
            List<T> objects,
            boolean reqHeader,
            Charset charset,
            boolean addBom
    ) {

        if (objects == null || objects.isEmpty()) {
            throw new IllegalArgumentException("Object is null");
        }

        Field[] fields = objects.getFirst().getClass().getDeclaredFields();

        try (
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                Writer writer = new OutputStreamWriter(out, charset);
                BufferedWriter bufferedWriter = new BufferedWriter(writer)
        ) {
            if (addBom && charset.equals(StandardCharsets.UTF_8)) {
                out.write(0xEF);
                out.write(0xBB);
                out.write(0xBF);
            }

            writeCsv(objects, fields, delimiter, reqHeader, bufferedWriter);
            return GeneratedFile.builder()
                    .filename(filename)
                    .extension(extension)
                    .contentType("text/csv")
                    .content(out.toByteArray())
                    .fileStorageMode(FileStorageMode.MEMORY)
                    .createAt(LocalDateTime.now())
                    .build();
        }
    }

    @SneakyThrows
    public <T> GeneratedFile generateCsvToDisk(
            String directoryOut,
            String filename,
            ExtensionEnum extension,
            String delimiter,
            List<T> objects,
            boolean reqHeader,
            Charset charset,
            boolean addBom
    ) {

        if (objects == null || objects.isEmpty()) {
            throw new IllegalArgumentException("Object is null");
        }

        Field[] fields = objects.getFirst().getClass().getDeclaredFields();
        Path outputPath = Path.of(directoryOut, filename + "." + extension);

        try (
                OutputStream out = Files.newOutputStream(outputPath);
                Writer writer = new OutputStreamWriter(out, charset);
                BufferedWriter bufferedWriter = new BufferedWriter(writer)
        ) {
            if (addBom && charset.equals(StandardCharsets.UTF_8)) {
                out.write(0xEF);
                out.write(0xBB);
                out.write(0xBF);
            }

            writeCsv(objects, fields, delimiter, reqHeader, bufferedWriter);
        }

        return GeneratedFile.builder()
                .filename(filename)
                .extension(extension)
                .contentType("text/csv")
                .path(outputPath.toString())
                .fileStorageMode(FileStorageMode.DISK_TEMP)
                .createAt(LocalDateTime.now())
                .build();
    }
    private static void createHeader(
            String delimiter,
            Field[] fields,
            BufferedWriter writer
    ) throws IOException {
        String header = Arrays.stream(fields)
                .map(field -> {
                    CsvColumn annotation = field.getAnnotation(CsvColumn.class);
                    return annotation != null ? annotation.header() : field.getName();
                })
                .collect(Collectors.joining(delimiter));
        writer.write(header);
        writer.newLine();
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

    private static <T> void writeCsv(
            List<T> objects,
            Field[] fields,
            String delimiter,
            boolean reqHeader,
            BufferedWriter writer
    ) throws IOException {

        if (reqHeader) {
            createHeader(delimiter, fields, writer);
        }

        Class<?> clazz = objects.getFirst().getClass();

        for (T obj : objects) {
            StringBuilder line = new StringBuilder();
            addDataLine(fields, delimiter, obj, clazz, line);
            writer.write(line.toString());
            writer.newLine();
        }

        writer.flush();
    }

    private static <T> void addDataLine(Field[] fields, String delimiter, T obj, Class<?> clazz, StringBuilder line) {
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            Object value = getValueViaGetter(obj, clazz, field.getName());
            String formatted = formatValue(field, value);

            line.append(formatted != null ? formatted : "");
            if (i < fields.length - 1) {
                line.append(delimiter);
            }
        }
    }

}
