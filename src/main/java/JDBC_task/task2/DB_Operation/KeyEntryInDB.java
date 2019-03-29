package JDBC_task.task2.DB_Operation;

//Например, имя DatabaseConstants точнее скажет зачем нужен этот класс
// Ключи для подключения к БД
public class KeyEntryInDB {
    //в EntriDB Вы сделали поле публичным, а тут константы приватные. В чем логика?)
    private static final String DB_CONNECTION = "jdbc:postgresql://localhost:5432/Clients";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres";


    public static String getDbConnection() {
        return DB_CONNECTION;
    }

    public static String getDbUser() {
        return DB_USER;
    }

    public static String getDbPassword() {
        return DB_PASSWORD;
    }
}
