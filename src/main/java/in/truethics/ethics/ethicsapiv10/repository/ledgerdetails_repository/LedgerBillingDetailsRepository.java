package in.truethics.ethics.ethicsapiv10.repository.ledgerdetails_repository;

import in.truethics.ethics.ethicsapiv10.model.master.LedgerBillingDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LedgerBillingDetailsRepository extends JpaRepository<LedgerBillingDetails, Long> {
    List<LedgerBillingDetails> findByLedgerMasterIdAndStatus(Long ledgerId, boolean b);

    LedgerBillingDetails findByIdAndStatus(long id, boolean b);
}
