
package sec.project.service;

import java.util.List;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import sec.project.domain.Signup;
import org.springframework.stereotype.Service;
import sec.project.repository.SignupRepository;

@Service
public class SignupService  {
    
    @Autowired
    SignupRepository signupRepository;
    
    @PersistenceContext
    private EntityManager em;
    
    public List<Signup> listSignups() {
        List<Signup> results = em.createQuery("SELECT s FROM Signup s ").getResultList();
        return results;
    }
    
    @Transactional
    public void addSignup(String name, String address) {
        List<Signup> signups = listSignups();
        long id = signups.size() + 1;
        em.createNativeQuery("INSERT INTO Signup (id, name, address) VALUES ("+ id + ", '" + name + "', '" + address + "');").executeUpdate();
    }
    /* one solution
    @Transactional
    public void addSignup(String name, String address) {
        List<Signup> signups = listSignups();
        long id = signups.size() + 1;
        Query query = em.createNativeQuery("INSERT INTO Signup (id, name, address) VALUES (?, ?, ?)");
        query.setParameter(1, id);
        query.setParameter(2, name);
        query.setParameter(3, address);
        query.executeUpdate();
    */
}
