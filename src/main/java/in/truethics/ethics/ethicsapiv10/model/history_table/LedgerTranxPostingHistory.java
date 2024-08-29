package in.truethics.ethics.ethicsapiv10.model.history_table;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "ledger_tranx_postings_history_tbl")
public class LedgerTranxPostingHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long ledgerTranxPostingId;
    private Long ledgerMasterId;
    private Long transactionTypeId;
    private Long associateGroupsId;
    private Long fiscalYearId;
    private Long branchId;
    private Long outletId;

    private Double amount;
    private LocalDate transactionDate;
    private Long transactionId; // transaction id
    private String invoiceNo; //Invoice Number
    private String ledgerType;//CR or DR
    private String tranxType; //Purchase,Sales,Payment,Receipt,Purchase Return,Sales Return etc;
    private String operations;//insert,update,delete
    @CreationTimestamp
    private LocalDateTime createdAt;
    private Long createdBy;
    private Long updatedBy;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private Boolean status;
}
