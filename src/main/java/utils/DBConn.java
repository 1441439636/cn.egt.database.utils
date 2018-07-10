package utils;

import dao.impl.RegistDaoImpl;
import db.DBFactory;
import db.Database;
import entity.DB;

/**
 * Created by ZLS on 2017/5/21.
 */
public class DBConn {
    public static Database createDB() {
        //获取数据库
        DB db = new RegistDaoImpl().read();
        return new DBFactory().createDB(db.getDbType());
    }
}
