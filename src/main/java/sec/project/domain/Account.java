
package sec.project.domain;

import javax.persistence.Entity;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class Account extends AbstractPersistable<Long>{
    
    @NotEmpty
    private String username;
    
    @NotEmpty
    @Size(min = 2)
    private String password;

    public Account() {
        super();
    }

    public Account(String username, String password) {
        this();
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
    
    @Override
    public String toString() {
        return this.username;
    }

}
