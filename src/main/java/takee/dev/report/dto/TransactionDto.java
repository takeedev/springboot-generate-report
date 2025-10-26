package takee.dev.report.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import takee.dev.report.common.interfece.CsvColumn;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
@Builder
public class TransactionDto {

    @CsvColumn(header = "รหัส")
    private String id;

    @CsvColumn(header = "ชื่อ")
    private String name;

    @CsvColumn(header = "จำนวนเงิน", format = "#,##0.00")
    private double amount;

    @CsvColumn(header = "วันที่", format = "yyyy-MM-dd")
    private LocalDate date;

    @CsvColumn(header = "วันที่และเวลา", format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTime;
}
