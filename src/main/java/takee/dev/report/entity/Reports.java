package takee.dev.report.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import takee.dev.report.enums.ReportTypeEnum;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reports")
public class Reports {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, updatable = false, name = "report_no")
    private String reportNo;

    @Column(nullable = false, updatable = false, name = "report_name")
    private String reportName;

    @Column(nullable = false, updatable = false, name = "type_report")
    private ReportTypeEnum typeReport;

    @Column(nullable = false, updatable = false, name = "template")
    private String template;

    @Column(nullable = false, updatable = false, name = "path_out")
    private String pathOut;

    @Column(nullable = false, updatable = false, name = "is_active")
    private boolean isActive;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;

}
