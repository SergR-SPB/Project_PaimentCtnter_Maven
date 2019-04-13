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

//            DB созданы наполнены - не создавать новые
//            initProductDB(); // Product(maker, model, type)
//            initPCDB(); // PC(code, model, speed, ram, hd, cd, price)
//            initLaptopDB();// Laptop(code, model, speed, ram, hd, screen, price)
//            initPrinterDB(); //Printer(code, model, color, type, price)

            while (true) {
                showMenu();
                showMenuSelect();
                String s = sc.nextLine();
                switch (s) {
                    case "01":
                        viewPCPrice();
                        break;

                    case "02":
                        viewPrinterMaker();
                        break;

                    case "03":
                        viewLaptopPrice();
                        break;

                    case "04":
                        viewPCPriceEndCD();
                        break;

                    case "05":
                        viewLaptop10hd();
                        break;

                    case "1":
                        addProduct();
                        break;

                    case "2":
                        viewProduct();
                        break;

                    case "3":
                        addPC();
                        break;

                    case "4":
                        viewPC();
                        break;

                    case "5":
                        addLaptop();
                        break;

                    case "6":
                        viewLaptop();
                        break;

                    case "7":
                        addPrinter();
                        break;

                    case "8":
                        viewPrinter();
                        break;

                    default:
                        return;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* 01. Найдите номер модели, скорость и размер жесткого диска для всех ПК
          стоимостью менее 500 долларов. Вывести: model, speed и hd*/

    private static void viewPCPrice() throws SQLException {

        try (PreparedStatement ps = connection.prepareStatement

                //Выборка из таблицы PC:
                        ("SELECT model, speed, hd, price FROM PC WHERE price < 500")

             //Выборка из таблиц Product & PC:
             //("SELECT Product.model, PC.speed, PC.hd, PC.price    FROM Product, PC WHERE Product.model = PC.model AND price < 500 ")
        ) {
            try (ResultSet rs = ps.executeQuery()) {
                printResultSet(rs);
            }
        }
    }

    /*02. Найдите производителей принтеров. Вывести: maker.*/
    private static void viewPrinterMaker() throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement
                /*для каждой строки из таблицы Product проверяется, есть ли такая модель в таблице Printer.
                Связь между этими таблицами (один-ко-многим) допускает наличие модели в таблице Product,
                которая отсутствовала бы в таблице Printer.  */

                       //("SELECT DISTINCT  maker FROM Product WHERE model IN (SELECT  model FROM Printer)");
                                   /*ВЫБРАТЬ ОТДЕЛЬНО(без повторений) maker (производитель) ИЗ Product (продукт),  ГДЕ model(модель) в (выберите модель из принтера)*/

                        ("SELECT DISTINCT   maker FROM Product WHERE type = 'Printer'")
        ) {
            try
                    (ResultSet rs = ps.executeQuery()) {
                printResultSet(rs);
            }
        }
    }
    /*03. Найдите номер модели, объем памяти и размеры экранов портативных компьютеров, цена которых превышает 1000 долларов.*/
    private static void viewLaptopPrice() throws SQLException {
       try( PreparedStatement ps = connection.prepareStatement("SELECT model, speed, ram, hd, screen, price FROM Laptop WHERE  price>1000")){
           try (ResultSet rs = ps.executeQuery()){
               printResultSet(rs);
           }
       }
    }

    /*04. Найдите номер модели, скорость и размер жесткого диска ПК, имеющих 12х или 24х CD и цену менее 600 долларов.*/
    private static void viewPCPriceEndCD() throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement

                /* ("SELECT model, speed, ram, cd, price FROM PC WHERE price<600 AND " +
                         "cd BETWEEN '12x' AND '24x'") */

                /*   ("SELECT model, speed, ram, cd, price FROM PC WHERE price<600 AND " +
                           "cd IN('12x' , '24x') ")*/
                        ("SELECT model, speed, ram, cd, price FROM PC WHERE price<600 AND " +
                                "cd = '12x' or  cd = '24x'")
        ) {
            try (ResultSet rs = ps.executeQuery()) {
                printResultSet(rs);
            }
        }
    }
    /*05. Укажите производителя и скорость портативных компьютеров с жестким диском объемом не менее 10 Гбайт*/
    private static void viewLaptop10hd() throws SQLException{
        try ( PreparedStatement ps = connection.prepareStatement
                /*("SELECT DISTINCT Product.maker, Laptop.speed FROM Product, Laptop " +
                        "WHERE Product.model = Laptop.model AND Laptop.hd >= 10")*/

                   ("SELECT DISTINCT Product.maker, Laptop.speed FROM Product JOIN  Laptop " +
                          "ON Product.model = Laptop.model WHERE Laptop.hd >= 10 ")
                ){
            try (ResultSet rs = ps.executeQuery()){
                printResultSet(rs);
            }
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
        } finally {
            st.close();
        }
    }

    private static void initPCDB() throws SQLException {
        // PC(code, model, speed, ram, hd, cd, price)
        Statement st = connection.createStatement();
        try {
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
        } finally {
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

    private static void initPrinterDB() throws SQLException {
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
        } finally {
            st.close();
        }
    }

    private static void showMenuSelect() {
        System.out.println("01. view PC Price, Output: model, speed and hd < 500 $");
        System.out.println("02. view Printer Maker, output: maker");
        System.out.println("03. view Laptop Price, output: model, speed, ram, hd, screen> 1000$");
        System.out.println("04. view PC Price End CD, output: speed, ram, hd <600 end cd = 12х or 24х ");
        System.out.println("05. view Laptop 10hd, output: maker, Laptop.speed");


    }

    private static void showMenu() {
        System.out.println("1. add Product");
        System.out.println("2. view Product");
        System.out.println("3. add PC");
        System.out.println("4. view PC");
        System.out.println("5. add Laptop");
        System.out.println("6. view Laptop");
        System.out.println("7. add Printer");
        System.out.println("8. view Printer");
        System.out.println("------------------------------------ ");
    }


    private static void addProduct() throws SQLException {
        System.out.println("Enter product maker: ");
        String maker = sc.nextLine();
        System.out.println("Enter product model: ");
        String model = sc.nextLine();
        System.out.println("Enter product type: ");
        String type = sc.nextLine();

        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO" +
                " Product(maker,model,type) VALUES(?,?,?) ")) {
            ps.setString(1, maker);
            ps.setString(2, model);
            ps.setString(3, type);
            ps.executeUpdate();
        }

    }

    private static void addPC() throws SQLException {
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

        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO " +
                "PC (code,model,speed,ram,hd,cd,price) VALUES (?,?,?,?,?,?,?)")) {
            ps.setInt(1, code);
            ps.setString(2, model);
            ps.setInt(3, speed);
            ps.setInt(4, ram);
            ps.setInt(5, hd);
            ps.setString(6, cd);
            ps.setInt(7, price);

            ps.executeUpdate();
        }
    }

    private static void addLaptop() throws SQLException {
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


        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO " +
                "Laptop (code,model,speed,ram,hd,price,screen) VALUES (?,?,?,?,?,?,?)")) {
            ps.setInt(1, code);
            ps.setString(2, model);
            ps.setInt(3, speed);
            ps.setInt(4, ram);
            ps.setInt(5, hd);
            ps.setInt(6, price);
            ps.setInt(7, screen);

            ps.executeUpdate();
        }
    }

    private static void addPrinter() throws SQLException {
        System.out.println("Enter Printer code: ");
        String sCode = sc.nextLine();
        int code = Integer.parseInt(sCode);

        System.out.println("Enter Printer model: ");
        String model = sc.nextLine();

        System.out.println("Enter Printer color: ");
        String color = sc.nextLine();

        System.out.println("Enter Printer type:");
        String type = sc.nextLine();

        System.out.println("Enter Printer price:");
        String sPrice = sc.nextLine();
        int price = Integer.parseInt(sPrice);


        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO " +
                "Printer (code,model,color,type,price) VALUES (?,?,?,?,?)")) {
            ps.setInt(1, code);
            ps.setString(2, model);
            ps.setString(3, color);
            ps.setString(4, type);
            ps.setInt(5, price);


            ps.executeUpdate();
        }
    }

    private static void viewProduct() throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT *FROM Product")) {
            try (ResultSet rs = ps.executeQuery()) {
                printResultSet(rs);
            }
        }
    }

    private static void viewPC() throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT *FROM PC")) {
            try (ResultSet rs = ps.executeQuery()) {
                printResultSet(rs);
            }
        }
    }


    private static void viewLaptop() throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT *FROM PC")) {
            try (ResultSet rs = ps.executeQuery()) {
                printResultSet(rs);
            }
        }
    }

    private static void viewPrinter() throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT *FROM Printer")) {
            try (ResultSet rs = ps.executeQuery()) {
                printResultSet(rs);
            }
        }
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
