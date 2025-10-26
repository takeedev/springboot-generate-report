package takee.dev.report.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import takee.dev.report.common.interfece.CsvColumn;

@Getter
@Setter
@Builder
public class TransactionDto {

    @CsvColumn(header = "รหัส")
    private String id;

    @CsvColumn(header = "ชื่อ")
    private String name;

    @CsvColumn(header = "จำนวนเงิน")
    private double amount;

}
