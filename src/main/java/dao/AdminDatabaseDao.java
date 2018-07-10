package dao;

import entity.DB;

import java.util.List;

/**
 * Created by 14414 on 2017/4/27.
 */
public interface AdminDatabaseDao {

    /**
     * 写入注册表
     *
     * @param db
     */
    void write(DB db);

    /**
     * 读取注册表
     *
     */
    DB read();
}
