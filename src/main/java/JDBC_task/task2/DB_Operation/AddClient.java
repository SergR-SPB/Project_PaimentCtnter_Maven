package JDBC_task.task2.DB_Operation;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

//для создания клиента не нужен класс Создать Клиента
//в прошлый раз Вы всю логику в один метод поместили, а сейчас
//по классу на каждый метод стали создавать
//Код надо писать не только для того, чтоб он работал, но и для того,
//чтоб его можно было прочесть и понять. В том числе и Вам. Через пару месяцев
//Вы и не вспомните что тут делается
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
