package in.truethics.ethics.ethicsapiv10.repository.tranxs_repository.receipt_repository;

import in.truethics.ethics.ethicsapiv10.model.tranx.receipt.TranxReceiptPerticularsDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TranxReceiptPerticularsDetailsRepository extends JpaRepository<TranxReceiptPerticularsDetails, Long> {

    List<TranxReceiptPerticularsDetails> findByLedgerMasterIdAndOutletIdAndBranchIdAndStatus(Long voucherId, Long id, Long id1, boolean b);



    List<TranxReceiptPerticularsDetails> findByLedgerMasterIdAndOutletIdAndStatus(Long voucherId, Long id, boolean b);

    List<TranxReceiptPerticularsDetails> findByTranxReceiptMasterIdAndOutletIdAndBranchIdAndStatusAndTypeIgnoreCase(Long voucherId, Long id, Long id1, boolean b, String cr);

    List<TranxReceiptPerticularsDetails> findByTranxReceiptMasterIdAndOutletIdAndStatusAndTypeIgnoreCase(Long voucherId, Long id, boolean b, String cr);


    List<TranxReceiptPerticularsDetails> findByTranxReceiptMasterIdAndOutletIdAndBranchIdAndStatusOrderByTypeDesc(Long id, Long id1, Long id2, boolean b);

    List<TranxReceiptPerticularsDetails> findByTranxReceiptMasterIdAndOutletIdAndBranchIdAndStatus(Long id, Long id1, Long id2, boolean b);

    TranxReceiptPerticularsDetails findByIdAndOutletIdAndBranchIdAndStatus(Long tranx_type, Long id, Long id1, boolean b);

    TranxReceiptPerticularsDetails findByIdAndOutletIdAndStatus(Long tranx_type, Long id, boolean b);

    List<TranxReceiptPerticularsDetails> findByTranxReceiptMasterIdAndOutletIdAndStatus(Long transactionId, Long id, boolean b);



//    @Query(value="SELECT * FROM `tranx_receipt_perticulars_details_tbl` LEFT JOIN tranx_receipt_master_tbl " +
//            " ON tranx_receipt_perticulars_details_tbl.tranx_receipt_master_id=?1 " +
//            " WHERE  tranx_receipt_master_tbl.outlet_id=?2 AND  tranx_receipt_master_tbl.branch_id=?3" +
//            " AND  tranx_receipt_master_tbl.fiscal_year_id=?4 AND" +
//            "  tranx_receipt_perticulars_details_tbl.status=?5 AND tranx_receipt_perticulars_details_tbl.type=?6 ",nativeQuery=true)
    @Query(value="SELECT * FROM `tranx_receipt_perticulars_details_tbl` LEFT JOIN tranx_receipt_master_tbl " +
            " ON tranx_receipt_perticulars_details_tbl.tranx_receipt_master_id=?1 " +
            " WHERE  tranx_receipt_master_tbl.outlet_id=?2 AND  tranx_receipt_master_tbl.branch_id=?3" +
            "  AND " +
            "  tranx_receipt_perticulars_details_tbl.status=?4 AND tranx_receipt_perticulars_details_tbl.type=?5 ",nativeQuery=true)
    List<TranxReceiptPerticularsDetails> findByReceiptMasterIdAndIds(Long transactionId, Long id, Long id1, boolean b, String dr);
}
