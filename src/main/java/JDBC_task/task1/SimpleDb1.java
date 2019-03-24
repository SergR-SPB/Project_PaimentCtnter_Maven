package JDBC_task.task1;




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
import java.util.Random;
import java.util.Scanner;

public class SimpleDb1 {

    //указание с какой BD  работать
    //если требуется, то пишите комментарии перед или после кода
    private static final String DB_CONNECTION = "jdbc:postgresql:" + //писать комментарии между операциями не следует
            "//localhost:5432/Clients";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres";

    private static Connection connection;
    private static Scanner sc;

    public static void main(String[] args) {
        sc = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD)) {
            connection = conn;
            initDB();

            while (true) {
                showMenu();

                String s = sc.nextLine();
                switch (s) {
                    case "1":
                        addClient();
                        break;
                    case "2":
                        insertRandomClients();
                        break;
                    case "3":
                        deleteClient();
                        break;
                    case "4":
                        changeClient();
                        break;
                    case "5":
                        viewClients();
                        break;
                    case "6":
                        viewClientsByAge();
                        break;
                    case "7":
                        viewClientsByName();
                        break;
                    default:
                        return;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void initDB() throws SQLException {
        Statement st = connection.createStatement();
        //вариант до Java 7 (с обычным try)
        try {
            st.execute("DROP TABLE IF EXISTS Clients");
            //По условию для клиента необходимо хранить только имя и возрас!
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
        System.out.println("1: add client");
        System.out.println("2: add random clients");
        System.out.println("3: delete client");
        System.out.println("4: change client");
        System.out.println("5: view clients");
        System.out.println("6: view clients by age");
        System.out.println("7: view clients by part of name");
        System.out.print("-> ");
    }

    private static void addClient() throws SQLException {
        System.out.print("Enter client name: ");
        String name = sc.nextLine();
        System.out.print("Enter client age: ");
        String sAge = sc.nextLine();
        int age = Integer.parseInt(sAge);

        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO " +
                "Clients (name, age) VALUES(?, ?)")) {
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.executeUpdate(); // for INSERT, UPDATE & DELETE
        }
    }

    private static void insertRandomClients() throws SQLException {
        System.out.print("Enter clients count: ");
        String sCount = sc.nextLine();
        int count = Integer.parseInt(sCount);
        Random rnd = new Random();

        connection.setAutoCommit(false); // enable transactions
        try {
            try (PreparedStatement ps = connection.prepareStatement("INSERT INTO Clients (name, age) VALUES(?, ?)")) {
                for (int i = 0; i < count; i++) {
                    ps.setString(1, "Name" + i);
                    ps.setInt(2, rnd.nextInt(100));
                    ps.executeUpdate();
                }
                connection.commit();

            } catch (Exception ex) {
                connection.rollback();
            }
        } finally {
            connection.setAutoCommit(true); // return to default mode
        }
    }

    private static void deleteClient() throws SQLException {
        System.out.print("Enter client id: ");
        String sId = sc.nextLine();
        int id = Integer.parseInt(sId);

        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM Clients WHERE id = " + id)) {
            ps.executeUpdate(); // for INSERT, UPDATE & DELETE
        }
    }

    private static void changeClient() throws SQLException {
        System.out.print("Enter client name: ");
        String name = sc.nextLine();
        System.out.print("Enter new age: ");
        String sAge = sc.nextLine();
        int age = Integer.parseInt(sAge);

        try (PreparedStatement ps = connection.prepareStatement("UPDATE Clients SET age = ? WHERE name = ?")) {
            ps.setInt(1, age);
            ps.setString(2, name);
            ps.executeUpdate(); // for INSERT, UPDATE & DELETE
        }
    }

    private static void viewClients() throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM Clients")) {
            // table of data representing a database result set,
            try (ResultSet rs = ps.executeQuery()) {
                printResultSet(rs);
            }
        }
    }

    private static void viewClientsByAge() throws SQLException {
        System.out.print("Enter min age: ");
        String sAge = sc.nextLine();
        int age = Integer.parseInt(sAge);

        try (Statement s = connection.createStatement()) {      //тут используем обычный Statement
            // table of data representing a database result set,
            try (ResultSet rs = s.executeQuery("SELECT * FROM Clients WHERE age > " + age)) {
                printResultSet(rs);
            }
        }
    }

    private static void viewClientsByName() throws SQLException {
        System.out.print("Enter part of name: ");
        String namePart = sc.nextLine();

        //тут используем PreparedStatement
        try (PreparedStatement ps =
                     connection.prepareStatement("SELECT * FROM Clients WHERE name like CONCAT('%', ?, '%')")) {
            ps.setString(1, namePart);
            // table of data representing a database result set,
            try (ResultSet rs = ps.executeQuery()) {
                printResultSet(rs);
            }
        }
    }

    private static void printResultSet(ResultSet rs) throws SQLException {
        // can be used to get information about the types and properties of the columns in a ResultSet object
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

    //Метод оставил только для того, чтоб прокомментировать
    //Вообще весь функционал содержится в других методах
    private void testDatabase() {
        String sqlDropTable = "DROP TABLE IF EXISTS JC_CLIENTS";

        String sqlCreateTable = "CREATE TABLE JC_CLIENTS" +
                "(       JC_CLIENTS SERIAL," +
                "        FIRST_NAME VARCHAR(50) NOT NULL," +
                "        LAST_NAME VARCHAR(50) NOT NULL," +
                "        BIRTH_DATE DATE NOT NULL," +
                "        PHONE VARCHAR(50) NOT NULL," +
                "        EMAIL VARCHAR(50) NOT NULL," +
                "        PRIMARY KEY (JC_CLIENTS))";

        //смотрим про принцип DRY
        //не следует повторять код!!! (INSERT INTO JC_CLIENTS (FIRST_NAME, LAST_NAME, BIRTH_DATE, PHONE, EMAIL) VALUES)
        String sqlInsert = "INSERT INTO JC_CLIENTS (FIRST_NAME, LAST_NAME, BIRTH_DATE, PHONE, EMAIL) VALUES ('One','First','1961-12-12','+79112345678','peter@pisem.net');" +
                "  INSERT INTO JC_CLIENTS (FIRST_NAME, LAST_NAME, BIRTH_DATE, PHONE, EMAIL) VALUES ('Two','Second','1971-12-12','+79112345678','peter@pisem.net');" +
                "  INSERT INTO JC_CLIENTS (FIRST_NAME, LAST_NAME, BIRTH_DATE, PHONE, EMAIL) VALUES ('Three','Third','1981-12-12','+79112345678','peter@pisem.net');" +
                "  INSERT INTO JC_CLIENTS (FIRST_NAME, LAST_NAME, BIRTH_DATE, PHONE, EMAIL) VALUES ('Four','Fourth','1991-12-12','+79112345678','peter@pisem.net');" +
                "  INSERT INTO JC_CLIENTS (FIRST_NAME, LAST_NAME, BIRTH_DATE,PHONE, EMAIL) VALUES ('Peter','Belgy','2001-12-12','+79112345678','peter@pisem.net');" +
                "  INSERT INTO JC_CLIENTS (FIRST_NAME, LAST_NAME, BIRTH_DATE,PHONE, EMAIL) VALUES ('Helga','Forte','2001-12-12','+79118765432','helga@pisem.net');" +
                "  INSERT INTO JC_CLIENTS (FIRST_NAME, LAST_NAME, BIRTH_DATE,PHONE, EMAIL) VALUES ('Zero','Zero','2001-12-12','+79118765432','helga@pisem.net');" +
                "  INSERT INTO JC_CLIENTS (FIRST_NAME, LAST_NAME, BIRTH_DATE,PHONE, EMAIL) VALUES ('Zero','W_Zero','1991-12-12','+79118765432','helga@pisem.net');";

        String sqlUpdate = " UPDATE JC_CLIENTS SET FIRST_NAME = 'Five', LAST_NAME = 'Fifth'  WHERE LAST_NAME = 'Belgy';" +
                "UPDATE JC_CLIENTS SET FIRST_NAME = 'Six', LAST_NAME = 'Sixth' WHERE LAST_NAME = 'Forte'";

        String sqlDelete = "DELETE FROM JC_CLIENTS WHERE FIRST_NAME = 'Zero';";


        try {
            //1. Прописка BD
            //отступы для комментариев никто не отменял
            Class.forName("org.postgresql.Driver");// загрузка класса jdbc который реализует  интерфейс java.sql.Driver
            String url = DB_CONNECTION;  // на каком хосте и на каком порту  работает конкретный экземпляр BD
            String login = DB_USER;
            String password = DB_PASSWORD;

            //2.Подключение к BD

            //Для подключения к базе данных необходимо создать объект java.sql.Connection.
            // Для его создаия применяется метод:
            Connection con =
                    //Для его создаия применяется метод:
                    DriverManager.getConnection(url, login, password);//реальное соединение с конкретным экземпляром СУБД определенного типа
            try {

                //3.Взаимодействия с базой данных.  Чтобы выполнить команду, вначале необходимо
                // создаеть объект Statement.
                Statement stmt =
                        //Для его создания у объекта Connection
                        // вызывается метод createStatement():
                        con.createStatement();
                //Для выполнения команд SQL в классе Statement определено три метода:
                //- executeUpdate: выполняет такие команды,
                // как CREATE TABLE,INSERT, UPDATE, DELETE, DROP TABLE.
                // В качестве результата возвращает количество строк, затронутых операцией

                stmt.executeUpdate(sqlDropTable);
                stmt.executeUpdate(sqlCreateTable);
                stmt.executeUpdate(sqlInsert);
                stmt.executeUpdate(sqlUpdate);
                stmt.executeUpdate(sqlDelete);


                ResultSet rs = stmt.executeQuery("SELECT * FROM JC_CLIENTS");
                while (rs.next()) {
                    String str = rs.getString("jc_clients") + ": " + rs.getString(2) +
                            " " + rs.getString(3) + " /" + rs.getString(4) + "/;";
                    System.out.println("Client №" + str);
                }
                rs.close();
                stmt.close();
            } finally {
                con.close();//закрытие соединения с ВD
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
