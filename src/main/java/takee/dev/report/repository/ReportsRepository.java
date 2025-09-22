package takee.dev.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import takee.dev.report.entity.Reports;

import java.util.UUID;

@Repository
public interface ReportsRepository extends JpaRepository<Reports, UUID> {
}
