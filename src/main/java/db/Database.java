package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 14414 on 2017/4/25.
 */
public interface Database {
Connection createConnection();
    Boolean adminConnect(String logname, String logpass);

    void addRoleAccount(String role, String account);

    int addAccount(String name, String pass);

    boolean hasAccount(String account_name);

    /**
     * 删除账号 删除roleaccount 中的账号 删除 querycondition 中的账号
     *
     * @param account_id
     * @return
     */
    boolean deleteAccount(int account_id);

    boolean serverlog(String logname, String logpass);

    void transTabletoView();

    void createview(String name);

    boolean hasview(String name);

    void dropAllUserTables();

    void createAlluserTables();

    boolean hasAllUserTables();

    /**
     * 读取视图方法 只读取视图，若无视图则创建视图
     */
    ArrayList<String> getUnadornViewList();

    ArrayList<String> getColumnList(String tablename);


    boolean hasTable(String name);

    boolean hasColumn(int table_id, String column_name);

    void setTableName(String name, String chinese);

    String getAdornTableName(String onselecttable);

    String getDataType(String tablename, String column_name);

    /*
     * 更新columnname 到底是使用column_id 还是直接用列名 选择了用id 列名虽然不多但 毕竟有三张表使用 然而并没有卵用
     * 还是改成列名
     */
    // 根据表名列名插入tableid columnid datatype
    void insertColname(String tablename, int table_id,
                       String column_name, String flag, String chinese, int xh);

    boolean updateColname(int table_id, String column_name, String flag,
                          String chinese, int xh);

    // 插入columnname
    // 首先 获取table_id
    // 检测是否之前已经插入列名
    // 存在 直接插入
    // 不存在先插入
    void setColname(String tablename, String colName, String flag,
                    String chinese, int xh);

    ArrayList<String> getRecord(int id, String colname);

    ArrayList<String> getAdornTablenameList();

    // 注意到有一些方法的结构类似 ->使用sql 传出一维的list 是否可以将其抽象为一个方法
    ArrayList<String[]> getRoleList(int type);

    //添加角色 返回角色id 如果存在角色返回-1 其他错误返回-2
    int addRole(String rolename);

    boolean hasRole(String rolename);

    void deleteRole(String rolename);

    /**
     * 删除角色 将角色从角色表中删除
     * 将角色账号表中此角色的账号的角色id 设为-1
     *
     * @param role_id
     */
    void deleteRole(int role_id);

    int updateRole(int role_id, String name);

    ArrayList<String> getAdornColumnList(String tablename);

    void addRolePermission(int role_id, int table_id,
                           ArrayList<String> list);

    int getRoleid(String role_name);

    int getTableid(String table_name);

    void deleteRolePermission(int role_id, int table_id);

    boolean hasRolePermission(int role_id, int table_id, String column);

    ArrayList<String[]> getAllAccount();

    void insertRoleAccount(int role_id, int account_id);

    void updateAccount(int account_id, String name, String pass);

    HashMap<String, String> getAccountByRole(int role_id);

    int getAccountid(String name);

    int getidByunadornname(String onselecttable);

    void deleteRoleAccount(int role_id, int account_id);

    void addRoleAccount(int role_id, int accountId);

    ArrayList<String> getRoleAccountByRoleId(int roleId);

    boolean hasRoleAccount(int role_id, int accountId);

    //user  method
    Boolean userConnect();

    boolean isRootAccount(int account_id);

    ArrayList<String> getColumnList(int account_id, int table_id);

    ResultSet getsetedcollist(String setname, int account_id);

    void setquerycondition(int account_id, int table_id, String setName, String colname, String select, String con1, String con2);

    void deletequerycondition(int account_id, String setname);

    String getcolumnname(int table_id, String colname);


    ArrayList<String> getSetname(int account_id);

    String gettablename(String setname, int account_id);

    ResultSet getresultTable(ArrayList<String> column, ArrayList<String> query, int table_id) throws Exception;

    int logByAccount(String userName, String password);

    ArrayList<String> getTableList(int account_id);

    String[] getquerycondition(int account_id, int table_id, String setname, String columnname);

    ArrayList<String> tranColumnlist(ArrayList<String> column, int table_id);

    String getcolumn(String column, int table_id);

    ArrayList<ArrayList<String>> getGroup(int table_id, String hzx, String hzxsql, String tjx, String tjxsql);

    String getUnadornColumnByadorn(int table_id, String col);

    String getUnadornTablenameById(int table_id);

    ArrayList<Integer> getRoleListByAccount(int account_id);

    boolean hasSetname(int account_id, String name);

    boolean getTypeColumn(String s, int table_id);

    String getColumnName(String s);

    public    ArrayList<ArrayList<String>>  getResultTableList(ArrayList<String[]> columnList, int tableId);
}
