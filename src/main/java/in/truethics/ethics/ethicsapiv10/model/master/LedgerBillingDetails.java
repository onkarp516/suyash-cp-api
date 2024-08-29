package in.truethics.ethics.ethicsapiv10.model.master;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ledger_billing_address_tbl")
public class LedgerBillingDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String district;
    private String billingAddress;
    private Long createdBy;
    @CreationTimestamp
    private LocalDateTime createdAt;
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "ledger_id")
    @JsonManagedReference
    private LedgerMaster ledgerMaster;
}
