package in.truethics.ethics.ethicsapiv10.model.master;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transaction_status_tbl")
public class TransactionStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String statusName;
    private Boolean status;

    /* @JsonBackReference
     @OneToMany(fetch = FetchType.LAZY,
             cascade = CascadeType.ALL)
     private List<TranxPurOrder> tranxPurOrders;

     @JsonBackReference
     @OneToMany(fetch = FetchType.LAZY,
             cascade = CascadeType.ALL)
     private List<TranxPurChallan> tranxPurChallans;

     @JsonBackReference
     @OneToMany(fetch = FetchType.LAZY,
             cascade = CascadeType.ALL)
     private List<TranxSalesQuotation> tranxSalesQuotations;

     @JsonBackReference
     @OneToMany(fetch = FetchType.LAZY,
             cascade = CascadeType.ALL)
     private List<TranxSalesOrder> tranxSalesOrders;

     @JsonBackReference
     @OneToMany(fetch = FetchType.LAZY,
             cascade = CascadeType.ALL)
     private List<TranxSalesChallan> tranxSalesChallans;

     @JsonBackReference
     @OneToMany(fetch = FetchType.LAZY,
             cascade = CascadeType.ALL)
     private List<TranxDebitNoteNewReferenceMaster> tranxDebitNoteNewReferences;

     @JsonBackReference
     @OneToMany(fetch = FetchType.LAZY,
             cascade = CascadeType.ALL)
     private List<TranxDebitNoteDetails> tranxDebitNoteDetails;

 */
    private Long createdBy;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private Long updatedBy;


}
