package takee.dev.report.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import takee.dev.report.common.ExcelCommon;
import takee.dev.report.common.TextCommon;
import takee.dev.report.common.dto.GeneratedFile;
import takee.dev.report.dto.TransactionDto;
import takee.dev.report.entity.Reports;
import takee.dev.report.enums.ExtensionEnum;
import takee.dev.report.repository.ReportsRepository;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DownloadReportServiceImp implements DownloadReportService {

    private final TextCommon textCommon;
    private final ExcelCommon excelCommon;
    private final ReportsRepository reportsRepository;

    private static final int NORMAL_EXCEL_ROWS = 500_000;
    private static final int LARGE_EXCEL_ROWS = 1_040_000;
    private static final int TEXT_ROWS = 100_000;

    @Override
    public GeneratedFile genByReport(String reportNo) {
        Reports result = reportsRepository.findByReportNo(reportNo);
        List<TransactionDto> transactionList = generateMockTransactions(NORMAL_EXCEL_ROWS);

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
        List<TransactionDto> transactionList = generateMockTransactions(LARGE_EXCEL_ROWS);

        log.info("Transaction Size : {}", transactionList.size());

        Map<String, List<TransactionDto>> dataList = new HashMap<>();
        dataList.put("SHEET1", transactionList);

        if (result != null && !transactionList.isEmpty()) {
            return excelCommon.generateMultiSheetExcelForLargeFiles(
                    result.getReportName(),
                    dataList,
                    20000
            );
        }
        return null;
    }

    @Override
    public GeneratedFile genByReportCsv(String reportNo) {
        Reports result = reportsRepository.findByReportNo(reportNo);
        List<TransactionDto> transactionList = generateMockTransactions(TEXT_ROWS);

        log.info("Transaction Size Csv : {}", transactionList.size());

        if (result!= null && !transactionList.isEmpty()) {
            return textCommon.generateCsvInMemory(
                    result.getReportName(),
                    ExtensionEnum.CSV,
                    "|",
                    transactionList,
                    true,
                    StandardCharsets.UTF_8,
                   false
            );
        }
        return null;
    }

    @Override
    public GeneratedFile genByReportText(String reportNo) {
        Reports result = reportsRepository.findByReportNo(reportNo);
        List<TransactionDto> transactionList = generateMockTransactions(TEXT_ROWS);

        log.info("Transaction Size Text : {}", transactionList.size());

        if (result!= null && !transactionList.isEmpty()) {
            return textCommon.generateCsvInMemory(
                    result.getReportName(),
                    ExtensionEnum.TXT,
                    ",",
                    transactionList,
                    true,
                    StandardCharsets.UTF_8,
                    false
            );
        }
        return null;
    }

    @Override
    public GeneratedFile genByReportCsvToDisk(String reportNo) {
        Reports result = reportsRepository.findByReportNo(reportNo);
        List<TransactionDto> transactionList = generateMockTransactions(TEXT_ROWS);

        log.info("Transaction Size Text : {}", transactionList.size());

        if (result!= null && !transactionList.isEmpty()) {
            return textCommon.generateCsvToDisk(
                    result.getPathOut(),
                    result.getReportName(),
                    ExtensionEnum.CSV,
                    "|",
                    transactionList,
                    true,
                    StandardCharsets.UTF_8,
                    false
            );
        }
        return null;
    }

    @Override
    public GeneratedFile genByReportTextToDisk(String reportNo) {
        Reports result = reportsRepository.findByReportNo(reportNo);
        List<TransactionDto> transactionList = generateMockTransactions(TEXT_ROWS);

        log.info("Transaction Size Text : {}", transactionList.size());

        if (result!= null && !transactionList.isEmpty()) {
            return textCommon.generateCsvToDisk(
                    result.getPathOut(),
                    result.getReportName(),
                    ExtensionEnum.TXT,
                    ",",
                    transactionList,
                    true,
                    StandardCharsets.UTF_8,
                    false
            );
        }
        return null;
    }

    private static List<TransactionDto> generateMockTransactions(int rows) {
        TransactionDto transactionDto = TransactionDto.builder()
                .id("ID")
                .name("NAME")
                .amount(1)
                .date(LocalDate.of(2025, 10, 20))
                .dateTime(LocalDateTime.of(2025, 10, 25, 22, 12, 23))
                .build();
        return Collections.nCopies(rows, transactionDto);
    }
}
