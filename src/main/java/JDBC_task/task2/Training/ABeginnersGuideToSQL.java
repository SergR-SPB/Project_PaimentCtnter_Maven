package JDBC_task.task2.Training;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

/*
* У нас есть книжная библиотека и люди. Также есть специальная таблица для учета выданных книг.
В таблице "books" хранится информация о заголовке, авторе, дате публикации и наличии книги. Все просто.
В таблице “members” — имена и фамилии всех записавшихся в библиотеку людей.
В таблице “borrowings” хранится информация о взятых из библиотеки книгах. Колонка bookid относится к
идентификатору взятой книги в таблице “books”, а колонка memberid относится к соответствующему человеку
из таблицы “members”. У нас также есть дата выдачи и дата, когда книгу нужно вернуть.
*/
public class ABeginnersGuideToSQL {

    private static final String DB_CONNECTION = "jdbc:postgresql://localhost:5432/ABeginnersGuideToSQL";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres";

    private static Connection connection;
    private static Scanner sc;


    public static void main(String[] args) {

        sc = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection
                (DB_CONNECTION, DB_USER, DB_PASSWORD)) {
            connection = conn;

            initBooksDB();
            initMembersDB();
            initBorrowingsDB();
            while (true) {
                showMenu();

                String s = sc.nextLine();

                switch (s) {
                    case "1":
                        addBook();
                        break;

                    case "2":
                        addMember();
                        break;

                    case "3":
                        addBorrowing();
                        break;

                }

            }


        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
    }

    private static void addBook() throws SQLException, ParseException {
        System.out.println("Enter id_book.");
        String sId = sc.nextLine();
        int id_book = Integer.parseInt(sId);

        System.out.println("Enter title book.");
        String title = sc.nextLine();

        System.out.println("Enter author book.");
        String author = sc.nextLine();

        System.out.println("Enter published date book.");
        String sDate = sc.nextLine();


            Date date = (Date) new SimpleDateFormat("dd/MM/YYYY").parse(sDate);



        System.out.println("Enter  stock book.");
        String sStock = sc.nextLine();
        int stock = Integer.parseInt(sStock);


        try (PreparedStatement ps = connection.prepareStatement
                ("INSERT INTO Books (id_book, title, author, published, stock) VALUES (?,?,?,?,?)")) {

            ps.setInt(1, id_book);
            ps.setString(2, title);
            ps.setString(3, author);
            ps.setDate(4, date);
            ps.setInt(5, stock);

            ps.executeUpdate();
        }
    }

    private static void addMember() throws SQLException {

        System.out.println("Enter Member.");
        String sIdM = sc.nextLine();
        int id_member = Integer.parseInt(sIdM);

        System.out.println("Enter first name client.");
        String firstName = sc.nextLine();

        System.out.println("Enter last name client.");
        String lastName = sc.nextLine();

        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO Members (id_member, firstName, lastName) VALUES (?,?,?) "
        )) {
            ps.setInt(1, id_member);
            ps.setString(2, firstName);
            ps.setString(3, lastName);

            ps.executeUpdate();
        }
    }

    private static void addBorrowing() {

    }

    private static void initBooksDB() throws SQLException {
        Statement st = connection.createStatement();
        try {
            st.execute("DROP TABLE IF EXISTS Books");
            st.execute("CREATE TABLE Books(" +
                    "id_book INT NOT NULL, " +
                    "title VARCHAR (30) NOT NULL, " +
                    "author VARCHAR (20)NOT NULL, " +
                    // "published DATE  NOT NULL, " +  // Покажите как правильно
                    "published DATE NOT NULL, " +
                    "stock INT NOT NULL )");
        } finally {
            st.close();
        }
    }

    private static void initMembersDB() throws SQLException {
        Statement st = connection.createStatement();
        try {
            st.execute("DROP TABLE IF EXISTS Members");
            st.execute("CREATE TABLE Members(" +
                    "id_member INT NOT NULL, " +
                    "firstName VARCHAR (30) NOT NULL, " +
                    "lastName VARCHAR (30) NOT NULL )");
        } finally {
            st.close();
        }
    }

    private static void initBorrowingsDB() throws SQLException {
        Statement st = connection.createStatement();
        try {
            st.execute("DROP TABLE IF EXISTS Borrowings");
            st.execute("CREATE TABLE Borrowings(" +
                    "id SERIAL NOT NULL PRIMARY KEY, " +
                    "id_book INT NOT NULL, " +
                    "id_member INT NOT NULL, " +
                    "borrowDate DATE NOT NULL, " +
                    "returnDate DATE NOT NULL)");
        } finally {
            st.close();
        }
    }

    private static void showMenu() {
        System.out.println("1 - add book");
        System.out.println("2 - add clientMember");
        System.out.println("3 - add orderBorrowing");
    }


}
