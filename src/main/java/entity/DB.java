package entity;

/**
 * Created by 14414 on 2017/4/27.
 */
public class DB {
    private String dbType;
    private String username;
    private String passwrod;
    private String address;
    private String database;

    public DB() {
        System.out.println( "----------------  DB()   ----------------");
        dbType="MySql";
        this.username = null;
        this.passwrod = null;
        this.address = null;
        this.database = null;
    }

    public DB(String dbType,  String username, String passwrod, String address, String database) {
        this.dbType = dbType;
        this.username = username;
        this.passwrod = passwrod;
        this.address = address;
        this.database = database;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswrod() {
        return passwrod;
    }

    public void setPasswrod(String passwrod) {
        this.passwrod = passwrod;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    @Override
    public String toString() {
        return "[dbType:" + dbType +   ",username:" + username + ",passwrod:" + passwrod + ",address:" + address + ",:database:" + database + "]";
    }

    public boolean isEmpty() {
        if (dbType == null)
            return true;
        if (username == null)
            return true;
        if (passwrod == null)
            return true;
        if (address == null)
            return true;
        if (database == null)
            return true;
        return false;
    }
}
