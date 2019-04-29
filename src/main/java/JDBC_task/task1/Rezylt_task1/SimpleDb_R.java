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
Подумать какие связи с таблицей Clients будут у добавляемых таблицах.
При удалении информации о клиенте должны удаляться все его заказы и детальная информация.
Основные операции дополнить следующими пунктами:
 - добавить заказ для клиента
 - удалить заказ для клиента
 - вывести все заказы указанного клиента (указывать id клиента)
 - добавить детализированную информацию для клиента
 - изменить детализированную информацию для клиента
 - удалить детализированную информацию для клиента
 */

import java.sql.*;
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
            // initClientsDB(); // метод  - создает таблицу Clients
            // initProductDB();   //таблица  Product (id_product, title_product)
            // initOrdersDB(); // таблица Orders
            // initDetailsDB();// таблица Details

            while (true) {

                showMenuClients();
                showMenuProduct();
                showMenuOrders();
                showMenuDetails();

                String s = sc.nextLine();

                switch (s) {
                    case "1":
                        addClient();
                        break;
                    case "11":
                        viewClients();
                        break;
                    case "111":
                        deleteClient();
                        break;
                    case "2":
                        addProduct();
                        break;
                    case "22":
                        viewProduct();
                        break;
                    case "3":
                        addOrders();
                        break;
                    case "33":
                        viewOrdersByIdClient();
                        break;
                    case "333":
                        viewOrdersByIdProduct();
                        break;
                    case "334":
                        viewAllOrders();
                        break;
                    case "335":
                        deleteOrderByClient();
                        break;
                    case "336":
                        deleteClientAllHisOrders();
                        break;
                    case "4":
                        addDetails();
                        break;
                    case "44":
                        viewDetails();
                        break;
                    case "444":
                        viewDetailsByIdClient();
                        break;
                    case "445":
                        deleteClientAllHisDetails();
                        break;
                    case "446":
                        changeDetailsByIdClients();
                        break;


                    /*case "1.2.":
                        insertRandomClient();
                        break;

                    case "1.4.":
                        changeClient();
                        break;*/

                    /*case "1.6.":
                        viewClientsByAge();
                        break;
                    case "1.7.":
                        viewClientsByName();
                        break;*/

                    /*case "2.2.":
                        insertAutoOrders();
                        break;
                    case "2.3.":
                        deleteOrders();
                        break;*/

                    /*case "2.5.":
                        viewOrdersByTitle();
                        break;*/

                    /*case "3.2.":
                        deleteDetails();
                        break;*/

                    default:
                        return;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static void initClientsDB() throws SQLException {
        Statement st = connection.createStatement();
        try {
            st.execute("DROP TABLE IF EXISTS Clients");

            st.execute("CREATE TABLE Clients (" +
                    "id SERIAL NOT NULL PRIMARY KEY, " +
                    "id_client INT NOT NULL  , " +
                    "name VARCHAR(20) NOT NULL, age INT)");
        } finally {
            st.close();
        }
    }

    public static void initProductDB() throws SQLException {
        Statement st = connection.createStatement();
        try {
            st.execute("DROP TABLE IF EXISTS Product");
            st.execute("CREATE TABLE Product ( " +
                    "id SERIAL NOT NULL PRIMARY KEY, " +
                    "id_product  INT NOT NULL," +
                    "title_product VARCHAR (50))");
        } finally {
            st.close();
        }
    }

    private static void initOrdersDB() throws SQLException {
        Statement st = connection.createStatement();
        try {
            st.execute("DROP TABLE IF EXISTS Orders");
            st.execute("CREATE TABLE Orders(" +
                    "id SERIAL NOT NULL PRIMARY KEY," +
                    "id_client INT NOT NULL  , " +
                    "id_product INT NOT NULL )");
        } finally {
            st.close();
        }
    }

    private static void initDetailsDB() throws SQLException {
        Statement st = connection.createStatement();
        try {
            st.execute("DROP TABLE IF EXISTS Details");
            st.execute("CREATE TABLE Details(id SERIAL NOT NULL PRIMARY KEY," +
                    "id_client INT NOT NULL, " +
                    "address VARCHAR (50) NOT NULL )");
        } finally {
            st.close();
        }
    }

    private static void showMenuClients() {
        System.out.println("1 - add client");
        System.out.println("11 - view clients");
        //System.out.println("1.2. add random client");
        System.out.println("111 delete client");
        //System.out.println("1.4. change client");
        //System.out.println("1.6. view clients by age");
        //System.out.println("1.7. view clients by part of name");
        System.out.println("------------------------------------ ");
    }

    private static void showMenuProduct() {
        System.out.println("2 - add product");
        System.out.println("22 - view product");
        System.out.println("------------------------------------ ");
    }

    private static void showMenuOrders() {
        System.out.println("3   - add orders");
        System.out.println("33  - view orders by client");
        System.out.println("333 - view orders by product");
        System.out.println("334 - view all orders");
        System.out.println("335 - delete order by client");
        System.out.println("336 - delete client & all his orders");
        //System.out.println("2.2. add random orders");
        //System.out.println("2.3. delete orders");
        //System.out.println("2.5. view orders by part of title");
        System.out.println("------------------------------------ ");
    }

    private static void showMenuDetails() {
        System.out.println("4 - add details by id client: ");
        System.out.println("44 - view details clients: ");
        System.out.println("444 - view details by id client: ");
        System.out.println("445 - delete Client All His Details:");
        System.out.println("446 - change Details By Id Clients:");
        //System.out.println("3.2. delete details by id client: ");
    }

//Client

    private static void addClient() throws SQLException {
        System.out.println("Enter id_client");
        String sId_client = sc.nextLine();
        int id_client = Integer.parseInt(sId_client);

        System.out.println("Enter client name: ");
        String name = sc.nextLine();

        System.out.println("Enter client age: ");
        String sAge = sc.nextLine();
        int age = Integer.parseInt(sAge);

        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO " +
                "Clients (id_client, name,age) VALUES (?, ?,?)")) {
            ps.setInt(1, id_client);
            ps.setString(2, name);
            ps.setInt(3, age);
            ps.executeUpdate();  // ------- for INSERT, UPDATE & DELETE
        }
    }

    private static void viewClients() throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM Clients")) {
            try (ResultSet rs = ps.executeQuery()) {
                printResultSet(rs);
            }
        }
    }

   /* private static void insertRandomClient() throws SQLException {
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
    }*/

    private static void deleteClient() throws SQLException {
        System.out.println("Enter client ID: ");
        String sId = sc.nextLine();
        int id = Integer.parseInt(sId);

        try (PreparedStatement ps = connection.prepareStatement
                ("DELETE FROM Clients WHERE id= " + id)) {
            ps.executeUpdate();
        }
    }

    /*private static void changeClient() throws SQLException {
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
    }*/

    public static void addProduct() throws SQLException {
        System.out.println("Enter ID product:");
        String sIdProd = sc.nextLine();
        int id_product = Integer.parseInt(sIdProd);
        System.out.println("Enter title product:");
        String title_product = sc.nextLine();
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO Product(id_product, title_product) " +
                "VALUES (?,?)")) {
            ps.setInt(1, id_product);
            ps.setString(2, title_product);
            ps.executeUpdate();
        }
    }

    public static void viewProduct() throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM Product")) {
            try (ResultSet rs = ps.executeQuery()) {
                printResultSet(rs);
            }
        }
    }

    private static void addOrders() throws SQLException {
        viewClients(); // Для выбора искомого клиента
        System.out.println("Enter id client");
        String sId_client = sc.nextLine();
        int id_client = Integer.parseInt(sId_client);

        viewProduct(); // Для выбора искомого продукта
        System.out.println("Enter id product");
        String sId_product = sc.nextLine();
        int id_product = Integer.parseInt(sId_product);

        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO Orders(id_client, id_product) VALUES (?,?)")) {
            ps.setInt(1, id_client);
            ps.setInt(2, id_product);
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
                    ps.setString(1, "Title" + (i + 1));
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

    //- вывести все заказы указанного клиента (указывать id клиента)

    private static void viewOrdersByIdClient() throws SQLException {

        viewClients(); // Для выбора искомого клиента

        System.out.println("Enter orders id_client: ");
        String sId = sc.nextLine();
        int id_client = Integer.parseInt(sId);

        try (PreparedStatement ps = connection.prepareStatement
                ("SELECT Orders.id_client, Clients.name, " +
                        //"Orders.id_product,  " +
                        "Product.title_product " +
                        "FROM Orders " +
                        "JOIN Clients ON Orders.id_client = Clients.id_client " +
                        "JOIN Product ON Orders.id_product = Product.id_product " +
                        "WHERE Clients.id_client =" + id_client)) {
            try (ResultSet rs = ps.executeQuery()) {
                printResultSet(rs);
            }
        }
    }

    private static void viewOrdersByIdProduct() throws SQLException {
        viewProduct(); // Для выбора искомого продукта

        System.out.println("Enter  id_product: ");
        String sId = sc.nextLine();
        int id_product = Integer.parseInt(sId);

        try (PreparedStatement ps = connection.prepareStatement

                ("SELECT Product.id_product, Product.title_product, Clients.name " +

                        "FROM Orders " +
                        "JOIN Clients ON Orders.id_client = Clients.id_client " +
                        "JOIN Product ON Orders.id_product = Product.id_product " +
                        "WHERE Product.id_product =" + id_product)) {
            try (ResultSet rs = ps.executeQuery()) {
                printResultSet(rs);
            }
        }
    }

    //- вывести все заказы

    private static void viewAllOrders() throws SQLException {

        try (PreparedStatement ps = connection.prepareStatement
                ("SELECT Orders.id_client, Clients.name, " +
                        //"Orders.id_product,  " +
                        "Product.title_product " +
                        "FROM Orders " +
                        "JOIN Clients ON Orders.id_client = Clients.id_client " +
                        "JOIN Product ON Orders.id_product = Product.id_product ")) {
            try (ResultSet rs = ps.executeQuery()) {
                printResultSet(rs);
            }
        }
    }

    private static void deleteOrderByClient() throws SQLException {
        System.out.println("Enter id client to delete his orders");
        String sId = sc.nextLine();
        int id = Integer.parseInt(sId);

        try (PreparedStatement ps = connection.prepareStatement
                ("DELETE FROM Orders WHERE id_client=" + id)) {
            ps.executeUpdate();
        }
    }

    private static void deleteClientAllHisOrders() throws SQLException {
        viewClients();
        System.out.println("Enter id client to delete & his orders");
        String sId = sc.nextLine();
        int id = Integer.parseInt(sId);

        connection.setAutoCommit(false);

        try (PreparedStatement ps = connection.prepareStatement
                /* ("DELETE FROM Clients , Orders" +
                          "WHERE Clients.id_client = Orders.id_client " +
                          "AND Clients.id_client =" + id ))*/

                        ("DELETE FROM Clients WHERE id_client=" + id +
                                " ; DELETE FROM Orders WHERE id_client=" + id)

                        /*("DELETE Clients.* , Orders.* from Clients , Orders " +
                                "WHERE Clients.id_client = Orders.id_client AND Clients.id_client="+id)*/
        ) {
            ps.executeUpdate();
            connection.commit();
        } catch (Exception ex) {
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
        }
    }




   /* private static void viewOrdersByTitle() throws SQLException {
        System.out.println("Enter part of title: ");
        String titilePart = sc.nextLine();
        try (PreparedStatement ps = connection.prepareStatement("SELECT *FROM Orders WHERE title like CONCAT " +
                "('%',?,'%')")) {
            ps.setString(1, titilePart);
            try (ResultSet rs = ps.executeQuery()) {
                printResultSet(rs);
            }
        }
    }*/

    private static void addDetails() throws SQLException {
        viewClients();
        System.out.println("Enter id client: ");
        String sIdC = sc.nextLine();
        int id = Integer.parseInt(sIdC);

        System.out.println("Enter address client: ");
        String address = sc.nextLine();

        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO Details(id_client,address)" +
                        "VALUES (?,?)")) {
            ps.setInt(1, id);
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
        try (PreparedStatement ps = connection.prepareStatement
                ("SELECT Details.id_client, Clients.name, " +
                        "Details.address " +
                        "FROM Details, Clients " +
                        "WHERE Clients.id_client=Details.id_client")) {
            try (ResultSet rs = ps.executeQuery()) {
                printResultSet(rs);
            }
        }
    }

    private static void viewDetailsByIdClient() throws SQLException {
        viewClients();
        System.out.println("Enter id clients:");
        String sId = sc.nextLine();
        int id_cl = Integer.parseInt(sId);

        try (PreparedStatement ps = connection.prepareStatement
                ("SELECT Details.id_client, Clients.name, " +
                        "Details.address " +
                        "FROM Details, Clients " +
                        "WHERE  Details.id_client =Clients.id_client "+
                        "AND Details.id_client =" + id_cl)) {
            try (ResultSet rs = ps.executeQuery()) {
                printResultSet(rs);
            }
        }
    }

    private static void deleteClientAllHisDetails() throws SQLException {
        viewClients();
        System.out.println("Enter id clients:");
        String sId = sc.nextLine();
        int id = Integer.parseInt(sId);
        connection.setAutoCommit(false);
        try (PreparedStatement ps = connection.prepareStatement
                ("DELETE FROM Clients WHERE id_client=" + id +
                        " ; DELETE FROM Details WHERE id_client=" + id)) {
            ps.executeUpdate();
            connection.commit();
        } catch (Exception ex) {
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
        }
    }

    private static void changeDetailsByIdClients() throws SQLException{

        viewClients();
        System.out.println("Enter id clients to change details");
        String sId = sc.nextLine();
        int id_C = Integer.parseInt(sId);

        System.out.println("Enter new address by this client");
        String address =sc.nextLine();

        try (PreparedStatement ps = connection.prepareStatement
                ("UPDATE Details SET address = ? WHERE  id_client = ?")){
            ps.setString(1, address);
            ps.setInt(2,id_C);
            ps.executeUpdate();
        }
        viewDetailsByIdClient();
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


}
