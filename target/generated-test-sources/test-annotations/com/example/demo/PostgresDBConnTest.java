package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

class PostgresDBConnTest {

	@Test
	void test() {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
 
    
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "284823";
 
        try {
            con = DriverManager.getConnection(url, user, password);
            st = con.createStatement();
            rs = st.executeQuery("SELECT VERSION()");
 
            if (rs.next()) {
                System.out.println(rs.getString(1));
            }
 
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(PostgresDBConnTest.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
 
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }
 
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(PostgresDBConnTest.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        } 


	}
	

}
