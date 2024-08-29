package in.truethics.ethics.ethicsapiv10.model.history_table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tranx_receipt_master_history_tbl")
public class TranxReceiptMasterHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long TranxReceiptMasterId;
    private Long  branchId;
    private Long outletId;
    private Long fiscalYearId;

    private double receiptSrNo;
    private LocalDate transactionDate;
    private double totalAmt;
    private boolean status;
    private String narrations;
    private String financialYear;
    private String feeReceiptNo;
    private String operationType;

    @CreationTimestamp
    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;

}

