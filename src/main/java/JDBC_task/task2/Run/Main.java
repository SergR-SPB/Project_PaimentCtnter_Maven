package JDBC_task.task2.Run;

import JDBC_task.task2.DB_Operation.AddClient;
import JDBC_task.task2.DB_Operation.EntriDB;

import java.sql.*;
import java.util.Scanner;

//общие замечания:
//  1) форматирование кода (при чем это IDEA умеет делать автоматически!)
//  2) именование сущностей (пакетов, классов, полей, методов). По имени должно быть понятно что происходит
//     Если б начинал смотреть с этого класса, то в жизни не догадался, что EntriDB.entriDB(); устанавливает
//     соединение с БД
public class Main {
    public static void main(String[] args) throws SQLException {
        Scanner sc = new Scanner(System.in);

        try {
            EntriDB.entriDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        unitDB(); // метод  - создает таблицу и столбцы)

        while (true) {
            showMenu(); // метод - создание мню в консоли

            String s = sc.nextLine();
            switch (s) {
                case "1":
                    AddClient.addClient();
                    break;

                default:
                    return;
            }
            viewClients();
        }

        //ГДЕ ЗАКРЫТИЕ КОННЕКШИНА? Если ресурс орткрываете, то его необходимо закрывать (касается
        //всех ресурсов)
    }


    private static void unitDB() throws SQLException {
        Statement st = EntriDB.connection.createStatement();
        try {
            st.execute("DROP TABLE IF EXISTS Clients");

            st.execute("CREATE TABLE Clients (" +
                    "id SERIAL NOT NULL PRIMARY KEY, " +
                    "name VARCHAR(20) NOT NULL, " +
                    "age INT" +
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

    private static void viewClients() throws SQLException {
        try (PreparedStatement ps = EntriDB.connection.prepareStatement("SELECT * FROM Clients")) {
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
