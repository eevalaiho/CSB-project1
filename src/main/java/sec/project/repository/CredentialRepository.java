package sec.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sec.project.domain.Credential;

public interface CredentialRepository extends JpaRepository<Credential, Long> {

}
