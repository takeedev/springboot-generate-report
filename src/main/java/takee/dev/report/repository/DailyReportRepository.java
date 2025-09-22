package takee.dev.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import takee.dev.report.entity.DailyReport;

import java.util.UUID;

public interface DailyReportRepository extends JpaRepository<DailyReport, UUID> {
}
