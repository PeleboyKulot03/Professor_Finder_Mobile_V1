package com.example.professorfinder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlDb {
    public static Connection connection() {
        System.out.println("hi");
        try {
            return DriverManager.getConnection("jdbc:mysql://aws.connect.psdb.cloud/professorfinder", "jemivdj53akpu6jqntjz", "pscale_pw_d3cU2lZ5EJPDYt0yUSA2nqQZ6B47zyyPCQvhVJOnJ0v");
        } catch (SQLException e) {
            return null;
        }
    }
}
