#!/usr/bin/env bash
set -euo pipefail

SQLCMD=/opt/mssql-tools/bin/sqlcmd
SQLCMD_EXTRA_ARGS=

if [ -x /opt/mssql-tools18/bin/sqlcmd ]; then
  SQLCMD=/opt/mssql-tools18/bin/sqlcmd
  SQLCMD_EXTRA_ARGS=-C
fi

DB_HOST="${DB_HOST:-sqlserver}"
DB_NAME="${DB_NAME:-reports}"
DB_USERNAME="${DB_USERNAME:-sa}"
DB_PASSWORD="${DB_PASSWORD:-${MSSQL_SA_PASSWORD:-P@ssw0rd12345}}"
SEED_FILE="${SEED_FILE:-/scripts/seed-data.sql}"

until "$SQLCMD" -S "$DB_HOST" -U "$DB_USERNAME" -P "$DB_PASSWORD" -d "$DB_NAME" -Q "SELECT 1" -b $SQLCMD_EXTRA_ARGS; do
  sleep 2
done

until "$SQLCMD" -S "$DB_HOST" -U "$DB_USERNAME" -P "$DB_PASSWORD" -d "$DB_NAME" -Q "SET NOCOUNT ON; IF (SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME IN ('reports', 'daily_report')) = 2 SELECT 1 ELSE SELECT 0" -h -1 -W -b $SQLCMD_EXTRA_ARGS | grep -q "^1$"; do
  sleep 2
done

"$SQLCMD" -S "$DB_HOST" -U "$DB_USERNAME" -P "$DB_PASSWORD" -d "$DB_NAME" -i "$SEED_FILE" -b $SQLCMD_EXTRA_ARGS
