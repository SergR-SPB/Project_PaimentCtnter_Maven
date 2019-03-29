package JDBC_task.task2.DB_Operation;   //нарушение Code Conventions в именовании

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

//Подключение к DB
//имя класса не соответствует его роли
public class EntriDB {

    public static Connection connection;    //нарушение инкапсуляции. иногда можно, но это не тот случай


    public static void entriDB() throws SQLException {  //по имени метода не понятно что он должен делать

        Connection conn = DriverManager.getConnection
                (KeyEntryInDB.getDbConnection(), KeyEntryInDB.getDbUser(), KeyEntryInDB.getDbPassword());
        {
            connection = conn;  //зачем приисаваивание обрамлять в {...}  ?
        }

    }}  //ФОРМАТИРОВАНИЕ!!! Нажимайте Ctrl+Alt+L в IDEA
