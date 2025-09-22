package takee.dev.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import takee.dev.report.entity.Reports;

import java.util.UUID;

public interface ReportsRepository extends JpaRepository<Reports, UUID> {
}
