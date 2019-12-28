
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
import sec.project.domain.Signup;
import org.springframework.stereotype.Service;

@Service
public class SignupService  {
    
    public List<Signup> listAll() throws Exception {
        
        List<Signup> signups = new ArrayList<>();
        // Open connection
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:./database", "sa", "");

        // Execute query and retrieve the query results
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM Signup");

        // Do something with the results -- here, we print the books
        while (resultSet.next()) {
            //String id = resultSet.getString("id");
            String name = resultSet.getString("name");
            String address = resultSet.getString("address");

            signups.add(new Signup(name, address));
        }

        // Close the connection
        resultSet.close();
        connection.close();
        
        return signups;
    }
    
    public void addSignup(String name, String address) throws SQLException {
        Connection c = DriverManager.getConnection("jdbc:h2:mem:./database", "sa", "");

        // Perform input validation to detect attacks
        String query = "INSERT INTO Agent (id, name) VALUES (?, ?) ";
        PreparedStatement pstmt = c.prepareStatement( query );
        pstmt.setString( 1, name); 
        pstmt.setString( 2, address);
        pstmt.execute();
        
        c.close();
    }
}
