package in.truethics.ethics.ethicsapiv10.repository.master_repository;

import in.truethics.ethics.ethicsapiv10.model.school_master.StudentTransport;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface StudentTransportRepository extends JpaRepository<StudentTransport, Long> {
    List<StudentTransport> findByOutletIdAndBranchIdAndAcademicYearIdAndStatus(Long id, Long id1,Long acdemicfiscalid, boolean b);

    StudentTransport findByIdAndStatus(Long studentTransportId, boolean b);

    @Query(value = "SELECT student_transport_tbl.*, academic_year_tbl.year, standard_tbl.standard_name," +
            " bus_tbl.bus_stop_name FROM `student_transport_tbl` LEFT JOIN student_admission_tbl ON" +
            " student_transport_tbl.student_id=student_admission_tbl.student_id LEFT JOIN academic_year_tbl ON" +
            " student_transport_tbl.academic_year_id=academic_year_tbl.id LEFT JOIN standard_tbl ON" +
            " student_admission_tbl.standard_id=standard_tbl.id LEFT JOIN bus_tbl ON" +
            " student_transport_tbl.bus_id=bus_tbl.id LEFT JOIN student_register_tbl ON student_transport_tbl.student_id=student_register_tbl.id" +
            " WHERE student_transport_tbl.outlet_id=?1 AND student_transport_tbl.branch_id=?2" +
            " AND student_transport_tbl.academic_year_id=?3 AND student_admission_tbl.standard_id=?4 AND student_transport_tbl.status=?5" +
            " ORDER BY TRIM(student_register_tbl.last_name) ASC", nativeQuery = true)
    List<StudentTransport> getDataByAcademicAndStandard(Long id, Long id1, String academicYearId, String standardId, boolean b);

    @Query(value = "SELECT student_transport_tbl.*, academic_year_tbl.year, standard_tbl.standard_name," +
            " bus_tbl.bus_stop_name FROM `student_transport_tbl` LEFT JOIN student_admission_tbl ON" +
            " student_transport_tbl.student_id=student_admission_tbl.student_id LEFT JOIN academic_year_tbl ON" +
            " student_transport_tbl.academic_year_id=academic_year_tbl.id LEFT JOIN standard_tbl ON" +
            " student_admission_tbl.standard_id=standard_tbl.id LEFT JOIN bus_tbl ON" +
            " student_transport_tbl.bus_id=bus_tbl.id LEFT JOIN student_register_tbl ON student_transport_tbl.student_id=student_register_tbl.id" +
            " WHERE student_transport_tbl.outlet_id=?1 AND student_transport_tbl.branch_id=?2" +
            " AND student_transport_tbl.academic_year_id=?3 AND student_admission_tbl.academic_year_id=?3 AND student_transport_tbl.status=?4 " +
            " ORDER BY TRIM(student_register_tbl.last_name) ASC", nativeQuery = true)
    List<StudentTransport> getDataByAcademic(Long id, Long id1, String academicYearId, boolean b);

    @Query(value = "SELECT student_transport_tbl.*, academic_year_tbl.year, standard_tbl.standard_name," +
            " bus_tbl.bus_stop_name FROM `student_transport_tbl` LEFT JOIN student_admission_tbl ON" +
            " student_transport_tbl.student_id=student_admission_tbl.student_id LEFT JOIN academic_year_tbl ON" +
            " student_transport_tbl.academic_year_id=academic_year_tbl.id LEFT JOIN standard_tbl ON" +
            " student_admission_tbl.standard_id=standard_tbl.id LEFT JOIN bus_tbl ON" +
            " student_transport_tbl.bus_id=bus_tbl.id LEFT JOIN student_register_tbl ON student_transport_tbl.student_id=student_register_tbl.id" +
            " WHERE student_transport_tbl.outlet_id=?1 AND student_transport_tbl.branch_id=?2" +
            " AND student_admission_tbl.standard_id=?3 AND student_transport_tbl.status=?4" +
            " ORDER BY TRIM(student_register_tbl.last_name) ASC", nativeQuery = true)
    List<StudentTransport> getDataByStandard(Long id, Long id1, String standardId, boolean b);

    List<StudentTransport> findByStudentRegisterIdAndStatus(Long id, boolean b);

    List<StudentTransport> findByOutletIdAndBranchIdAndStatus(Long id, Long id1, boolean b);

    StudentTransport findByStudentRegisterIdAndAcademicYearIdAndStatus(Long id, long academicYearId, boolean b);

    @Transactional
    @Modifying
    @Cascade(CascadeType.DELETE)
    @Query(value = "DELETE FROM `student_transport_tbl` WHERE student_id=?1 AND academic_year_id=?2", nativeQuery = true)
    void deleteTransportRecord(Long id, Long id1);

    @Transactional
    @Modifying
    @Cascade(CascadeType.DELETE)
    @Query(value = "DELETE FROM `student_transport_tbl` WHERE id=?1", nativeQuery = true)
    void deleteTransportRecordById(Long id);
}
