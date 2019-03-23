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
        String sqlDropTable = "DROP TABLE IF EXISTS JC_CLIENTS";

        String sqlCreateTable = "CREATE TABLE JC_CLIENTS" +
                "(       JC_CLIENTS SERIAL," +
                "        FIRST_NAME VARCHAR(50) NOT NULL," +
                "        LAST_NAME VARCHAR(50) NOT NULL," +
                "        BIRTH_DATE DATE NOT NULL," +
                "        PHONE VARCHAR(50) NOT NULL," +
                "        EMAIL VARCHAR(50) NOT NULL," +
                "        PRIMARY KEY (JC_CLIENTS))";

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
            Class.forName("org.postgresql.Driver");// загрузка класса jdbc который реализует  интерфейс java.sql.Driver
            String url = "jdbc:postgresql:" +    //указание с какой BD  работать
                    "//localhost:5432/Clients";  // на каком хосте и на каком порту  работает конкретный экземпляр BD
            String login = "postgres";
            String password = "postgres";

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
                            " " + rs.getString(3) + " /" + rs.getString(4)+ "/;";
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