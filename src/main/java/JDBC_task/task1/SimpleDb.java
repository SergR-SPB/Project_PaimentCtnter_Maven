package JDBC_task.task1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SimpleDb {

    public static void main(String[] args) {


        SimpleDb m = new SimpleDb();
        m.testDatabase();
    }

    private void testDatabase() {
        String sqlCommand1 = "CREATE TABLE JC_CLIENTS1 (JC_CLIENTS SERIAL,    FIRST_NAME VARCHAR(50) NOT NULL,  LAST_NAME VARCHAR(50) NOT NULL,  BIRTH_DATE DATE NOT NULL, PHONE VARCHAR(50) NOT NULL, EMAIL VARCHAR(50) NOT NULL, PRIMARY KEY (JC_CLIENTS))";

        try {
//1. Прописка BD
            Class.forName("org.postgresql.Driver");// загрузка класса jdbc который реализует  интерфейс java.sql.Driver
            String url = "jdbc:postgresql:" +    //указание с какой BD  работать
                    "//localhost:5432/Clients";  // на каком хосте и на каком порту  работает конкретный экземпляр BD
            String login = "postgres";
            String password = "postgres";

//2.Подключение к BD

            //Для подключения к базе данных необходимо создать объект java.sql.Connection. Для его создаия применяется метод:
            Connection con =
                    //Для его создаия применяется метод:
                    DriverManager.getConnection(url, login, password);//реальное соединение с конкретным экземпляром СУБД определенного типа
            try {

//3.Взаимодействия с базой данных.  Чтобы выполнить команду, вначале необходимо создаеть объект Statement.
                Statement stmt =
                        //Для его создания у объекта Connection вызывается метод createStatement():
                        con.createStatement();
        //Для выполнения команд SQL в классе Statement определено три метода:
            //- executeUpdate: выполняет такие команды, как CREATE TABLE,INSERT, UPDATE, DELETE, DROP TABLE.
                // В качестве результата возвращает количество строк, затронутых операцией
                stmt.executeUpdate(sqlCommand1);









            //- executeQuery: выполняет команду SELECT. Возвращает объект ResultSet, который содержит результаты запроса
                ResultSet rs = stmt.executeQuery("SELECT * FROM JC_CLIENTS1");
                while (rs.next()) {
                    String str = rs.getString("jc_clients") + ":" + rs.getString(2);
                    System.out.println("Client:" + str);
                }
                rs.close();
                stmt.close();
            }finally {
              con.close();//закрытие соединения с ВD
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}