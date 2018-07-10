package dao.impl;

import entity.DB;

import java.util.prefs.Preferences;


public class RegistDaoImplDemo {
    private final String[] key = {"dbtype", "name", "password", "ip", "databasename"};
    private String dbName = "autoDatabase00001";

    public void write(DB db) {
        Preferences preferences = Preferences.userRoot().node(dbName);
        String[] val = new String[]{db.getDbType(), db.getUsername(), db.getPasswrod(), db.getAddress(), db.getDatabase()};
        for (int i = 0; i < val.length; i++) {
            preferences.put(key[i], val[i]);
        }
    }

    public DB read() {
        DB db = new DB();
        try {
            Preferences preferences = Preferences.userRoot().node(dbName);
            db.setDbType(preferences.get("dbtype", ""));
            db.setUsername(preferences.get("name", ""));
            db.setPasswrod(preferences.get("password", ""));
            db.setAddress(preferences.get("ip", ""));
            db.setDatabase(preferences.get("databasename", ""));
            return db;
        } catch (Exception e) {
            return null;
        }
    }

}
