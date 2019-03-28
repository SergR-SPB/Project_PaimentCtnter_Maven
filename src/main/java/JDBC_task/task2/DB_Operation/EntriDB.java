package JDBC_task.task2.DB_Operation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

//Подключение к DB
public class EntriDB {

    static Connection connection;


    private static void entriDB() throws SQLException {

        Connection conn = DriverManager.getConnection
                (KeyEntryInDB.getDbConnection(), KeyEntryInDB.getDbUser(), KeyEntryInDB.getDbPassword());
        {
            connection = conn;
        }

    }}
