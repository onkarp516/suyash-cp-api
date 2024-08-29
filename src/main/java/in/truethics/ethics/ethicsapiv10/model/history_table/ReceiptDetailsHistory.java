package in.truethics.ethics.ethicsapiv10.model.history_table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tranx_receipt_details_history_tbl")
public class ReceiptDetailsHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tranxReceiptDetailsId;
    private Long  branchId;
    private Long outletId;
    private Long ledgerMasterId;
    private Long receiptMasterId;

    private String type; //Cr or Dr
    private String ledgerType;
    private Double paidAmount;
    private String operationType;

    @CreationTimestamp
    private LocalDateTime createdAt;
    private Long createdBy;
    private Boolean status;
}
