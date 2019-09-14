package megatravel.com.cerrepo.repository;

import megatravel.com.cerrepo.domain.rbac.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    User findUserByUsername(String username);

}
