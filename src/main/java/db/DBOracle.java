package db;

import dao.impl.RegistDaoImpl;
import entity.DB;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

/*
 * 使用connect 初始化  connect 因为有可能数据库登录连接失败
 * 
 * 
 */
public class DBOracle implements Database {

    private static final String hasAllUserTables = "select count(*) from user_objects where object_name in ('ROLE','ACCOUNT','ROLEACCOUNT','ROLEPERMISSION','TABLENAME','COLUMNNAME','QUERYCONDITION')";
    private final static String isEixtTableName = "select count(*) from tablename where table_name =?";
    private final static String insertTableName = "insert into tablename(table_id,table_name) select object_id,object_name from user_objects where object_name=?";
    private final static String updateTableName = "update tablename set adorn_name= ?,flag=? where table_name= ?";
    private final static String hasAccount = "select count(*) from account where name=?";
    private final static String deleAccount = "delete from account  where account_id=?";
    private final static String getAutoaddNext = "select autoadd.nextval as id from dual";
    private final static String updateRoleAccount = "update roleaccount set role_id=? where account_id=?";
    private final static String insertRoleAccount = "insert into roleaccount values(?,?)";
    private final static String hasView = "select count(*) from all_views where view_name=?";
    private final static String createView = "create or replace view ? as select * from ?";
    private final static String isRootAccount = "select role_name from role where role_id in (select role_id from roleaccount where account_id=?)";
    private final static String getcolumn = "select column_name from  columnname where table_id=? and adorn_name=?";
    private final static String getUnadornColumnByadorn = "select column_name from  columnname where table_id=? and adorn_name=?";
    private Connection con = null;

    @Override
    public Connection createConnection() {
        System.out.println("--------------------     adminConnect           ---------");
        //获取注册表中的存储
        DB dba = new RegistDaoImpl().read();
        String jdbc = "oracle.jdbc.driver.OracleDriver";
//        String url = "jdbc:oracle:" + "thin:@" + dba.getAddress() + ":1521:" + dba.getDatabase();
        String url = "jdbc:oracle:" + "thin:@" + dba.getAddress() + "/" + dba.getDatabase();
        try {
            //获取注册表中的存储
            // 初始化Connection properties
            Class.forName(jdbc);// 加载Oracle驱动程序
            con = DriverManager.getConnection(url, dba.getUsername(), dba.getPasswrod());
            //检测所有表的完整
            if (!hasAllUserTables()) {
                dropAllUserTables();
                createAlluserTables();
                addRole("root");//增加默认root角色 此角色可以查看所有表 此角色的账号是服务端软件的登录账号
                addAccount("admin", "admin");//增加root账号 这就是默认的登录密码
                addRoleAccount("root", "admin");
            }
        } catch (Exception e) {
            System.out.println("链接错误");
        }
        return con;
    }

    // 更新表名 首先看在tablename中是否存在 不存在插入 存在更新
    // 有时候直接写sql语句感觉更方便点

    public Boolean adminConnect(String logname, String logpass) {
        System.out.println("--------------------     adminConnect           ---------");
        //获取注册表中的存储
        DB dba = new RegistDaoImpl().read();
        String jdbc = "oracle.jdbc.driver.OracleDriver";
        String url = "jdbc:oracle:" + "thin:@" + dba.getAddress() + "/" + dba.getDatabase();
        try {
            //获取注册表中的存储
            // 初始化Connection properties
            Class.forName(jdbc);// 加载Oracle驱动程序
            con = DriverManager.getConnection(url, dba.getUsername(), dba.getPasswrod());
            //检测所有表的完整
            if (!hasAllUserTables()) {
                dropAllUserTables();
                createAlluserTables();
                addRole("root");//增加默认root角色 此角色可以查看所有表 此角色的账号是服务端软件的登录账号
                addAccount("admin", "admin");//增加root账号 这就是默认的登录密码
                addRoleAccount("root", "admin");
            }
            //账号不可用
            if (!serverlog(logname, logpass)) return false;
        } catch (Exception e) {
            System.out.println("链接错误");
            return false;
        }
        return true;
    }

    public void addRoleAccount(String roleName, String accountName) {
        int role_id = getRoleid(roleName);
        int account_id = getAccountid(accountName);
        insertRoleAccount(role_id, account_id);
    }

    public int addAccount(String name, String pass) {
        PreparedStatement pre = null;
        try {
            if (hasAccount(name)) return -1;
            int id = getAutoaddNext();
            String addAccount = "insert into account(account_id,name,password) values(?,?,?)";
            pre = con.prepareStatement(addAccount);
            pre.setInt(1, id);
            pre.setString(2, name.trim());
            pre.setString(3, pass.trim());
            pre.executeQuery();
            pre.close();
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
            return -2;
        } finally {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public boolean hasAccount(String account_name) {
        PreparedStatement pre = null;
        try {
            pre = con.prepareStatement(hasAccount);
            pre.setString(1, account_name.trim());
            ResultSet result = pre.executeQuery();
            if (result.next() && result.getInt(1) == 0) {
                result.close();
                pre.close();

                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 删除账号 删除roleaccount 中的账号 删除 querycondition 中的账号
     *
     * @param account_id
     * @return
     */
    public boolean deleteAccount(int account_id) {
        PreparedStatement pre = null;
        try {
            String deleteaccount = "delete from account where account_id=?";
            pre = con.prepareStatement(deleteaccount);
            pre.setInt(1, account_id);
            pre.executeQuery();
            pre.close();
            con.prepareStatement("delete FROM roleaccount where account_id=" + account_id).executeQuery().close();
            con.prepareStatement("delete FROM querycondition where account_id=" + account_id).executeQuery().close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public boolean serverlog(String logname, String logpass) {
        PreparedStatement pre = null;
        String serverlog = "select count(*) from account a,roleaccount ra,role r where a.name=? and a.password=? and a.account_id=ra.account_id and ra.role_id=r.role_id  and r.role_name='root'";
        try {

            pre = con.prepareStatement(serverlog);
            pre.setString(1, logname.trim());
            pre.setString(2, logpass.trim());
            ResultSet result = pre.executeQuery();
            if (result.next() && result.getInt(1) == 1) {
                result.close();
                return true;
            }
            result.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void transTabletoView() {
        System.out.println("--------------------------------www-----");
        try {
            PreparedStatement pre = con.prepareStatement(
                    "select object_name from user_objects where object_type='TABLE' and object_name not in ('ROLE','ACCOUNT','ROLEACCOUNT','ROLEPERMISSION','TABLENAME','COLUMNNAME','QUERYCONDITION') order by object_name");
            ResultSet result = pre.executeQuery();
            while (result.next()) {
                String name = result.getString(1);
                if (!hasview(name + "_VIEW")) {
                    createview(name);
                }
            }
            result.close();
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createview(String name) {
        PreparedStatement pre = null;
        try {
            pre = con.prepareStatement("create or replace view " + name + "_view" + " as select * from " + name);
            pre.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean hasview(String name) {
        PreparedStatement pre = null;
        try {
            pre = con.prepareStatement(hasView);
            pre.setString(1, name);
            ResultSet result = pre.executeQuery();

            if (result.next() && result.getInt(1) == 0) {
                result.close();
                pre.close();
                return false;
            }
            result.close();
            pre.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    //**********************************
    public void dropAllUserTables() {
        String[] usertablename = {"QUERYCONDITION", "COLUMNNAME", "TABLENAME", "ROLEPERMISSION", "ACCOUNT", "ROLEACCOUNT", "ROLE"};
        for (int i = 0; i < usertablename.length; i++) {
            PreparedStatement pre = null;
            try {
                pre = con.prepareStatement("drop table " + usertablename[i]);
                pre.executeQuery();
                pre.close();
            } catch (Exception e) {
            } finally {
                try {
                    pre.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }//无视删除表时引起的找不到表的异常
        }
        PreparedStatement pre = null;
        try {
            pre = con.prepareStatement("drop sequence autoadd ");
            pre.executeQuery();
            pre.close();
        } catch (SQLException e) {
        } finally {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //*****************************************************
    public void createAlluserTables() {
        String[] createUserTableSql = {
                "create table role(role_id number primary key,role_name varchar2(30) not null unique)",//角色表存放角色id 自己分配  角色名 角色名不能为空不能重复
                "create table account(account_id number  primary key,name varchar2(30) not null UNIQUE,password varchar2(30) not null)",//账号表 存放账号 id 自己分配 账号名 账号密码 账号名不能为空不能重复 密码不能为空
                "create table roleaccount(account_id number,role_id number)",// 角色 账号对应表 存放 角色id 账号id
                "create table rolepermission(role_id number ,table_id number not null,column_name varchar2(30) not null)",//角色 权限表 角色id 表id 表的列名
                "create table tablename(table_id number primary key,adorn_name varchar(30) unique,table_name varchar(30),flag char(1) default 'N')",//表名 表id 数据库分配 翻译名  不能相同  表名 是否可见 默认不可见
                "create table columnname(table_id number not null,column_name varchar2(30) not null,datatype varchar(106),adorn_name varchar(30),flag char(1) default 'N',no number default 0)",//列名 表id 列名 翻译列名 数据类型 翻译名 是否可见
                "create table querycondition(account_id number ,table_id number not null,column_name varchar2(30) not null,flag varchar(1),con1 varchar(30),con2 varchar(30),setname varchar(30) not null)",//保存的设置  账号id 表id 列名   是否被选中  查询条件1 查询条件2  设置名
                "CREATE SEQUENCE autoadd INCREMENT BY 1   START WITH 1    NOMAXVALUE  NOCYCLE "//序列 分配用
        };
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            for (int i = 0; i < createUserTableSql.length; i++) {
                stmt.addBatch(createUserTableSql[i]);
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean hasAllUserTables() {
        PreparedStatement pre = null;
        try {
            pre = con.prepareStatement(hasAllUserTables);
            ResultSet result = pre
                    .executeQuery();
            if (result.next() && result.getInt(1) == 7) {
                result.close();
                pre.close();
                System.out.println("完整");
                return true;
            }
            result.close();
            pre.close();
            System.out.println("不完整");
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取视图方法 只读取视图，若无视图则创建视图
     */
    //*******************************
    public ArrayList<String> getUnadornViewList() {
        transTabletoView();
        PreparedStatement pre = null;
        ArrayList<String> tablename = new ArrayList<String>();
        try {
            pre = con.prepareStatement("select object_name from user_objects where object_type='VIEW'  order by object_name");
            ResultSet result = pre
                    .executeQuery();
            while (result.next()) {
                tablename.add(result.getString(1));
            }
            result.close();
            pre.close();
            return tablename;
        } catch (SQLException e) {
            e.printStackTrace();
            tablename.clear();
            return tablename;
        } finally {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //获得表的所有字段信息
    public ArrayList<String> getColumnList(String tablename) {
        ArrayList<String> list = new ArrayList<String>();
        PreparedStatement pre = null;
        try {
            String getUnadornedColumnListSql = "select column_name from user_tab_cols where table_name=?";

            pre = con.prepareStatement(getUnadornedColumnListSql);
            pre.setString(1, tablename.trim());
            ResultSet result = pre.executeQuery();
            while (result.next()) {
                list.add(result.getString(1));
            }
            result.close();
            pre.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return list;
        } finally {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean hasTable(String name) {
        PreparedStatement pre = null;
        try {
            pre = con.prepareStatement(isEixtTableName);
            pre.setString(1, name.trim());
            ResultSet result = pre.executeQuery();
            boolean flag = result.next() && result.getInt(1) == 1;
            result.close();
            return flag;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean hasColumn(int table_id, String column_name) {
        PreparedStatement pre = null;
        try {
            String isEixtColumnName = "select count(*)  from columnname  where  table_id=? and column_name=?";
            pre = con.prepareStatement(isEixtColumnName);
            pre.setInt(1, table_id);
            pre.setString(2, column_name.trim());
            ResultSet result = pre.executeQuery();
            boolean flag = result.next() && result.getInt(1) == 1;
            result.close();
            pre.close();
            System.out.println("---------------    hasColumn   ---------------  return " + flag);
            return flag;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println("---------------    hasColumn   ---------------  return  false");
        return false;
    }

    public void setTableName(String name, String chinese) {

        String haschiese = chinese.equals("") ? "N" : "Y";
        boolean isExit = hasTable(name);
        PreparedStatement pre = null;
        try {
            if (isExit) {
                pre = con.prepareStatement(updateTableName);

                pre.setString(1, chinese.trim());
                pre.setString(2, haschiese.trim());
                pre.setString(3, name.trim());
                pre.executeQuery();

            } else {
                pre = con.prepareStatement(insertTableName);
                pre.setString(1, name.trim());
                pre.executeQuery();
                pre = con.prepareStatement(updateTableName);
                pre.setString(1, chinese.trim());
                pre.setString(2, haschiese.trim());
                pre.setString(3, name.trim());
                pre.executeQuery();
            }
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //获取表的翻译值
    public String getAdornTableName(String onselecttable) {
        String str = "";
        PreparedStatement pre = null;
        try {

            String getAdornTableName = "select adorn_name from tablename where table_name=?";

            pre = con.prepareStatement(getAdornTableName);
            pre.setString(1, onselecttable.trim());
            pre.executeQuery();
            ResultSet result = pre.executeQuery();
            if (result.next()) {
                str = result.getString(1);
            }
            result.close();
            pre.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return str;
        } finally {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return str;
    }

    public String getDataType(String tablename, String column_name) {
        String str = "";
        PreparedStatement pre = null;
        try {

            String getDataType = "select data_type from user_tab_cols where table_name=? and column_name=? ";
            pre = con.prepareStatement(getDataType);
            pre.setString(1, tablename.trim());
            pre.setString(2, column_name.trim());
            pre.executeQuery();
            ResultSet result = pre.executeQuery();
            if (result.next()) {
                str = result.getString(1);
            }
            result.close();
            pre.close();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("--------------------    getDataType  fail    -----------");
            return str;
        } finally {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println("--------------------    getDataType   ----------tablename=" + tablename + "    column_name=" + column_name + "-------- type=" + str);
        return str;

    }

    /*
     * 更新columnname 到底是使用column_id 还getRecord是直接用列名 选择了用id 列名虽然不多但 毕竟有三张表使用 然而并没有卵用
     * 还是改成列名
     */
    // 根据表名列名插入tableid columnid datatype
    public void insertColname(String tablename, int table_id,
                              String column_name, String flag, String chinese, int xh) {
        System.out.println("----------------   insertColname     ---------------------");
        String datatype = getDataType(tablename, column_name);
        PreparedStatement pre = null;
        String insertColName = "insert into columnname(table_id,column_name,adorn_name,datatype,flag,no) values(?,?,?,?,?,?)";
        try {
            pre = con.prepareStatement(insertColName);
            pre.setInt(1, table_id);
            pre.setString(2, column_name.trim());
            pre.setString(3, chinese.trim());
            pre.setString(4, datatype.trim());
            pre.setString(5, flag.trim());
            pre.setInt(6, xh);
            pre.executeQuery();
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public boolean updateColname(int table_id, String column_name, String flag,
                                 String chinese, int xh) {
        System.out.println("----------------   updateColname     ---------------------  column_name=" + column_name);
        PreparedStatement pre = null;
        String updateColName = "update columnname set adorn_name= ?, flag = ? , no= ? where table_id = ? and column_name=?";
        try {
            pre = con.prepareStatement(updateColName);
            pre.setString(1, chinese.trim());
            pre.setString(2, flag.trim());
            pre.setInt(3, xh);
            pre.setInt(4, table_id);
            pre.setString(5, column_name.trim());
            pre.executeQuery();
            pre.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;

    }

    // 插入columnname
    // 首先 获取table_id
    // 检测是否之前已经插入列名
    // 存在 直接插入
    // 不存在先插入
    public void setColname(String tablename, String colName, String flag,
                           String chinese, int xh) {
        System.out.println("----------------   setColname     --------------------- colName=" + colName);
        int table_id = 0;
        PreparedStatement pre = null;
        try {
            pre = con.prepareStatement("select table_id from tablename where table_name=? ");
            pre.setString(1, tablename.trim());
            ResultSet result = pre.executeQuery();
            if (result.next()) {
                table_id = result.getInt(1);
                result.close();
            } else {
                result.close();
                return;
            }
            pre.close();
            boolean isExit = hasColumn(table_id, colName);
            if (isExit) {
                updateColname(table_id, colName, flag, chinese, xh);
            } else {
                insertColname(tablename, table_id, colName, flag, chinese, xh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }

    /**
     * 获取字段的使用情况、字段翻译、字段排列
     *
     * @param id
     * @param colname
     * @return
     */
    public ArrayList<String> getRecord(int id, String colname) {
        System.out.println("--------------------------    getRecord    ---------------------  id=" + id + "   colname=" + colname);
        ArrayList<String> set = null;
        PreparedStatement pre = null;
        try {
            String getRecordSql =
                    "select c.flag,c.adorn_name,c.no from columnname c where c.table_id =? and c.column_name=?";
            pre = con.prepareStatement(getRecordSql);
            pre.setInt(1, id);
            pre.setString(2, colname.trim());
            ResultSet result = pre.executeQuery();
            if (result.next()) {
                set = new ArrayList<String>();
                set.add(result.getString(1));
                set.add(result.getString(2));
                set.add(result.getString(3));
            }
            result.close();
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return set;
    }

    public ArrayList<String> getAdornTablenameList() {
        System.out.println("-----------------------  getAdornTablenameList   ------------------------------");
        ArrayList<String> list = new ArrayList<String>();
        PreparedStatement pre = null;
        try {
            String getAdornTablenameList = "select adorn_name from tablename where flag='Y'";
            pre = con.prepareStatement(getAdornTablenameList);
            ResultSet result = pre.executeQuery();
            while (result.next()) {
                list.add(result.getString(1));
            }
            pre.close();
            result.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return list;
        } finally {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // 注意到有一些方法的结构类似 ->使用sql 传出一维的list 是否可以将其抽象为一个方法
    //type=0  不查询root
    public ArrayList<String[]> getRoleList(int type) {
        System.out.println("------------------------- getRoleList     ----------------------type=" + type);
        ArrayList<String[]> list = new ArrayList<String[]>();
        PreparedStatement pre = null;
        try {
            String getRoleList = "select role_id,role_name from role";
            String s;
            switch (type) {
                case 0:
                    s = getRoleList + " where not role_name='root'";
                    break;
                default:
                    s = getRoleList;
                    break;
            }
            pre = con.prepareStatement(s);
            ResultSet result = pre.executeQuery();
            while (result.next()) {
                String[] kv = {result.getString(1), result.getString(2)};
                list.add(kv);
            }
            result.close();
            pre.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return list;
        } finally {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //添加角色 返回角色id 如果存在角色返回-1 其他错误返回-2
    public int addRole(String rolename) {
        System.out.println("------------------------- addRole     ----------------------rolename=" + rolename);
        boolean hasRole = hasRole(rolename);
        PreparedStatement pre = null;
        try {
            String addRole = "insert into role values(?,?)";
            if (hasRole) return -1;
            int id = getAutoaddNext();
            pre = con.prepareStatement(addRole);
            pre.setInt(1, id);
            pre.setString(2, rolename.trim());
            pre.executeQuery();
            pre.close();
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
            return -2;
        } finally {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean hasRole(String rolename) {
        System.out.println("------------------------- hasRole     ----------------------rolename=" + rolename);
        PreparedStatement pre = null;
        try {
            String hasrole = "select count(*) from role where role_name=?";
            pre = con.prepareStatement(hasrole);
            pre.setString(1, rolename.trim());
            ResultSet result = pre.executeQuery();
            if (result.next() && result.getInt(1) == 0) {
                result.close();
                pre.close();
                return false;
            }
            result.close();
            pre.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    public void deleteRole(String rolename) {
        PreparedStatement pre = null;
        try {
            String deleteRole = "delete from role where role_name=?";
            pre = con.prepareStatement(deleteRole);
            pre.setString(1, rolename.trim());
            pre.executeQuery();
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除角色 将角色从角色表中删除
     * 将角色账号表中此角色的账号的角色id 设为-1
     *
     * @param role_id
     */
    public void deleteRole(int role_id) {
        System.out.println("---------------------------------      deleteRole         ---------------------------------           ");
        try {
            PreparedStatement pre1 = con.prepareStatement("delete from role where role_id= " + role_id);
            pre1.executeQuery().close();
            pre1.close();
            PreparedStatement pre2 = con.prepareStatement("update roleaccount set role_id=-1 where role_id= " + role_id);
            pre2.executeQuery().close();
            pre2.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int updateRole(int role_id, String name) {
        System.out.println("---------------------------------      updateRole         ---------------------------------      name=" + name);
        PreparedStatement pre = null;
        try {
            pre = con.prepareStatement("update role set role_name= '" + name + "' where role_id= " + role_id);
            pre.executeQuery();
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 1;
    }

    //获得所有翻译过的字段
    public ArrayList<String> getAdornColumnList(String table_adorn_name) {
        ArrayList<String> list = new ArrayList<String>();
        PreparedStatement pre = null;
        try {
            String getAdornColumnList = "select adorn_name from columnname where flag='Y' and table_id in (select table_id from tablename where adorn_name=?)";
            pre = con.prepareStatement(getAdornColumnList);
            pre.setString(1, table_adorn_name.trim());
            ResultSet result = pre.executeQuery();
            while (result.next()) {
                list.add(result.getString(1));
            }
            result.close();
            pre.close();

            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return list;
        } finally {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int getRoleid(String role_name) {
        System.out.println("-----------------------      getRoleid      role_name=" + role_name);
        PreparedStatement pre = null;
        try {
            String getRoleid = "select role_id from role where role_name=?";

            pre = con.prepareStatement(getRoleid);
            pre.setString(1, role_name.trim());
            ResultSet result = pre.executeQuery();
            int i = -1;
            if (result.next()) {
                i = result.getInt(1);
            }
            result.close();
            pre.close();
            return i;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int getTableid(String adorn_table_name) {
        int i = -1;
        PreparedStatement pre = null;
        ResultSet result = null;
        try {
            pre = con.prepareStatement("select table_id from tablename where adorn_name=?");
            pre.setString(1, adorn_table_name.trim());
            result = pre.executeQuery();
            if (result.next()) {
                i = result.getInt(1);
            }
            result.close();
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pre != null) {
                    pre.close();
                }
                if (result != null) {
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return i;
    }

    public void addRolePermission(int role_id, int table_id,
                                  ArrayList<String> adorn_nameList) {
        System.out.println("------------------------       addRolePermission   -----------------------       ");
        PreparedStatement pre = null;
        try {
            String addRolePermission = "insert into rolepermission(role_id,table_id,column_name) select r.role_id ,c.table_id,c.column_name from columnname c,role r  where r.role_id=? and  c.table_id=? and  c.adorn_name=?";
            pre = con.prepareStatement(addRolePermission);
            for (int i = 0; i < adorn_nameList.size(); i++) {
                pre.setInt(1, role_id);
                pre.setInt(2, table_id);
                pre.setString(3, adorn_nameList.get(i).trim());
                pre.executeQuery();
            }
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteRolePermission(int role_id, int table_id) {
        PreparedStatement pre = null;
        try {
            String deleteRolePermission = "delete from rolepermission where role_id =? and  table_id=?";
            pre = con.prepareStatement(deleteRolePermission);
            pre.setInt(1, role_id);
            pre.setInt(2, table_id);
            pre.executeQuery();
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //查询某字段是否属于某角色
    public boolean hasRolePermission(int role_id, int table_id, String adorn_name) {
        PreparedStatement pre = null;
        String hasRolePermission = "select count(*) from rolepermission r where r.role_id=? and r.table_id=? and r.column_name in (select t.column_name from columnname t where t.table_id=? and t.adorn_name=?)";
        try {
            pre = con.prepareStatement(hasRolePermission);
            pre.setInt(1, role_id);
            pre.setInt(2, table_id);
            pre.setInt(3, table_id);
            pre.setString(4, adorn_name.trim());
            ResultSet result = pre.executeQuery();
            boolean flag = result.next() && result.getInt(1) == 1;
            result.close();
            pre.close();
            return flag;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public ArrayList<String[]> getAllAccount() {
        ArrayList<String[]> list = new ArrayList<String[]>();
        String s;
        ResultSet result;
        PreparedStatement pre = null;
        try {
            String getAllAccount = "select a.account_id, a.name,a.password  from account a";
            pre = con.prepareStatement(getAllAccount);
            result = pre.executeQuery();
            while (result.next()) {
                String[] acc = new String[3];
                acc[0] = result.getString(1);
                acc[1] = result.getString(2);
                acc[2] = result.getString(3);
                list.add(acc);
            }

            result.close();
            pre.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return list;
        } finally {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void insertRoleAccount(int role_id, int account_id) {
        PreparedStatement pre = null;
        try {
            pre = con.prepareStatement(insertRoleAccount);
            pre.setInt(1, account_id);
            pre.setInt(2, role_id);
            pre.executeQuery();
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateAccount(int account_id, String name, String pass) {
        System.out.println("--------------------     updateAccount       --------------------      account_id=" + account_id + "  name=" + name + "  pass=" + pass);
        String updateAccount = "update account set name=?,password=? where account_id=?";
        PreparedStatement pre = null;
        try {
            pre = con.prepareStatement(updateAccount);
            pre.setString(1, name.trim());
            pre.setString(2, pass.trim());
            pre.setInt(3, account_id);
            pre.execute();
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public HashMap<String, String> getAccountByRole(int role_id) {
        HashMap<String, String> list = new HashMap<String, String>();
        PreparedStatement pre = null;
        try {
            String getAccountByRoleid = "select a.account_id ,a.name,a.password  from account a where a.account_id in (select account_id from roleaccount where role_id =?)";

            pre = con.prepareStatement(getAccountByRoleid);
            pre.setInt(1, role_id);
            ResultSet result = pre.executeQuery();
            while (result.next()) {
                list.put(result.getString(2), "");
                String[] acc = new String[3];
                acc[0] = result.getString(1);
                acc[1] = result.getString(2);
                acc[2] = result.getString(3);
            }
            result.close();
            pre.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return list;
        } finally {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public int getAccountid(String accountName) {
        PreparedStatement pre = null;
        try {
            String getAccountid = "select account_id from account where name=?";

            pre = con.prepareStatement(getAccountid);
            pre.setString(1, accountName.trim());
            ResultSet result = pre.executeQuery();
            int i = -1;
            if (result.next()) {
                i = result.getInt(1);
            }

            result.close();
            pre.close();
            return i;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    //获取表的id值
    public int getidByunadornname(String onselecttable) {
        PreparedStatement pre = null;
        try {

            String getidByunadornname = "select table_id from tablename where table_name=? ";
            pre = con.prepareStatement(getidByunadornname);
            pre.setString(1, onselecttable.trim());
            ResultSet result = pre.executeQuery();
            int i = -1;
            if (result.next()) {
                i = result.getInt(1);
            }
            result.close();
            pre.close();
            return i;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteRoleAccount(int role_id, int account_id) {
        System.out.println("-----------------       deleteRoleAccount          --------------------------");
        PreparedStatement pre = null;
        try {
            String deleRoleAccount = "delete FROM roleaccount where role_id=? and account_id=?";

            pre = con.prepareStatement(deleRoleAccount);
            pre.setInt(1, role_id);
            pre.setInt(2, account_id);
            pre.executeQuery();
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void addRoleAccount(int role_id, int accountId) {
        System.out.println("-----------------       addRoleAccount          --------------------------");
        PreparedStatement pre = null;
        try {
            String addRoleAccount = "insert into roleaccount(role_id,account_id) values(?,?)";

            pre = con.prepareStatement(addRoleAccount);
            pre.setInt(1, role_id);
            pre.setInt(2, accountId);
            pre.executeQuery();
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ArrayList<String> getRoleAccountByRoleId(int roleId) {
        ArrayList<String> list = new ArrayList<String>();
        PreparedStatement pre = null;
        try {
            pre = con.prepareStatement("select account_id from roleaccount where role_id =" + roleId);
            ResultSet result = pre.executeQuery();
            while (result.next()) {
                list.add(result.getString(1));
            }
            result.close();
            pre.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return list;
        } finally {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean hasRoleAccount(int role_id, int account_id) {
        System.out.println("--------------------     hasRoleAccount   ---------------------  role_id=" + role_id + "  account_id=" + account_id);
        PreparedStatement pre = null;
        try {
            pre = con.prepareStatement("select count(*) from roleaccount where role_id =? and account_id=?");
            pre.setInt(1, role_id);
            pre.setInt(2, account_id);
            ResultSet result = pre.executeQuery();
            boolean flag = result.next() && result.getInt(1) == 1;
            result.close();
            pre.close();
            return flag;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取序列
     *
     * @return
     * @throws SQLException
     */
    public int getAutoaddNext() {
        PreparedStatement pre = null;
        try {
            pre = con.prepareStatement(getAutoaddNext);
            ResultSet result = pre.executeQuery();
            result.next();
            int i = result.getInt(1);
            result.close();
            pre.close();
            return i;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    //user method
    public Boolean userConnect() {
        System.out.println(" --------------------      connect    DBOracle      -------------------------");
//        获取注册列表
        DB dba = new RegistDaoImpl().read();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");// 加载Oracle驱动程序
            String url = "jdbc:oracle:" + "thin:@" + dba.getAddress() + ":1521:" + dba.getDatabase();// 127.0.0.1是本机地址， orcl是你的数据库名
            System.out.println("url: ====>" + url);
            Properties common = new Properties();
            con = DriverManager.getConnection(url, dba.getUsername(), dba.getPasswrod());
            if (!con.prepareStatement("select table_name from user_tables where table_name='QUERYCONDITION'").executeQuery().next()) {
                JOptionPane.showMessageDialog(null, "数据库错误", "+_+", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean isRootAccount(int account_id) {
        System.out.println(" --------------------      isRootAccount      -------------------------account_id=" + account_id);
        PreparedStatement pre = null;
        ResultSet result = null;
        try {
            pre = con.prepareStatement(isRootAccount);

            pre.setInt(1, account_id);

            result = pre.executeQuery();
            if (result.next() && result.getString(1).equals("root")) return true;
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                pre.close();
                result.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<String> getColumnList(int account_id, int table_id) {
        System.out.println(" --------------------      getColumnList      -------------------------account_id=" + account_id + "  table_id=" + table_id);
        PreparedStatement pre = null;
        ResultSet result = null;
        ArrayList<String> list = new ArrayList<String>();
        try {
            String getcollistSql = "select c.adorn_name from columnname c where c.table_id=? and c.column_name in (select r.column_name from rolepermission r where r.role_id in (select role_id from roleaccount where account_id=?) and r.table_id=?) order by c.no";
            pre = con.prepareStatement(getcollistSql);
            pre.setInt(1, table_id);
            pre.setInt(2, account_id);
            pre.setInt(3, table_id);
            result = pre.executeQuery();
            while (result.next()) {
                list.add(result.getString(1));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return list;
        } finally {
            try {
                pre.close();
                result.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    //需要处理
    public ResultSet getsetedcollist(String setname, int account_id) {
        System.out.println(" --------------------      getsetedcollist      ------------------------- setname=" + setname);
        PreparedStatement pre = null;
        ResultSet rs = null;
        String getsetedcollistSql = "select focus,colname,con1,con2 from querycondition where querycondition.setname=? and account_id=? ";
        try {
            pre = con.prepareStatement(getsetedcollistSql);
            pre.setString(1, setname.trim());
            pre.setInt(2, account_id);
            rs = pre.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pre.close();
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rs;
    }

//    public String getEnglishTablenameById(int table_id) {
//        System.out.println("-------------------   getEnglishTablenameById   ----------------  table_id=" + table_id + "  ");
//        String trSql = "select table_name from tablename where table_id='" + table_id + "'";
//        PreparedStatement pre = null;
//        ResultSet rs = null;
//        try {
//            pre = con.prepareStatement(trSql);
//            rs = pre.executeQuery();
//            if (rs.next())
//                return (String) rs.getString(1);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                pre.close();
//                rs.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }

    public void setquerycondition(int account_id, int table_id, String setName, String adorn_column_name, String flag, String con1, String con2) {
        System.out.print("-------------------   setquerycondition   ----------------  account_id=" + account_id +
                "  table_id=" + table_id + " " +
                " setName=" + setName + "  " +
                " adorn_column_name=" + adorn_column_name + "   " +
                "flag=" + flag + "   " +
                "con1=" + con1 + "  " +
                "con2=" + con2 + "  ");


        PreparedStatement pre = null;
        String insertintoqueryconditionSql = "insert into querycondition(account_id,table_id,setname,column_name,flag,con1,con2)"
                + "values(?,?,?,?,?,?,?)";
        try {
            //insert into querycondition(account_id,table_id,setname,column_name,flag,con1,con2)"
            String column_name = getcolumnname(table_id, adorn_column_name);
            System.out.println("  column_name=" + column_name);
            pre = con.prepareStatement(insertintoqueryconditionSql);
//			String sql="insert into querycondition(account_id,table_id,setname,column_name,flag,con1,con2)"
//		    		+"values("+account_id+","+table_id+","+"'"+setName+"',"+"'"+colname+"',"+"'"+select+"',"+"'"+con1+"',"+"'"+con2+"')";
//			pre=con.prepareStatement(sql);
            pre.setInt(1, account_id);
            pre.setInt(2, table_id);
            pre.setString(3, setName.trim());
            pre.setString(4, column_name.trim());
            pre.setString(5, flag.trim());
            pre.setString(6, con1.trim());
            pre.setString(7, con2.trim());
            pre.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deletequerycondition(int account_id, String setname) {
        System.out.println("-------------------   deletequerycondition   ----------------  account_id=" + account_id);
        PreparedStatement pre = null;
        try {
            String deletequerycondition = "delete from querycondition where account_id=? and setname=?";
            pre = con.prepareStatement(deletequerycondition);
            pre.setInt(1, account_id);
            pre.setString(2, setname.trim());
            pre.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public String getcolumnname(int table_id, String colname) {
        System.out.println("-------------------   getcolumnname   ----------------  table_id=" + table_id + "  colname=" + colname);
        ResultSet result = null;
        PreparedStatement pre = null;
        String getcolumnname = "select column_name from columnname where table_id=? and adorn_name=?";
        try {
            pre = con.prepareStatement(getcolumnname);
            pre.setInt(1, table_id);
            pre.setString(2, colname.trim());
            result = pre.executeQuery();
            if (result.next())
                return result.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pre.close();
                result.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public ArrayList<String> getSetname(int account_id) {
        System.out.println("-------------------   getSetname   ----------------  account_id=" + account_id);
        PreparedStatement pre = null;
        ResultSet rs = null;
        ArrayList<String> list = new ArrayList<String>();
        try {
            pre = con.prepareStatement("select distinct setname from querycondition where account_id=?");
            pre.setInt(1, account_id);
            rs = pre.executeQuery();
            while (rs.next()) {
                list.add(rs.getString(1));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return list;
        } finally {
            try {
                pre.close();
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public String gettablename(String setname, int account_id) {
        System.out.println("-------------------   gettablename   ---------------- setname=" + setname + "   account_id=" + account_id);
        ResultSet result = null;
        PreparedStatement pre = null;
        try {
            String gettablenameSql = "select adorn_name from tablename where table_id in (select distinct table_id from querycondition where setname=? and account_id=?)";
            pre = con.prepareStatement(gettablenameSql);
            pre.setString(1, setname.trim());
            pre.setInt(2, account_id);
            result = pre.executeQuery();
            if (result.next()) return result.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pre.close();
                result.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //列名 t 对应列的查询条件query 表名 tablename
    //TODO 这里有很多种方法
    //
    public ResultSet getresultTable(ArrayList<String> column, ArrayList<String> query, int table_id) throws Exception {
        System.out.println("-------------------   getresultTable   ----------------  table_id=" + table_id);

        String tableName = this.getUnadornTablenameById(table_id);

        StringBuilder sql = new StringBuilder("select ");
        int size = query.size();
        ArrayList<String> columnList = new ArrayList<String>();
        for (int i = 0; i < query.size(); i++) {
            columnList.add(this.getcolumn(column.get(i), table_id));
        }


        for (int i = 0; i < size - 1; i++) {
            sql.append(columnList.get(i) + ",");
        }
        sql.append(columnList.get(size - 1) + " from " + tableName);
        boolean isStart = true;
        for (int i = 0; i < size; i++) {
            if (!query.get(i).equals("")) {
                if (isStart) {
                    sql.append(" where " + columnList.get(i) + " " + query.get(i));
                    isStart = false;
                } else {
                    sql.append(" and " + columnList.get(i) + " " + query.get(i));
                }
            }
        }
        return con.prepareStatement(sql.toString()).executeQuery();
    }

    /**
     * 查询结果
     *
     * @param columnList
     * @param tableId
     */
    public ArrayList<ArrayList<String>> getResultTableList(ArrayList<String[]> columnList, int tableId) {
        System.out.println("-------------------   getResultTableList   ----------------  tableId=" + tableId);
        String tableName = this.getUnadornTablenameById(tableId);
        StringBuilder sql = new StringBuilder("select ");
        for (int i = 0; i < columnList.size() - 1; i++) {
            sql.append(columnList.get(i)[0] + ",");
        }
        sql.append(columnList.get(columnList.size() - 1)[0] + " from " + tableName);
        boolean isStart = true;
        for (String[] column : columnList) {
            if (!column[1].trim().equals("")) {
                if (isStart) {
                    sql.append(" where " + column[1]);
                    isStart = false;
                } else {
                    sql.append(" AND " + column[1]);
                }
            }
        }
        System.out.println("。。。。。。。。。。。。。。。。。。。sql.toString()=" + sql.toString());
        PreparedStatement pre = null;
        ResultSet rs = null;
        ArrayList<ArrayList<String>> resultList = new ArrayList<ArrayList<String>>();
        try {
            pre = con.prepareStatement(sql.toString());
            rs = pre.executeQuery();
            while (rs.next()) {
                ArrayList<String> result = new ArrayList<String>();
                for (int i = 0; i < columnList.size(); i++) {
                    result.add(rs.getString(i + 1));
                }
                resultList.add(result);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pre.close();
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return resultList;
    }

    public int logByAccount(String userName, String password) {
        System.out.println("-------------------   logByAccount   ----------------  userName=" + userName + "   password=" + password);
        ResultSet result = null;
        PreparedStatement pre = null;
        int id;
        try {
            pre = con.prepareStatement("select account_id from account where name=? and password=?");
            pre.setString(1, userName.trim());
            pre.setString(2, password.trim());
            result = pre.executeQuery();
            if (result.next()) {
                id = result.getInt(1);
                return id;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pre != null) {
                    pre.close();
                }
                if (result != null) {
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public ArrayList<String> getTableList(int account_id) {
        System.out.println("-------------------   getTableList   ----------------  account_id=" + account_id);
        boolean flag = isRootAccount(account_id);
        PreparedStatement pre = null;
        ArrayList<String> list = new ArrayList<String>();
        try {
            if (flag) {
                pre = con.prepareStatement("select adorn_name from tablename");
                ResultSet result = pre.executeQuery();
                while (result.next()) {
                    list.add(result.getString(1));
                }
                result.close();
            } else {
                String getTablelist = "select distinct adorn_name from tablename where table_id in (select  distinct table_id from rolepermission where role_id in (select role_id from roleaccount where account_id=?))";
                pre = con.prepareStatement(getTablelist);
                pre.setInt(1, account_id);
                ResultSet result = pre.executeQuery();
                while (result.next()) {
                    list.add(result.getString(1));
                }
                result.close();
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return list;
        } finally {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public String[] getquerycondition(int account_id, int table_id, String setname, String columnname) {
        System.out.println("-------------------   getquerycondition   ----------------  account_id=" + account_id + "  table_id=" + table_id + "  setname= " + setname + "   columnname= " + columnname);
        PreparedStatement pre = null;
        ResultSet result = null;
        String[] set = {"N", "", ""};
        try {
            String getquerycondition = "select flag,con1,con2 from  querycondition where account_id=? and table_id=? and column_name=? and setname=?";
            String column_name = getcolumnname(table_id, columnname);
            pre = con.prepareStatement(getquerycondition);
            pre.setInt(1, account_id);
            pre.setInt(2, table_id);
            pre.setString(3, column_name.trim());
            pre.setString(4, setname.trim());
            result = pre.executeQuery();
            if (result.next()) {
                set[0] = result.getString(1);
                set[1] = result.getString(2);
                set[2] = result.getString(3);
                //	System.out.println("get");
            }
            return set;
        } catch (SQLException e) {
            e.printStackTrace();
            return set;
        } finally {
            try {
                pre.close();
                result.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<String> tranColumnlist(ArrayList<String> column, int table_id) {
        System.out.println("-------------------   tranColumnlist   ----------------  table_id=" + table_id);
        for (int i = 0; i < column.size(); i++) {
            column.set(i, getcolumn(column.get(i), table_id));
        }
        return column;
    }

    public String getcolumn(String column, int table_id) {
        System.out.println("-------------------   getcolumn   ----------------  column=" + column + " table_id=" + table_id);
        ResultSet result = null;
        PreparedStatement pre = null;
        try {
            pre = con.prepareStatement(getcolumn);
            pre.setInt(1, table_id);
            pre.setString(2, column.trim());
            result = pre.executeQuery();
            if (result.next()) {
                return result.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pre.close();
                result.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return "wrong";
    }

    public ArrayList<ArrayList<String>> getGroup(int table_id, String hzx, String hzxsql, String tjx, String tjxsql) {
        System.out.println("-------------------   getGroup   ----------------  table_id=" + table_id + " hzx= " + hzx + " hzxsql= " + hzxsql + " tjx= " + tjxsql + " tjx= " + tjxsql);

        String tablename = getUnadornTablenameById(table_id);
        String uhzx = getUnadornColumnByadorn(table_id, hzx);
        String utjx = getUnadornColumnByadorn(table_id, tjx);
        String wheresql;
        if (hzxsql.equals("") && tjxsql.equals("")) {
            wheresql = "";
        } else if (!hzxsql.equals("") && tjxsql.equals("")) {
            wheresql = "where" + hzx + " " + hzxsql;
        } else if (hzxsql.equals("") && !tjxsql.equals("")) {
            wheresql = "where" + tjx + " " + tjxsql;
        } else {
            wheresql = "where" + hzx + " " + hzxsql + " and " + tjx + " " + tjxsql;
        }
        String sql = "select " + utjx + ", sum(" + uhzx + ") as " + hzx + "统计" + " from " + tablename + wheresql + " group by " + utjx;
        ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
        ResultSet result = null;
        PreparedStatement pre = null;
        try {
            pre = con.prepareStatement(sql);
            result = pre.executeQuery();
            while (result.next()) {
                ArrayList<String> temp = new ArrayList<String>();
                temp.add(result.getString(1));
                temp.add(result.getString(2));
                data.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pre.close();
                result.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    public String getUnadornColumnByadorn(int table_id, String col) {
        System.out.println("----------------------------   getUnadornColumnByadorn     ---------------------------  table_ID=" + table_id);
        PreparedStatement pre = null;
        ResultSet result = null;
        try {
            pre = con.prepareStatement(getUnadornColumnByadorn);
            pre.setInt(1, table_id);
            pre.setString(2, col.trim());
            result = pre.executeQuery();
            if (result.next()) return result.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pre.close();
                result.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getUnadornTablenameById(int table_id) {
        System.out.println("----------------------------   getUnadornTablenameById     ---------------------------  table_ID=" + table_id);
        PreparedStatement pre = null;
        String getUnadornTablenameById = "select table_name from tablename where table_id=?";
        ResultSet result = null;
        try {
            pre = con.prepareStatement(getUnadornTablenameById);
            pre.setInt(1, table_id);
            result = pre.executeQuery();
            if (result.next()) return result.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pre.close();
                result.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public ArrayList<Integer> getRoleListByAccount(int account_id) {
        System.out.println("-------------------   getRoleListByAccount   ----------------  account_id=" + account_id);
        PreparedStatement pre = null;
        ArrayList<Integer> list = new ArrayList<Integer>();
        String getRoleListByAccount = "select role_id from roleaccount where account_id=?";
        ResultSet result = null;
        try {
            pre.setInt(1, account_id);
            pre = con.prepareStatement(getRoleListByAccount);
            result = pre.executeQuery();
            while (result.next()) {
                list.add(result.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                pre.close();
                result.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    public boolean hasSetname(int account_id, String name) {
        System.out.println("-------------------   hasSetname   ----------------  account_id=" + account_id + "  name=" + name);
        PreparedStatement pre = null;
        try {
            pre = con.prepareStatement("select count(*) from querycondition  where account_id=? and setname=?");
            pre.setInt(1, account_id);
            pre.setString(2, name.trim());
            ResultSet result = pre.executeQuery();
            if (result.next() && result.getInt(1) != 0) {
                result.close();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @Override
    public boolean getTypeColumn(String column, int table_id) {
        System.out.println("-------------------   getTypeColumn   ----------------  column=" + column + "    table_id=" + table_id);

        PreparedStatement pre = null;
        String sql = "SELECT datatype FROM COLUMNNAME WHERE column_name =? AND TABLE_ID =?";
        try {
            pre = con.prepareStatement(sql);
            pre.setString(1, column.toUpperCase().trim());
            pre.setInt(2, table_id);
            ResultSet result = pre.executeQuery();
            if (result.next()) {
                String s = result.getString(1);
                System.out.println("column=" + column + "   type=>" + s);
                if (s.equalsIgnoreCase("int") || s.equalsIgnoreCase("double") || s.equalsIgnoreCase("decimal") || s.equalsIgnoreCase("number")) {
                    result.close();
                    return true;
                }
                result.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @Override
    public String getColumnName(String adorn_name) {
        System.out.println("-------------------   getColumnName   ----------------  adorn_name=" + adorn_name);
        PreparedStatement pre = null;
        ResultSet result = null;
        try {
            pre = con.prepareStatement("  select COLUMN_NAME from columnname   where adorn_name=?");
            pre.setString(1, adorn_name.trim());
            result = pre.executeQuery();
            if (result.next()) {
                return result.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pre != null) {
                    pre.close();
                }
                if (result != null) {
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}