package JDBC_task.task2.DB_Operation;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class AddClient {
    private static Scanner sc;

    public static void addClient() throws SQLException {
        sc = new Scanner(System.in);
        System.out.println("Enter client name: ");
        String name = sc.nextLine();
        System.out.println("Enter client age: ");
        String sAge = sc.nextLine();
        int age = Integer.parseInt(sAge);

        try (PreparedStatement ps = EntriDB.connection.prepareStatement("INSERT INTO " +
                "Clients (name,age) VALUES (?,?)")) {
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.executeUpdate();  // ------- for INSERT, UPDATE & DELETE
        }
    }
}
