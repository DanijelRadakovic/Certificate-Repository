package megatravel.com.cerrepo.repository;

import megatravel.com.cerrepo.domain.rbac.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
}
