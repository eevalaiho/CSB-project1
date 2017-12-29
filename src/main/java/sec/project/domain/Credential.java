package sec.project.domain;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;

@Entity
public class Credential extends AbstractPersistable<Long> {

    public String username;
    public String password;

    public Credential() {
        super();
    }

    public Credential(String username, String password) {
        this();
        this.username = username;
        this.password = password;
    }
}

