package takee.dev.report.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import takee.dev.report.common.ExcelCommon;
import takee.dev.report.common.dto.GeneratedFile;
import takee.dev.report.dto.TransactionDto;
import takee.dev.report.entity.Reports;
import takee.dev.report.repository.ReportsRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DownloadReportServiceImp implements DownloadReportService {

    private final ExcelCommon excelCommon;
    private final ReportsRepository reportsRepository;

    @Override
    public GeneratedFile genByReport(String reportNo) {
        Reports result = reportsRepository.findByReportNo(reportNo);
        TransactionDto transactionDto = TransactionDto
                .builder()
                .id("ID")
                .name("NAME")
                .amount(1)
                .date(LocalDate.of(2025, 10, 20))
                .dateTime(LocalDateTime.of(2025, 10, 25, 22, 12, 23))
                .build();

        Map<String, List<TransactionDto>> dataList = new HashMap<>();
        dataList.put("SHEET1", List.of(transactionDto));

        if (result != null) {
            return excelCommon.generateMultiSheetExcel(
                    result.getReportName(),
                    dataList
            );
        }
        return null;
    }
}
