# Spring Boot Generate Report

Learning project for generating and downloading report files with Spring Boot, Apache POI, CSV, and text export.

## Requirements

- Java 21
- Maven Wrapper
- SQL Server for local runtime

## Configuration

Runtime database settings are read from environment variables.

```bash
export DB_URL='jdbc:sqlserver://localhost:1499;databaseName=reports;TrustServerCertificate=True'
export DB_USERNAME='sa'
export DB_PASSWORD='your-password'
export JPA_DDL_AUTO='update'
```

`JPA_DDL_AUTO` defaults to `none` so the application does not change schema automatically unless local development explicitly enables it.

## Run

```bash
sh mvnw spring-boot:run
```

Swagger UI:

- `http://localhost:8080/swagger`
- `http://localhost:8080/swagger-ui/index.html`

OpenAPI JSON:

- `http://localhost:8080/api-doc`

## Test

```bash
sh mvnw test
```

Tests use H2 in-memory database from `src/test/resources/application.yml`.

## Main Endpoints

- `POST /api/manage/report/add-report`
- `GET /api/manage/report/get-report`
- `POST /api/manage/report/add-report-daily`
- `GET /api/manage/report/get-daily-report`
- `GET /api/download-file/gen-normal-report-excel?reportNo=001`
- `GET /api/download-file/gen-large-report-excel?reportNo=001`
- `GET /api/download-file/gen-report-csv?reportNo=001`
- `GET /api/download-file/gen-report-text?reportNo=001`
- `GET /api/download-file/gen-report-csv-disk?reportNo=001`
- `GET /api/download-file/gen-report-text-disk?reportNo=001`

## Excel Export Notes

`XSSFWorkbook` keeps rows and cells in memory. Use it for small to medium files, especially when `autoSizeColumn` or editing existing cells is needed.

`SXSSFWorkbook` is the streaming writer for large `.xlsx` files. This project writes large Excel exports directly to a temp file, then streams the file to the HTTP response and cleans up the temp file after streaming.

CSV/text exports escape delimiter, quote, and newline characters so generated files remain parseable.
