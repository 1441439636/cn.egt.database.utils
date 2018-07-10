package service.impl;

import dao.impl.RegistDaoImpl;
import entity.DB;
import service.AdminDatabaseService;

/**
 * Created by 14414 on 2017/4/25.
 */
public class AdminDatabaseServiceImpl implements AdminDatabaseService {
    public boolean write(DB db) {
        if (db.isEmpty())
            return false;
        new RegistDaoImpl().write(db);
        return true;
    }

    public DB readDB() {
        return new RegistDaoImpl().read();
    }
}
