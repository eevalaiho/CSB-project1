package sec.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sec.project.domain.Message;
import sec.project.domain.Signup;

public interface MessageRepository extends JpaRepository<Message, Long> {

}
