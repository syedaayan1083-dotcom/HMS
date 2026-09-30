package database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    public static Connection connect() {

        Connection connection = null;

        try {

            // DATABASE URL
            String url =
                    "jdbc:postgresql://localhost:5432/healthcare_system";

            // USERNAME
            String user = "postgres";

            // PASSWORD
            String password = "PASSWORD";

            connection = DriverManager.getConnection(
                    url,
                    user,
                    password
            );

            System.out.println(
                    "Database Connected Successfully!"
            );

        }
        catch(Exception e) {

            e.printStackTrace();
        }

        return connection;
    }
}
