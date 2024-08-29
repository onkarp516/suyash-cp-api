package in.truethics.ethics.ethicsapiv10.repository.access_permission_repository;

import in.truethics.ethics.ethicsapiv10.model.access_permissions.SystemMasterModules;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SystemMasterModuleRepository extends JpaRepository<SystemMasterModules, Long> {
    List<SystemMasterModules> findByStatus(boolean b);

    SystemMasterModules findByIdAndStatus(Long parentModuleId, boolean b);
}
