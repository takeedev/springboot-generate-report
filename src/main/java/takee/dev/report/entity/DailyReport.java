package takee.dev.report.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import takee.dev.report.enums.ReportTypeEnum;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "daily_report")
public class DailyReport {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, updatable = false, name = "report_name")
    private String reporname;

    @Column(nullable = false, updatable = false, name = "report_type")
    private ReportTypeEnum reportType;

    @Column(nullable = false, updatable = false, name = "status")
    private String status;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
    
    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;
    
}
