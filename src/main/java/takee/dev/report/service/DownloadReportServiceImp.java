package takee.dev.report.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import takee.dev.report.common.ExcelCommon;
import takee.dev.report.common.dto.GeneratedFile;
import takee.dev.report.dto.TransactionDto;
import takee.dev.report.entity.Reports;
import takee.dev.report.repository.ReportsRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DownloadReportServiceImp implements DownloadReportService {

    private final ExcelCommon excelCommon;
    private final ReportsRepository reportsRepository;

    @Override
    public GeneratedFile genByReport(String reportNo) {
        Reports result = reportsRepository.findByReportNo(reportNo);
        List<TransactionDto> transactionList = new ArrayList<>();

        for (int i = 0; i < 500000; i++) {
            TransactionDto transactionDto = TransactionDto.builder()
                    .id("ID")
                    .name("NAME")
                    .amount(1)
                    .date(LocalDate.of(2025, 10, 20))
                    .dateTime(LocalDateTime.of(2025, 10, 25, 22, 12, 23))
                    .build();
            transactionList.add(transactionDto);
        }

        log.info("Size : {}", transactionList.size());

        Map<String, List<TransactionDto>> dataList = new HashMap<>();
        dataList.put("SHEET1", transactionList);

        if (result != null) {
            return excelCommon.generateMultiSheetExcel(
                    result.getReportName(),
                    dataList
            );
        }
        return null;
    }

    @Override
    public GeneratedFile genByReportLargeFiles(String reportNo) {
        Reports result = reportsRepository.findByReportNo(reportNo);
        List<TransactionDto> transactionList = new ArrayList<>();

        for (int i = 0; i < 100000; i++) {
            TransactionDto transactionDto = TransactionDto.builder()
                    .id("ID")
                    .name("NAME")
                    .amount(1)
                    .date(LocalDate.of(2025, 10, 20))
                    .dateTime(LocalDateTime.of(2025, 10, 25, 22, 12, 23))
                    .build();
            transactionList.add(transactionDto);
        }

        log.info("Transaction Size : {}", transactionList.size());

        Map<String, List<TransactionDto>> dataList = new HashMap<>();
        dataList.put("SHEET1", transactionList);

        if (result!= null && !transactionList.isEmpty()) {
            return excelCommon.generateMultiSheetExcelForLargeFiles(
                    result.getReportName(),
                    dataList,
                    20000
            );
        }
        return null;
    }
}
