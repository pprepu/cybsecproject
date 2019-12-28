package sec.project.domain;

import javax.persistence.Entity;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class EventFood extends AbstractPersistable<Long>{
    
    private String name;

    public EventFood() {
        super();
    }

    public EventFood(String name) {
        this();
        this.name = name;
    }

    public String Name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
