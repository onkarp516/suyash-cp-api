package in.truethics.ethics.ethicsapiv10.repository.access_permission_repository;

import in.truethics.ethics.ethicsapiv10.model.access_permissions.SystemAccessPermissions;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface SystemAccessPermissionsRepository extends JpaRepository<SystemAccessPermissions, Long> {

    List<SystemAccessPermissions> findByUsersIdAndStatus(Long id, boolean b);


    @Transactional
    @Modifying
    @Cascade(CascadeType.DELETE)
    @Query(value = "DELETE FROM `access_permissions_tbl` WHERE users_id=?1", nativeQuery = true)
    void deleteOldPermissions(Long id);
}
