package megatravel.com.cerrepo.repository;

import megatravel.com.cerrepo.domain.rbac.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findRoleByName(String name);

}
