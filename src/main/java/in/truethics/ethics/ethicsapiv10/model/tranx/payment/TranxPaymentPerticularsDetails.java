package in.truethics.ethics.ethicsapiv10.model.tranx.payment;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import in.truethics.ethics.ethicsapiv10.model.master.Branch;
import in.truethics.ethics.ethicsapiv10.model.master.LedgerMaster;
import in.truethics.ethics.ethicsapiv10.model.master.Outlet;
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
@Table(name = "tranx_payment_perticulars_Details_tbl")
public class TranxPaymentPerticularsDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    @JsonManagedReference
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "outlet_id")
    @JsonManagedReference
    private Outlet outlet;

    @ManyToOne
    @JoinColumn(name = "ledger_id")
    @JsonManagedReference
    private LedgerMaster ledgerMaster;


    @ManyToOne
    @JoinColumn(name = "tranx_payment_master_id")
    @JsonManagedReference
    private TranxPaymentMaster tranxPaymentMaster;

    @ManyToOne
    @JoinColumn(name = "tranx_payment_perticulars_id")
    @JsonManagedReference
    private TranxPaymentPerticulars tranxPaymentPerticulars;

    private Long tranxInvoiceId;
    private String type;
    private Double paidAmt;
    private LocalDate transactionDate;
    private String tranxNo;
    private Boolean status;
    private Double totalAmt;

    @CreationTimestamp
    private LocalDateTime createdAt;
    private Long createdBy;
}
