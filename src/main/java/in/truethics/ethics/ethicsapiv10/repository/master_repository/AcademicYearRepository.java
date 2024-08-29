package in.truethics.ethics.ethicsapiv10.repository.master_repository;

import in.truethics.ethics.ethicsapiv10.model.school_master.AcademicYear;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AcademicYearRepository extends JpaRepository<AcademicYear, Long> {
    List<AcademicYear> findByOutletIdAndStatus(Long id, boolean b);

    AcademicYear findByIdAndStatus(long id, boolean b);

    List<AcademicYear> findByBranchIdAndStatus(Long branchId, boolean b);

    List<AcademicYear> findByOutletIdAndBranchIdAndStatus(Long id, Long id1, boolean b);



    AcademicYear findByBranchIdAndFiscalYearIdAndStatus(Long id, Long id1, boolean b);

    AcademicYear findByBranchIdAndYearAndStatus(Long id, String rightOffacademicYear, boolean b);
}
