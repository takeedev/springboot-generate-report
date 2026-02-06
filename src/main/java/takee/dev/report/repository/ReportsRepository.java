package takee.dev.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import takee.dev.report.entity.Reports;

import java.util.UUID;

@Repository
public interface ReportsRepository extends JpaRepository<Reports, UUID> {

    @Query(value = """
            SELECT * FROM REPORTS
            WHERE report_no = :reportNo
            """, nativeQuery = true)
    Reports findByReportNo(@Param("reportNo") String reportNo);

}
