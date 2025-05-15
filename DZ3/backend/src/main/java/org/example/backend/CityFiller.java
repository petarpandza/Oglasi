package org.example.backend;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class CityFiller {
    public static void main(String[] args) throws SQLException, IOException {
        Properties login = new Properties();
        try (FileReader in = new FileReader("env.properties")) {
            login.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Connection con = connectToDatabase(login.getProperty("DB_USER"), login.getProperty("DB_PASSWORD"));
        System.out.println("Opened database successfully");

        fillDB(con);

        con.close();
    }

    private static Connection connectToDatabase(String username, String password) {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/oglasi-db",
                            username, password);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return null;
    }
    private static void fillDB(Connection c) throws IOException, SQLException {

        String statement = "insert into core.city (name) VALUES (?)";
        PreparedStatement preparedStatement = c.prepareStatement(statement);

        BufferedReader reader = new BufferedReader(new FileReader("cities.txt"));
        String city = reader.readLine();
        while (city != null) {
            preparedStatement.setString(1, city);
            preparedStatement.executeUpdate();
            city = reader.readLine();
        }
        preparedStatement.close();
    }
}
