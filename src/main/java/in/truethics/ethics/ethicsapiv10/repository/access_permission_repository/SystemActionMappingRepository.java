package in.truethics.ethics.ethicsapiv10.repository.access_permission_repository;

import in.truethics.ethics.ethicsapiv10.model.access_permissions.SystemActionMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SystemActionMappingRepository extends JpaRepository<SystemActionMapping, Long> {

    List<SystemActionMapping> findByStatus(boolean b);

    SystemActionMapping findByIdAndStatus(long mapping_id, boolean b);
}
