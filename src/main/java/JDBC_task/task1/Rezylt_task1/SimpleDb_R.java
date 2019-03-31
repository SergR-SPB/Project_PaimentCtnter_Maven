package JDBC_task.task1.Rezylt_task1;


    /* Написать простое консольное приложение для работы с базой данных.
В базе должна сохраняться информация о клиентах некоторой фирмы.
Для каждого клиента необходимо хранить его имя и возраст.
Для соединения с БД использовать JDBC (см Хорстман, том 2, глава 4)
Основные операции приложения:
 1) добавить нового клиента;
 2) изменить данные клиента с заданным id
 3) удалить клиента с заданным id
 4) вывести список всех клиентов
 5) вывести всех клиентов, старше определенного возраста (использовать statement)
 6) найти всех клиентов, имя которых содержит заданную посдтроку (использовать preparedStatement)
Дополнить предыдущую задачу двумя таблицами:
 1) Orders (id, title)
 2) Details (id, client_id, address)
..................
 */

import java.sql.*;
import java.util.Random;
import java.util.Scanner;

public class SimpleDb_R {

    private static final String DB_CONNECTION = "jdbc:postgresql://localhost:5432/Clients";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres";

    private static Connection connection;
    private static Scanner sc;

    public static void main(String[] args) {

        sc = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection
                (DB_CONNECTION, DB_USER, DB_PASSWORD)) {
            connection = conn;
            unitClientsDB(); // метод  - создает таблицу Clients
            unitOrdersDB(); // таблица Orders
            unitDetailsDB();// таблица Details

            while (true) {
                showMenuClients();
                showMenuOrders();// метод - создание мню в консоли
                showMenuDetails();
                String s = sc.nextLine();
                switch (s) {
                    case "1.1.":
                        addClient();
                        break;
                    case "1.2.":
                        insertRandomClient();
                        break;
                    case "1.3.":
                        deleteClient();
                        break;
                    case "1.4.":
                        changeClient();
                        break;
                    case "1.5.":
                        viewClients();
                        break;
                    case "1.6.":
                        viewClientsByAge();
                        break;
                    case "1.7.":
                        viewClientsByName();
                        break;
                    case "2.1.":
                        addOrders();
                        break;
                    case "2.2.":
                        insertAutoOrders();
                        break;
                    case "2.3.":
                        deleteOrders();
                        break;
                    case "2.4.":
                        viewOrders();
                        break;
                    case "2.5.":
                        viewOrdersByTitle();
                        break;
                    case "3.1.":
                        addDetails();
                        break;
                    case "3.2.":
                        deleteDetails();
                        break;
                    case "3.3.":
                        viewDetails();
                        break;
                    case "3.4.":
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


    private static void unitClientsDB() throws SQLException {
        Statement st = connection.createStatement();
        try {
            st.execute("DROP TABLE IF EXISTS Clients");

            st.execute("CREATE TABLE Clients (" +
                    "id SERIAL NOT NULL PRIMARY KEY, " +
                    "name VARCHAR(20) NOT NULL, age INT)");
        } finally {
            st.close();
        }
    }

    private static void unitOrdersDB() throws SQLException {
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

    private static void showMenuClients() {
        System.out.println("1.1. add client");
        System.out.println("1.2. add random client");
        System.out.println("1.3. delete client");
        System.out.println("1.4. change client");
        System.out.println("1.5. view clients");
        System.out.println("1.6. view clients by age");
        System.out.println("1.7. view clients by part of name");
        System.out.println("------------------------------------ ");
    }
    private static void showMenuOrders() {
        System.out.println("2.1. add orders");
        System.out.println("2.2. add random orders");
        System.out.println("2.3. delete orders");
        System.out.println("2.4. view orders");
        System.out.println("2.5. view orders by part of title");
        System.out.println("------------------------------------ ");
    }
    private static void showMenuDetails() {
        System.out.println("3.1. add details by id client: ");
        System.out.println("3.2. delete details by id client: ");
        System.out.println("3.3. view details clients: ");
        System.out.println("3.4. view details by id client: ");
    }

    private static void addClient() throws SQLException {
        System.out.println("Enter client name: ");
        String name = sc.nextLine();
        System.out.println("Enter client age: ");
        String sAge = sc.nextLine();
        int age = Integer.parseInt(sAge);

        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO " +
                "Clients (name,age) VALUES (?,?)")) {
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.executeUpdate();  // ------- for INSERT, UPDATE & DELETE
        }
    }

    private static void insertRandomClient() throws SQLException {
        System.out.println("Enter clients count: ");
        String sCount = sc.nextLine();
        int count = Integer.parseInt(sCount);
        Random rnd = new Random();

        connection.setAutoCommit(false);// ------------enable transactions
        try {
            try (PreparedStatement ps = connection.prepareStatement("INSERT INTO Clients (name, age) VALUES (?,?)")) {
                for (int i = 0; i <= count; i++) {
                    ps.setString(1, "Name" + i);
                    ps.setInt(2, rnd.nextInt(50));
                    ps.executeUpdate();
                }
                connection.commit();
            } catch (Exception ex) {
                connection.rollback();
            }
        } finally {
            connection.setAutoCommit(true); // ---------return to default mode
        }
    }

    private static void deleteClient() throws SQLException {
        System.out.println("Enter client ID: ");
        String sId = sc.nextLine();
        int id = Integer.parseInt(sId);

        try (PreparedStatement ps = connection.prepareStatement
                ("DELETE FROM Clients WHERE id= " + id)) {
            ps.executeUpdate();
        }
    }

    private static void changeClient() throws SQLException {
        System.out.println("Enter clients name: ");
        String name = sc.nextLine();

        System.out.println("Enter new age: ");
        String sAge = sc.nextLine();
        int age = Integer.parseInt(sAge);

        try (PreparedStatement ps = connection.prepareStatement
                ("UPDATE Cliens SET age = ? WHERE  name=?")) {
            ps.setInt(1, age);
            ps.setString(2, name);
            ps.executeUpdate();
        }

    }

    private static void viewClients() throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM Clients")) {
            try (ResultSet rs = ps.executeQuery()) {
                printResultSet(rs);
            }
        }
    }


    private static void viewClientsByAge() throws SQLException {
        System.out.println("Enter min age: ");
        String sAge = sc.nextLine();
        int age = Integer.parseInt(sAge);

        try (Statement s = connection.createStatement()) {
            try (ResultSet rs = s.executeQuery("SELECT *FROM Clients WHERE age>" + age)) {
                printResultSet(rs);
            }
        }
    }

    private static void viewClientsByName() throws SQLException {
        System.out.println("Enter part of name: ");
        String namePart = sc.nextLine();
        try (PreparedStatement ps = connection.prepareStatement
                ("SELECT * FROM Clients WHERE name like CONCAT" +
                        "('%', ?, '%')")) {
            ps.setString(1, namePart);
            try (ResultSet rs = ps.executeQuery()) {
                printResultSet(rs);
            }
        }
    }
    private static void addOrders() throws SQLException {
        System.out.println("Enter order title: ");
        String title = sc.nextLine();

        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO Orders(title) VALUES (?)")) {
            ps.setString(1, title);
            ps.executeUpdate();
        }
    }

    private static void insertAutoOrders() throws SQLException {
        System.out.println("Enter orders count: ");
        String sCount = sc.nextLine();
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
        String sId = sc.nextLine();
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
        String titilePart = sc.nextLine();
        try(PreparedStatement ps = connection.prepareStatement("SELECT *FROM Orders WHERE title like CONCAT " +
                "('%',?,'%')")){
            ps.setString(1,titilePart);
            try(ResultSet rs = ps.executeQuery()){
                printResultSet(rs);
            }
        }
    }
    private static void addDetails() throws SQLException {
        System.out.println("Enter id client: ");
        String sIdClient = sc.nextLine();
        int idClient = Integer.parseInt(sIdClient);

        System.out.println("Enter address client: ");
        String address = sc.nextLine();

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
        String sIdClient = sc.nextLine();
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

        for(int i = 1; i<=md.getColumnCount();i++){
            System.out.print(md.getColumnName(i)+"\t\t");
        }
        System.out.println();

        while (rs.next()){
            for(int i=1; i<=md.getColumnCount();i++){
                System.out.print(rs.getString(i)+"\t\t");
            }
            System.out.println();
        }
    }
}
