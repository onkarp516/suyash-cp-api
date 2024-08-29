package in.truethics.ethics.ethicsapiv10.repository.tranxs_repository.journal_repository;

import in.truethics.ethics.ethicsapiv10.model.tranx.journal.TranxJournalDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TranxJournalDetailsRepository extends JpaRepository<TranxJournalDetails, Long> {
    List<TranxJournalDetails> findByLedgerMasterIdAndOutletIdAndBranchId(Long sdid, Long id, Long id1);

    List<TranxJournalDetails> findByLedgerMasterIdAndOutletId(Long sdid, Long id);


//    TranxJournalMaster findByIdAndStatus(Long transactionId, boolean b);

    List<TranxJournalDetails> findByTranxJournalMasterIdAndOutletIdAndBranchIdAndStatus(Long voucherId, Long id, Long id1, boolean b);

    List<TranxJournalDetails> findByTranxJournalMasterIdAndOutletIdAndBranchIdAndStatusAndTypeIgnoreCase(Long id, Long id1, Long id2, boolean b, String dr);

    List<TranxJournalDetails> findByTranxJournalMasterIdAndOutletIdAndStatus(Long voucherId, Long id, boolean b);

    List<TranxJournalDetails> findByTranxJournalMasterIdAndOutletIdAndStatusAndTypeIgnoreCase(Long voucherId, Long id, boolean b, String dr);

    List<TranxJournalDetails> findByTranxJournalMasterIdAndOutletIdAndBranchIdAndStatusOrderByTypeDesc(Long id, Long id1, Long id2, boolean b);

    TranxJournalDetails findByIdAndStatus(Long transactionId, boolean b);

    List<TranxJournalDetails> findByTranxJournalMasterIdAndStatus(Long id, boolean b);

    TranxJournalDetails findByIdAndOutletIdAndBranchIdAndStatus(Long tranx_type, Long id, Long id1, boolean b);

    TranxJournalDetails findByIdAndOutletIdAndStatus(Long tranx_type, Long id, boolean b);


           @Query(value="SELECT * FROM `tranx_journal_details_tbl` LEFT JOIN tranx_journal_master_tbl " +
                    "ON tranx_journal_details_tbl.tranx_journal_master_id=?1" +
                    " WHERE tranx_journal_details_tbl.outlet_id=?2 AND tranx_journal_details_tbl.branch_id=?3 and " +
                    " tranx_journal_details_tbl.status=?5 AND tranx_journal_details_tbl.type=?6 AND  tranx_journal_master_tbl.fiscal_year_id=?4",nativeQuery=true)
    List<TranxJournalDetails> findByFiscalYearAndJournalMasterIdAndStatus(Long transactionId, Long id, Long id1, Long id2, boolean b, String dr);
}
