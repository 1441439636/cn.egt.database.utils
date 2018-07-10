package dao.impl;

import entity.DB;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Properties;


public class RegistDaoImpl {
    public void write(DB db) {
        FileWriter fileWriter=null;
        try {
            fileWriter=new FileWriter( new File("regist.properties"),false);
            fileWriter.append("dbtype=");
            fileWriter.append(db.getDbType());
            fileWriter.append("\nname=");
            fileWriter.append(db.getUsername());
            fileWriter.append("\npassword=");
            fileWriter.append(db.getPasswrod());
            fileWriter.append("\naddress=");
            fileWriter.append(db.getAddress());
            fileWriter.append("\ndatabase=");
            fileWriter.append(db.getDatabase());
            fileWriter.close();
        } catch (Exception e) {
        }
    }

    public DB read() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("regist.properties"));
        } catch (Exception e) {
        }
        DB db = new DB();
        db.setDbType(properties.get("dbtype").toString());
        db.setUsername(properties.get("name").toString());
        db.setPasswrod(properties.get("password").toString());
        db.setAddress(properties.get("address").toString());
        db.setDatabase(properties.get("database").toString());
        System.out.println(db.toString());
        return db;
    }

}
