package JDBC_task.task2.Part2;
/*Написать простое консольное приложение для работы с базой данных.

Модуль  2)
 Details (id, client_id, address)

 */

import java.sql.*;
import java.util.Scanner;

public class DetailsDB {

    private static final String DB_CONNECTION = "jdbc:postgresql://localhost:5432/Clients";
    private static final String DB_USER = "postgres";
    private static final String DB_Pasword = "postgres";

    private static Connection connection;
    private static Scanner scanner;

    public static void main(String[] args) {

        scanner = new Scanner(System.in);

        try (Connection con = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_Pasword)) {
            connection = con;
            unitDetailsDB();

            while (true) {
                showMenuDetails();
                String s = scanner.nextLine();
                switch (s) {
                    case "1":
                        addDetails();
                        break;
                    case "2":
                        deleteDetails();
                        break;
                    case "3":
                        viewDetails();
                        break;
                    case "4":
                        viewDetailsByIdClient();
                        break;
                    default:
                        return;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addDetails() throws SQLException {
        System.out.println("Enter id client: ");
        String sIdClient = scanner.nextLine();
        int idClient = Integer.parseInt(sIdClient);

        System.out.println("Enter address client: ");
        String address = scanner.nextLine();

        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO Details(idClient,address)" +
                        "VALUES (?,?)")) {
            ps.setInt(1, idClient);
            ps.setString(2, address);
            ps.executeUpdate();
        }
    }

    private static void deleteDetails() throws SQLException {
        System.out.println("Enter id client for delete details: ");
        String sIdClient = scanner.nextLine();
        int idClient = Integer.parseInt(sIdClient);

        try (PreparedStatement ps = connection.prepareStatement("" +
                "DELETE  FROM Details WHERE idClient =" + idClient)) {
            ps.executeUpdate();
        }
    }

    private static void viewDetails() throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT *FROM Details")) {
            try (ResultSet rs = ps.executeQuery()) {
                printResultSet(rs);
            }
        }
    }
    private static void viewDetailsByIdClient() {

    }


    private static void printResultSet(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();

        for (int i = 1; i <= md.getColumnCount(); i++) {
            System.out.print(md.getColumnName(i) + "\t\t");
        }
        System.out.println();

        while (rs.next()) {
            for (int i = 1; i <= md.getColumnCount(); i++) {
                System.out.print(rs.getString(i) + "\t\t");
            }
            System.out.println();
        }
    }



    private static void showMenuDetails() {
        System.out.println("1. add details by id client: ");
        System.out.println("2. delete details by id client: ");
        System.out.println("3. view details clients: ");
        System.out.println("4. view details by id client: ");
    }

    private static void unitDetailsDB() throws SQLException {
        Statement st = connection.createStatement();
        try {
            st.execute("DROP TABLE IF EXISTS Details");
            st.execute("CREATE TABLE Details(id SERIAL NOT NULL PRIMARY KEY," +
                    "idClient INT NOT NULL, " +
                    "address VARCHAR (50) NOT NULL )");
        } finally {
            st.close();
        }
    }


}