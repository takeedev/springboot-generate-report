SET NOCOUNT ON;

DECLARE @now datetime2 = SYSUTCDATETIME();

IF NOT EXISTS (SELECT 1 FROM reports WHERE report_no = '001')
BEGIN
    INSERT INTO reports (
        id,
        report_no,
        report_name,
        type_report,
        template,
        path_out,
        is_active,
        created_at,
        updated_at
    )
    VALUES (
        NEWID(),
        '001',
        'Daily Sales Excel Report',
        'EXCEL',
        'blue',
        '/tmp/reports/daily-sales.xlsx',
        1,
        @now,
        @now
    );
END;

IF NOT EXISTS (SELECT 1 FROM reports WHERE report_no = '002')
BEGIN
    INSERT INTO reports (
        id,
        report_no,
        report_name,
        type_report,
        template,
        path_out,
        is_active,
        created_at,
        updated_at
    )
    VALUES (
        NEWID(),
        '002',
        'Monthly Revenue CSV Report',
        'CSV',
        'green',
        '/tmp/reports/monthly-revenue.csv',
        1,
        @now,
        @now
    );
END;

IF NOT EXISTS (SELECT 1 FROM reports WHERE report_no = '003')
BEGIN
    INSERT INTO reports (
        id,
        report_no,
        report_name,
        type_report,
        template,
        path_out,
        is_active,
        created_at,
        updated_at
    )
    VALUES (
        NEWID(),
        '003',
        'Transaction Text Report',
        'TEXT',
        'orange',
        '/tmp/reports/transactions.txt',
        1,
        @now,
        @now
    );
END;

IF NOT EXISTS (
    SELECT 1 FROM daily_report
    WHERE report_name = 'Daily Sales Excel Report'
      AND report_type = 'EXCEL'
)
BEGIN
    INSERT INTO daily_report (
        id,
        report_name,
        report_type,
        status,
        created_at,
        updated_at
    )
    VALUES (
        NEWID(),
        'Daily Sales Excel Report',
        'EXCEL',
        'READY',
        @now,
        @now
    );
END;

IF NOT EXISTS (
    SELECT 1 FROM daily_report
    WHERE report_name = 'Monthly Revenue CSV Report'
      AND report_type = 'CSV'
)
BEGIN
    INSERT INTO daily_report (
        id,
        report_name,
        report_type,
        status,
        created_at,
        updated_at
    )
    VALUES (
        NEWID(),
        'Monthly Revenue CSV Report',
        'CSV',
        'READY',
        @now,
        @now
    );
END;

IF NOT EXISTS (
    SELECT 1 FROM daily_report
    WHERE report_name = 'Transaction Text Report'
      AND report_type = 'TEXT'
)
BEGIN
    INSERT INTO daily_report (
        id,
        report_name,
        report_type,
        status,
        created_at,
        updated_at
    )
    VALUES (
        NEWID(),
        'Transaction Text Report',
        'TEXT',
        'READY',
        @now,
        @now
    );
END;

SELECT report_no, report_name, type_report, template, path_out, is_active
FROM reports
ORDER BY report_no;

SELECT report_name, report_type, status
FROM daily_report
ORDER BY report_name;
