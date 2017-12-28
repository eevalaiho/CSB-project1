package sec.project.domain;

import org.springframework.data.jpa.domain.AbstractPersistable;
import javax.persistence.Entity;

@Entity
public class Message extends AbstractPersistable<Long> {

    public String title;
    public String content;

    public Message() {
        super();
    }

    public Message(String title, String content) {
        this();
        this.title = title;
        this.content = content;
    }
}

