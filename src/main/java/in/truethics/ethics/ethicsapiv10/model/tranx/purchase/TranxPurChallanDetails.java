package in.truethics.ethics.ethicsapiv10.model.tranx.purchase;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import in.truethics.ethics.ethicsapiv10.model.inventory.PackingMaster;
import in.truethics.ethics.ethicsapiv10.model.inventory.Product;
import in.truethics.ethics.ethicsapiv10.model.master.TransactionTypeMaster;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Data
@Entity
@Table(name = "tranx_purchase_challan_details_tbl")
public class TranxPurChallanDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tranx_pur_challan_id")
    @JsonManagedReference
    private TranxPurChallan tranxPurChallan;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonManagedReference
    private Product product;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "transaction_type_id")
    @JsonManagedReference
    private TransactionTypeMaster transactionType;

    @ManyToOne
    @JoinColumn(name = "packaging_id")
    @JsonManagedReference
    private PackingMaster packingMaster;

    @JsonBackReference
    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<TranxPurChallanDetailsUnits> tranxPurChallanDetailsUnits;

    @JsonBackReference
    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<TranxPurchaseChallanProductSrNumber> tranxPurchaseChallanProductSrNumbers;

    //private Double qty;
    private Double base_amt;
    //private String referenceType;
    private String orderReference;
    private Boolean isPurchaseOrder;
    private String PurchaseOrderId;
    //private Double rate;
    private Double totalAmount;
    private Double discountAmount;
    private Double discountPer;
    private Double discountAmountCal;
    private Double discountPerCal;
    private Double igst;
    private Double sgst;
    private Double cgst;
    private Double totalIgst;
    private Double totalSgst;
    private Double totalCgst;
    private Double finalAmount;
    private Double qtyHigh;
    private Double rateHigh;
    private Double qtyMedium;
    private Double rateMedium;
    private Double qtyLow;
    private Double rateLow;
    private Double baseAmtHigh;
    private Double baseAmtLow;
    private Double baseAmtMedium;
    private Boolean status;
    private String operations;
    private Long createdBy;
    @CreationTimestamp
    private LocalDateTime createdAt;
    private Long updatedBy;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
