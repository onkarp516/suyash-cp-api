package in.truethics.ethics.ethicsapiv10.repository.history_repository;

import in.truethics.ethics.ethicsapiv10.model.history_table.TranxReceiptMasterHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptMstHistoryRepository extends JpaRepository<TranxReceiptMasterHistory, Long> {
}
