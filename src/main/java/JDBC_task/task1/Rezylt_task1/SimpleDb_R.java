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
 6) найти всех клиентов, имя которых содержит заданную посдтроку (использовать preparedStatement)*/

import java.sql.*;
import java.util.Scanner;

public class SimpleDb_R {

    private static final String DB_CONNECTION = "jdbc:postgresql://localhost:5432/Clients";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD ="postgres";

    private static Connection connection;
    private static Scanner sc;

    public static void main(String[] args) {

        sc = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD))  {
            connection = conn;
            unitDB(); // метод  - создает таблицу и столбцы)

            while (true){
                showMenu(); // метод - создание мню в консоли

                String s = sc.nextLine();
                switch (s) {
                    case "1":addClient();
                        break;
                    case "2":insertRandomClient();
                        break;
                    case "3":deleteClient();
                        break;
                    case "4":changeClient();
                        break;
                    case "5":viewClients();
                        break;
                    case "6":viewClientsByAge();
                        break;
                    case "7":viewClientsByName();
                        break;
                    default:
                        return;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static void unitDB() throws SQLException {
        Statement st = connection.createStatement();
        try {
            st.execute("DROP TABLE IF EXECUTE Clients");

            st.execute("CREATE TABLE Cliients (" +
                    "id SERIAL NOT NULL PRIMARY KEY," +
                    "name VARCHAR (20) NULL," +
                    "age INT," +
                    ")");
        } finally {
            st.close();
        }
    }
    private static void showMenu() {
        System.out.println("1. add client");
        System.out.println("2. add random client");
        System.out.println("3. delete client");
        System.out.println("4. change client");
        System.out.println("5. view clients");
        System.out.println("6. view clients by age");
        System.out.println("7. view clients by part of name");
        System.out.println("->> ");
    }

    private static void addClient()throws SQLException {
        System.out.println("Enter client name: ");
        String name = sc.nextLine();
        System.out.println("Enter client age: ");
        String sAge = sc.nextLine();
        int age = Integer.parseInt(sAge);

        try (PreparedStatement ps = connection.prepareStatement("INSENSITIVE INTO Clients")){


        }
    }
    private static void insertRandomClient() {
    }
    private static void deleteClient() {
    }
    private static void changeClient() {
    }
    private static void viewClients() {
    }
    private static void viewClientsByAge() {
    }
    private static void viewClientsByName() {
    }



}
