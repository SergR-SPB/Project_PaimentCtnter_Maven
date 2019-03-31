package JDBC_task.task2.Part2;
/*Написать простое консольное приложение для работы с базой данных.

  task2
  Модуль  Orders (id, title)

 */

import java.sql.*;
import java.util.Scanner;

public class OrdersDB {

    private static final String DB_CONNECTION = "jdbc:postgresql://localhost:5432/Clients";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres";


    private static Connection connection;
    private static Scanner scanner;

    public static void main(String[] args) {

        scanner = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection
                (DB_CONNECTION, DB_USER, DB_PASSWORD)) {
            connection = conn;
            unitDBOrders();

            while (true) {
                showMenuOrders();

                String s = scanner.nextLine();
                switch (s) {
                    case "1":
                        addOrders();
                        break;
                    case "2":
                        insertAutoOrders();
                        break;
                    case "3":
                        deleteOrders();
                        break;
                    case "4":
                        viewOrders();
                        break;
                    case "5":
                        viewOrdersByTitle();
                        break;
                        default:
                            return;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addOrders() throws SQLException {
        System.out.println("Enter order title: ");
        String title = scanner.nextLine();

        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO Orders(title) VALUES (?)")) {
            ps.setString(1, title);
            ps.executeUpdate();
        }
    }

    private static void insertAutoOrders() throws SQLException {
        System.out.println("Enter orders count: ");
        String sCount = scanner.nextLine();
        int count = Integer.parseInt(sCount);

        connection.setAutoCommit(false);
        try {
            try (PreparedStatement ps = connection.prepareStatement("INSERT INTO Orders (title) VALUES (?)")) {

                for (int i = 0; i <= count; i++) {
                    ps.setString(1, "Title" + (i+1));
                    ps.executeUpdate();
                }
                connection.commit();
            } catch (Exception ex) {
                connection.rollback();
            }
        } finally {
            connection.setAutoCommit(true);
        }
    }

    private static void deleteOrders() throws SQLException {
        System.out.println("Enter orders id: ");
        String sId = scanner.nextLine();
        int id = Integer.parseInt(sId);

        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM Orders WHERE id=" + id)) {
            ps.executeUpdate();
        }
    }

    private static void viewOrders() throws SQLException {
        try(PreparedStatement ps = connection.prepareStatement("SELECT *FROM Orders")){
            try(ResultSet rs = ps.executeQuery()){
                printResultSet(rs);
            }
        }
    }



    private static void viewOrdersByTitle() throws SQLException {
        System.out.println("Enter part of title: ");
        String titilePart = scanner.nextLine();
        try(PreparedStatement ps = connection.prepareStatement("SELECT *FROM Orders WHERE title like CONCAT " +
                "('%',?,'%')")){
            ps.setString(1,titilePart);
            try(ResultSet rs = ps.executeQuery()){
                printResultSet(rs);
            }
        }

    }

    private static void showMenuOrders() {
        System.out.println("1. add orders");
        System.out.println("2. add random orders");
        System.out.println("3. delete orders");
        System.out.println("4. view orders");
        System.out.println("5. view orders by part of title");
    }

    private static void unitDBOrders() throws SQLException {

        Statement st = connection.createStatement();
        try {
            st.execute("DROP TABLE IF EXISTS Orders");
            st.execute("CREATE TABLE Orders(" +
                    "id SERIAL NOT NULL PRIMARY KEY," +
                    "title VARCHAR (50) NOT NULL )");
        } finally {
            st.close();
        }
    }

    private static void printResultSet(ResultSet rs) throws SQLException{
        ResultSetMetaData md = rs.getMetaData();

        for (int i=1; i<=md.getColumnCount(); i++){
            System.out.print("\t"+md.getColumnName(i)+"\t\t");
        }
        System.out.println();
        while (rs.next()){
            for (int i=1; i<=md.getColumnCount(); i++){
                System.out.print("\t"+rs.getString(i)+"\t\t");
            }
            System.out.println();

        }

    }


}