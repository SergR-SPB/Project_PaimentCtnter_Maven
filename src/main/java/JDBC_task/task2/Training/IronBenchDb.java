package JDBC_task.task2.Training;


    /*
   источник http://sql-tutorial.ru/ru/book_computers_database.html
   ++21 упражнение


    Схема БД состоит из четырех таблиц :

Product(maker, model, type)
PC(code, model, speed, ram, hd, cd, price)
Laptop(code, model, speed, ram, hd, screen, price)
Printer(code, model, color, type, price)
Таблица Product представляет производителя (maker), номер модели (model) и тип (PC — ПК, Laptop — портативный компьютер или Printer — принтер).
Предполагается, что в этой таблице номера моделей уникальны для всех производителей и типов продуктов.

В таблице PC для каждого номера модели, обозначающего ПК, указаны скорость процессора — speed (МГерц),
общий объем оперативной памяти - ram (Мбайт), размер диска — hd (в Гбайт), скорость считывающего устройства - cd (например, '4х') и цена — price.

Таблица Laptop аналогична таблице РС за исключением того, что вместо скорости CD-привода содержит размер экрана — screen (в дюймах).

В таблице Printer для каждой модели принтера указывается, является ли он цветным — color ('y', если цветной),
тип принтера — type (лазерный — Laser, струйный — Jet или матричный — Matrix)
и цена — price.
+ */

import java.sql.*;
import java.util.Random;
import java.util.Scanner;

public class IronBenchDb {

    private static final String DB_CONNECTION = "jdbc:postgresql://localhost:5432/IronBench";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres";

    private static Connection connection;
    private static Scanner sc;

    public static void main(String[] args) {

        sc = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection
                (DB_CONNECTION, DB_USER, DB_PASSWORD)) {
            connection = conn;
            initProductDB(); // Product(maker, model, type)
            initPCDB(); // PC(code, model, speed, ram, hd, cd, price)
            initLaptopDB();// Laptop(code, model, speed, ram, hd, screen, price)
            initPrinterDB(); //Printer(code, model, color, type, price)

           while (true) {
                showMenu();
                String s = sc.nextLine();
                switch (s) {
                    case "1":
                        addProduct();
                        break;
                    case "2":
                        insertRandomProduct();
                        break;
                    case "3":
                        addPC();
                        break;
                    case "4":
                        insertRandomPC();
                        break;
                    case "5":
                        addLaptop();
                        break;
                    case "6":
                        insertRandomLaptop();
                        break;
                    case "7":
                        addPrinter();
                        break;
                    case "8":
                        insertRandomPrinter();
                        break;

                    default:
                        return;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private static void initProductDB() throws SQLException {
        // Product(maker, model, type)
        Statement st = connection.createStatement();
        try {
            st.execute("DROP TABLE IF EXISTS    Product");

            st.execute("CREATE TABLE Product (" +
                    "id SERIAL NOT NULL PRIMARY KEY, " +
                    "maker VARCHAR (10) NOT NULL," +
                    "model VARCHAR (50) NOT NULL," +
                    "type VARCHAR (50) NOT NULL) ");
        }finally {
            st.close();
        }
    }

    private static void initPCDB() throws SQLException {
        // PC(code, model, speed, ram, hd, cd, price)
        Statement st = connection.createStatement();
        try{
            st.execute("DROP  TABLE IF EXISTS PC");

            st.execute("CREATE TABLE PC (" +
                    "id SERIAL NOT NULL PRIMARY KEY, " +
                    "code INT," +
                    "model VARCHAR (50)," +
                    "speed INT ," +
                    "ram INT ," +
                    "hd INT ," +
                    "cd VARCHAR (10)," +
                    "price INT)");
        }finally {
            st.close();

        }
    }

    private static void initLaptopDB() throws SQLException {
        // Laptop(code, model, speed, ram, hd, screen, price)
        Statement st = connection.createStatement();
        try {
            st.execute("DROP TABLE IF EXISTS Laptop");
            st.execute("CREATE TABLE Laptop (" +
                    "id SERIAL NOT NULL PRIMARY KEY, " +
                    "code INT," +
                    "model VARCHAR (50)," +
                    "speed INT ," +
                    "ram INT ," +
                    "hd INT ," +
                    "price INT," +
                    "screen INT " +
                    ")");
        } finally {
            st.close();
        }
    }
    private static void initPrinterDB() throws SQLException{
        //Printer(code, model, color, type, price)
        Statement st = connection.createStatement();
        try {
            st.execute("DROP TABLE IF EXISTS Printer");
            st.execute("CREATE TABLE Printer (" +
                    "id SERIAL NOT NULL PRIMARY KEY, " +
                    "code INT," +
                    "model VARCHAR (50)," +
                    "color CHAR (1)," +
                    "type VARCHAR (10)," +
                    "price MONEY)");
        }finally {
            st.close();
        }
    }
    private static void showMenu() {
        System.out.println("1. add Product");
        System.out.println("2. add random Product");
        System.out.println("3. add PC");
        System.out.println("4. add random PC");
        System.out.println("5. add Laptop");
        System.out.println("6. add random Laptop");
        System.out.println("7. add Printer");
        System.out.println("8. add random Printer");
        System.out.println("------------------------------------ ");
    }


    private static void addProduct()throws SQLException{
        System.out.println("Enter product maker: ");
        String maker = sc.nextLine();
        System.out.println("Enter product model: ");
        String model = sc.nextLine();
        System.out.println("Enter product type: ");
        String type = sc.nextLine();

        try( PreparedStatement ps= connection.prepareStatement("INSERT INTO" +
                " Product(maker,model,type) VALUES(?,?,?) ")){
            ps.setString(1,maker);
            ps.setString(2,model);
            ps.setString(3,type);
            ps.executeUpdate();
        }

    }

    private static void insertRandomProduct() throws SQLException{

    }

    private static void addPC() throws SQLException{
        System.out.println("Enter PC code: ");
        String sCode = sc.nextLine();
        int code = Integer.parseInt(sCode);

        System.out.println("Enter PC model: ");
        String model = sc.nextLine();

        System.out.println("Enter PC speed: ");
        String sSpeed = sc.nextLine();
        int speed = Integer.parseInt(sSpeed);

        System.out.println("Enter PC ram:");
        String sRam = sc.nextLine();
        int ram = Integer.parseInt(sRam);

        System.out.println("Enter PC hd:");
        String sHd = sc.nextLine();
        int hd = Integer.parseInt(sHd);

        System.out.println("Enter PC cd:");
        String cd = sc.nextLine();

        System.out.println("Enter PC price:");
        String sPrice = sc.nextLine();
        int price = Integer.parseInt(sPrice);

        try(PreparedStatement ps = connection.prepareStatement("INSERT INTO " +
                "PC (code,model,speed,ram,hd,cd,price) VALUES (?,?,?,?,?,?,?)")){
            ps.setInt(1,code);
            ps.setString(2,model);
            ps.setInt(3,speed);
            ps.setInt(4,ram);
            ps.setInt(5,hd);
            ps.setString(6,cd);
            ps.setInt(7,price);

            ps.executeUpdate();
        }
    }

    private static void insertRandomPC() throws SQLException{

    }

    private static void addLaptop() throws SQLException{
        System.out.println("Enter Laptop code: ");
        String sCode = sc.nextLine();
        int code = Integer.parseInt(sCode);

        System.out.println("Enter Laptop model: ");
        String model = sc.nextLine();

        System.out.println("Enter Laptop speed: ");
        String sSpeed = sc.nextLine();
        int speed = Integer.parseInt(sSpeed);

        System.out.println("Enter Laptop ram:");
        String sRam = sc.nextLine();
        int ram = Integer.parseInt(sRam);

        System.out.println("Enter Laptop hd:");
        String sHd = sc.nextLine();
        int hd = Integer.parseInt(sHd);

        System.out.println("Enter Laptop price:");
        String sPrice = sc.nextLine();
        int price = Integer.parseInt(sPrice);

        System.out.println("Enter Laptop screen:");
        String sScreen = sc.nextLine();
        int screen = Integer.parseInt(sScreen);


        try(PreparedStatement ps = connection.prepareStatement("INSERT INTO " +
                "Laptop (code,model,speed,ram,hd,price,screen) VALUES (?,?,?,?,?,?,?)")){
            ps.setInt(1,code);
            ps.setString(2,model);
            ps.setInt(3,speed);
            ps.setInt(4,ram);
            ps.setInt(5,hd);
            ps.setInt(6,price);
            ps.setInt(7,screen);

            ps.executeUpdate();
        }

    }

    private static void insertRandomLaptop() throws SQLException{
    }

    private static void addPrinter() throws SQLException{
        System.out.println("Enter Printer code: ");
        String sCode = sc.nextLine();
        int code = Integer.parseInt(sCode);

        System.out.println("Enter Printer model: ");
        String model = sc.nextLine();

        System.out.println("Enter Printer color: ");
        String color= sc.nextLine();

        System.out.println("Enter Printer type:");
        String type = sc.nextLine();

        System.out.println("Enter Printer price:");
        String sPrice = sc.nextLine();
        int price = Integer.parseInt(sPrice);




        try(PreparedStatement ps = connection.prepareStatement("INSERT INTO " +
                "Printer (code,model,color,type,price) VALUES (?,?,?,?,?)")){
            ps.setInt(1,code);
            ps.setString(2,model);
            ps.setString(3,color);
            ps.setString(4,type);
            ps.setInt(5,price);


            ps.executeUpdate();
        }

    }

    private static void insertRandomPrinter() throws SQLException{
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

    private static void viewOrders() throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT *FROM Orders")) {
            try (ResultSet rs = ps.executeQuery()) {
                printResultSet(rs);
            }
        }
    }

    private static void viewOrdersByTitle() throws SQLException {
        System.out.println("Enter part of title: ");
        String titilePart = sc.nextLine();
        try (PreparedStatement ps = connection.prepareStatement("SELECT *FROM Orders WHERE title like CONCAT " +
                "('%',?,'%')")) {
            ps.setString(1, titilePart);
            try (ResultSet rs = ps.executeQuery()) {
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
