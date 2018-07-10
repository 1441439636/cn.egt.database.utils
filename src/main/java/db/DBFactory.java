package db;

/**
 * Created by ZLS on 2017/5/8.
 */
public class DBFactory {
    public Database createDB(String dbName) {
        if (dbName.equals("SqlServer")) {
            System.out.println("SqlServer=>");
            return new DBSqlServer();
        } else if (dbName.equals("MySql")) {
            System.out.println("MySql=>");
            return new DBMySql();
        } else if (dbName.equals("Oracle")) {
            System.out.println("Oracle=>");
            return new DBOracle();
        }
        return null;
    }
}
