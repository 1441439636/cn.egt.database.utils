package db;

import dao.impl.RegistDaoImpl;
import entity.DB;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

/*
 * 使用connect 初始化  connect 因为有可能数据库登录连接失败
 *
 *
 */
public class DBMySql implements Database {
    private String dbName = null;
    private Connection con = null;

    @Override
    public Connection createConnection() {
        DB dba = new RegistDaoImpl().read();
        dbName = dba.getDatabase();
        try {
            String url = "jdbc:mysql://" + dba.getAddress() + ":3306/" + dba.getDatabase() + "?useUnicode=true&characterEncoding=UTF-8";
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, dba.getUsername(), dba.getPasswrod());
            System.out.println("数据库链接成功");
            if (!hasAllUserTables()) {
                dropAllUserTables();
                createAlluserTables();
                System.out.println("3333333333333333333333333333333333");
                addRole("root");//增加默认root角色 此角色可以查看所有表 此角色的账号是服务端软件的登录账号
                System.out.println("4----------------------------------");
                addAccount("admin", "admin");//增加root账号 这就是默认的登录密码
                System.out.println("4-------------------------------------");
                addRoleAccount("root", "admin");
            }
            //账号不可用
        } catch (Exception e) {
            System.out.println("找不到驱动程序类 ，加载驱动失败！");
        }
        return con;
    }

    //true
    public Boolean adminConnect(String logname, String logpass) {
        DB dba = new RegistDaoImpl().read();
        dbName = dba.getDatabase();
        try {
            String url = "jdbc:mysql://" + dba.getAddress() + ":3306/" + dba.getDatabase() + "?useUnicode=true&characterEncoding=UTF-8";
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, dba.getUsername(), dba.getPasswrod());
            System.out.println("数据库链接成功");
            if (!hasAllUserTables()) {
                dropAllUserTables();
                createAlluserTables();
                addRole("root");//增加默认root角色 此角色可以查看所有表 此角色的账号是服务端软件的登录账号
                System.out.println("4----------------------------------");
                addAccount("admin", "admin");//增加root账号 这就是默认的登录密码
                System.out.println("4-------------------------------------");
                addRoleAccount("root", "admin");
            }
            //账号不可用
            if (!serverlog(logname, logpass))
                return false;
        } catch (Exception e) {
            System.out.println("找不到驱动程序类 ，加载驱动失败！");
            return false;
        }
        return true;
    }

    //true
    public boolean hasAllUserTables() {
        String sql = "show tables  from querydata where Tables_in_querydata in ('ROLE','ACCOUNT','ROLEACCOUNT','ROLEPERMISSION','TABLENAME','COLUMNNAME','QUERYCONDITION')";

        PreparedStatement pre = null;
        ResultSet rs = null;
        try {
            pre = con.prepareStatement(sql);
            rs = pre.executeQuery();
            int count = 0;
            while (rs.next()) {
                if (rs.getString(1) != null)
                    count++;
            }
            if (count == 7) {
                return true;
            }
            System.out.println("method -=--=-=---------- hasAllUserTables=" + false);
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pre != null) {
                    pre.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //true
    public void dropAllUserTables() {
        String[] userTableName = {"drop table QUERYCONDITION",
                " drop table COLUMNNAME",
                "drop table  TABLENAME",
                "drop table ROLEPERMISSION",
                "drop table ACCOUNT",
                "drop table ROLEACCOUNT",
                " drop table role"};
        String table = "drop table QUERYCONDITION;" +
                "drop table COLUMNNAME;" +
                "drop table TABLENAME;" +
                "drop table ROLEPERMISSION;" +
                "drop table ACCOUNT;" +
                "drop table ROLEACCOUNT;" +
                "drop table role;";
        for (String s : userTableName) {
            PreparedStatement ps = null;
            try {
                ps = con.prepareStatement(s);
                ps.execute();
                ps.close();
            } catch (SQLException e) {
                System.out.println("------------------      dropAllUserTables  is fail   " + s + "    -------------------------");
            }
        }


    }


    //true
    public void createAlluserTables() {
        String[] createUserTableSql = {
                " create table role( role_id int not null Primary key  AUTO_INCREMENT,role_name varchar(30) not null unique) ENGINE=MyISAM DEFAULT CHARSET=utf8;"//角色表存放角色id 自己分配  角色名 角色名不能为空不能重复
                , "create table account(account_id int  primary key AUTO_INCREMENT,name varchar(30) not null UNIQUE,password varchar(30) not null) ENGINE=MyISAM DEFAULT CHARSET=utf8;"//账号表 存放账号 id 自己分配 账号名 账号密码 账号名不能为空不能重复 密码不能为空
                , "create table roleaccount(account_id int,role_id int) ENGINE=MyISAM DEFAULT CHARSET=utf8;"// 角色 账号对应表 存放 角色id 账号id
                , "create table rolepermission(role_id int ,table_id int not null,column_name varchar(30) not null) ENGINE=MyISAM DEFAULT CHARSET=utf8;"//角色 权限表 角色id 表id 表的列名
                , "create table tablename(table_id int primary key AUTO_INCREMENT,adorn_name varchar(30) unique,table_name varchar(30),flag char(1) default 'N') ENGINE=MyISAM DEFAULT CHARSET=utf8;"//表名 表id 数据库分配 翻译名  不能相同  表名 是否可见 默认不可见
                , "create table columnname(table_id int not null,column_name varchar(30) not null,datatype varchar(106),adorn_name varchar(30),flag char(1) default 'N',no int default 0) ENGINE=MyISAM DEFAULT CHARSET=utf8;"//列名 表id 列名 翻译列名 数据类型 翻译名 是否可见
                , "create table querycondition(account_id int not null ,table_id int not null,column_name varchar(30) not null,flag varchar(1),con1 varchar(30),con2 varchar(30),setname varchar(30) not null) ENGINE=MyISAM DEFAULT CHARSET=utf8;"//保存的设置  账号id 表id 列名   是否被选中  查询条件1 查询条件2  设置名
        };
        for (int i = 0; i < createUserTableSql.length; i++) {
            Statement stmt = null;
            try {
                stmt = con.createStatement();
                stmt.addBatch(createUserTableSql[i]);
                stmt.executeBatch();
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //true
//添加角色 返回角色id 如果存在角色返回-1 其他错误返回-2
    public int addRole(String rolename) {
        System.out.println("--------------addRole-----------rolename=" + rolename);
        try {
            if (hasRole(rolename))
                return -1;
            PreparedStatement pre = null;
            pre = con.prepareStatement("insert into role(role_name) values(?)");
            pre.setString(1, rolename);
            pre.execute();
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return -2;
        }
        return getRoleid(rolename);
    }

    //true
    public int addAccount(String name, String pass) {
        System.out.println("--------------addAccount-----------name=" + name + "  pass=" + pass);
        try {
            if (hasAccount(name)) return -1;
            PreparedStatement pre = null;
            pre = con.prepareStatement("insert into account(name,password) values(? ,?)");
            pre.setString(1, name);
            pre.setString(2, pass);
            pre.execute();
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return getAccountid(name);
    }

    //    true
    public boolean hasAccount(String account_name) {
        System.out.println("--------------addAccount-----------account_name=" + account_name);
        try {
            PreparedStatement pre = null;
            pre = con.prepareStatement("select count(*) from account where name=?");
            pre.setString(1, account_name);
            ResultSet result = pre.executeQuery();
            if (result.next() && result.getInt(1) == 0) {
                result.close();
                pre.close();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    //true
    public void addRoleAccount(String role, String account) {
        System.out.println("--------------addRoleAccount-----------role=" + role + "role=" + role);
        int role_id = getRoleid(role);
        int account_id = getAccountid(account);
        PreparedStatement pre = null;
        try {
            pre = con.prepareStatement("insert into roleaccount values(?,?)");
            pre.setInt(1, account_id);
            pre.setInt(2, role_id);
            pre.execute();
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    //true
    public int getRoleid(String role_name) {
        try {
            PreparedStatement pre = null;
            pre = con.prepareStatement("select role_id from role where role_name=?");
            pre.setString(1, role_name);
            ResultSet result = pre.executeQuery();
            int i = -1;
            if (result.next()) {
                i = result.getInt(1);
            }
            System.out.println("--------------  getRoleid   -----------role_name=" + role_name);
            result.close();
            pre.close();
            return i;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    //true
    public int getAccountid(String name) {
        try {
            PreparedStatement pre = null;
            pre = con.prepareStatement("select account_id from account where name=?");
            pre.setString(1, name);
            ResultSet result = pre.executeQuery();
            int i = -1;
            if (result.next()) {
                i = result.getInt(1);
            }
            System.out.println("--------------  getAccountid   -----------name=" + name);
            result.close();
            pre.close();
            return i;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    //----------------------------------
    public void insertRoleAccount(int role_id, int account_id) {


    }

    /**
     * 删除账号 删除roleaccount 中的账号 删除 querycondition 中的账号
     *
     * @param account_id
     * @return
     */
//true
    public boolean deleteAccount(int account_id) {
        try {
            PreparedStatement pre = null;
            pre = con.prepareStatement("delete from account where account_id=?");
            pre.setInt(1, account_id);
            pre.execute();
            pre.close();
            System.out.println("--------------  deleteAccount   -----------account_id=" + account_id);
            con.prepareStatement("delete from roleaccount where account_id=" + account_id).execute();
            con.prepareStatement("delete from querycondition where account_id=" + account_id).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    //rue
    public boolean serverlog(String logname, String logpass) {
        try {
            PreparedStatement pre = null;
            pre = con.prepareStatement("select count(*) from account a,roleaccount ra,role r where a.name=? and a.password=? and a.account_id=ra.account_id and ra.role_id=r.role_id  and r.role_name='root'");
            pre.setString(1, logname);
            pre.setString(2, logpass);
            ResultSet result = pre.executeQuery();
            System.out.println("--------------  serverlog   -----------logname=" + logname + "   logpass=" + logpass);
            if (result.next() && result.getInt(1) == 1) {
                result.close();
                pre.close();
                return true;
            }
            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }


    //true
    public void transTabletoView() {
        PreparedStatement pre;
        try {
            pre = con.prepareStatement("show  tables   where tables_in_querydata not in('ROLE','ACCOUNT','ROLEACCOUNT','ROLEPERMISSION','TABLENAME','COLUMNNAME','QUERYCONDITION')");
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                String name = rs.getString(1);
                if (!name.contains("_view") && hasview(name + "_view")) {
                    System.out.println("--------------  transTabletoView   ----------  tablename=" + name);
                    createview(name);
                }
            }
            System.out.println("--------------  transTabletoView   ---------- ");
            rs.close();
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //true
    public boolean hasview(String name) {
        try {
            PreparedStatement pre = null;
            pre = con.prepareStatement("show tables where tables_in_querydata =?");
            pre.setString(1, name);
            ResultSet result = pre.executeQuery();
            if (result.next()) {
                result.close();
                pre.close();
                System.out.println("--------------   hasview   ----------  name=" + name);
                return false;
            }
            result.close();
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //true
    public void createview(String name) {
        PreparedStatement pre = null;
        dropView(name);
        try {
            pre = con.prepareStatement("create view " + name + "_view as  select * from " + name);
            pre.execute();
            pre.close();
            System.out.println("-------------------  createview  -------------    name=" + name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //true
    public void dropView(String name) {
        PreparedStatement pre = null;
        try {
            pre = con.prepareStatement("drop view " + name);
            pre.execute();
            pre.close();
            System.out.println("-------------------  dropView  -------------    name=" + name);
        } catch (SQLException e) {
        }
    }

    /**
     * 读取视图方法 只读取视图，若无视图则创建视图
     */
    //true
    public ArrayList<String> getUnadornViewList() {
        ArrayList<String> tablename = new ArrayList<String>();
        String sql = "select table_name from information_schema.TABLES b  where  table_type='view'  and table_schema=?";
        try {
            transTabletoView();
            PreparedStatement pre = con.prepareStatement(sql);
            pre.setString(1, dbName);
            System.out.println(sql);
            ResultSet result = pre.executeQuery();
            while (result.next()) {
                tablename.add(result.getString(1));
            }
            result.close();
            pre.close();
            System.out.println("-----------------       getUnadornViewList       -----------------------");
            return tablename;
        } catch (SQLException e) {
            e.printStackTrace();
            tablename.clear();
            return tablename;
        }
    }


    /**
     * 得到列
     *
     * @param tablename
     * @return
     */
//true
    public ArrayList<String> getColumnList(String tablename) {
        ArrayList<String> list = new ArrayList<String>();
        try {
            PreparedStatement pre = null;
            pre = con.prepareStatement("SELECT  COLUMN_NAME FROM information_schema.COLUMNS  where table_name=?");
            pre.setString(1, tablename);
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
        }
    }

    // 更新表名 首先看在tablename中是否存在 不存在插入 存在更新
// 有时候直接写sql语句感觉更方便点
//true
    public boolean hasTable(String name) {
        PreparedStatement pre = null;
        boolean flag = false;
        try {
            pre = con.prepareStatement("select count(*) from tablename where table_name =?");
            pre.setString(1, name);
            ResultSet result = pre.executeQuery();
            flag = result.next() && result.getInt(1) == 1;
            result.close();
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }

    //true
    public boolean hasColumn(int table_id, String column_name) {
        PreparedStatement pre = null;
        boolean flag = false;
        try {
            pre = con.prepareStatement("select count(*)  from columnname c where  c.table_id=? and c.column_name=?");
            pre.setInt(1, table_id);
            pre.setString(2, column_name);
            ResultSet result = pre.executeQuery();
            flag = result.next() && result.getInt(1) == 1;
            result.close();
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }

    //true
    public void setTableName(String name, String chinese) {

        String updateTableName = "update tablename set adorn_name= ?,flag=? where table_name= ?";
        String haschiese = chinese.equals("") ? "N" : "Y";
        boolean isExit = hasTable(name);
        PreparedStatement pre = null;
        if (isExit) {
            try {
                pre = con.prepareStatement(updateTableName);
                pre.setString(1, chinese);
                pre.setString(2, haschiese);
                pre.setString(3, name);
                pre.execute();
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                pre = con.prepareStatement("insert into tablename(table_name) VALUES (?)");
                pre.setString(1, name);
                pre.execute();
                pre = con.prepareStatement(updateTableName);
                pre.setString(1, chinese);
                pre.setString(2, haschiese);
                pre.setString(3, name);
                pre.execute();
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    //true
    public String getAdornTableName(String onselecttable) {
        String str = "";
        try {
            PreparedStatement pre = null;
            pre = con.prepareStatement("select adorn_name from tablename where table_name=?");
            pre.setString(1, onselecttable);
            ResultSet result = pre.executeQuery();
            if (result.next()) {
                str = result.getString(1);
            }
            result.close();
            pre.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return str;
        }
        return str;
    }

    /*
     * 更新columnname 到底是使用column_id 还是直接用列名 选择了用id 列名虽然不多但 毕竟有三张表使用 然而并没有卵用
     * 还是改成列名
     */
// 根据表名列名插入tableid columnid datatype

    /**
     * @param tablename
     * @param table_id
     * @param column_name
     * @param flag
     * @param chinese
     * @param xh
     */
    //true
    public void insertColname(String tablename, int table_id,
                              String column_name, String flag, String chinese, int xh) {

        String insertColName = "insert into columnname(table_id,column_name,adorn_name,datatype,flag,no) values(?,?,?,?,?,?)";
        String datatype = getDataType(tablename, column_name);
        PreparedStatement pre = null;
        try {
            pre = con.prepareStatement(insertColName);
            pre.setInt(1, table_id);
            pre.setString(2, column_name);
            pre.setString(3, chinese);
            pre.setString(4, datatype);
            pre.setString(5, flag);
            pre.setInt(6, xh);
            pre.execute();
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    /**
     * @param tablename
     * @param column_name
     * @return
     */
    //true
    public String getDataType(String tablename, String column_name) {
        String str = "";
        try {
            PreparedStatement pre = null;
            String getDataType = "SELECT data_type FROM information_schema.COLUMNS  where table_name=? AND  COLUMN_NAME=?";
            pre = con.prepareStatement(getDataType);
            pre.setString(1, tablename);
            pre.setString(2, column_name);
            ResultSet result = pre.executeQuery();
            if (result.next()) {
                str = result.getString(1);
            }
            result.close();
            pre.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return str;
        }
        return str;

    }

    //true
    public boolean updateColname(int table_id, String column_name, String flag,
                                 String chinese, int xh) {
        PreparedStatement pre = null;
        try {
            pre = con.prepareStatement("update columnname set adorn_name= ?, flag = ? , no= ? where table_id = ? and column_name=?");
            pre.setString(1, chinese);
            pre.setString(2, flag);
            pre.setInt(3, xh);
            pre.setInt(4, table_id);
            pre.setString(5, column_name);
            pre.execute();
            pre.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

// 插入columnname
// 首先 获取table_id
// 检测是否之前已经插入列名
// 存在 直接插入
// 不存在先插入

    //true
    public void setColname(String tablename, String colName, String flag,
                           String chinese, int xh) {
        int table_id = 0;
        PreparedStatement pre = null;
        try {
            pre = con.prepareStatement("select table_id from tablename where table_name=? ");
            pre.setString(1, tablename);
            ResultSet result = pre.executeQuery();
            if (result.next()) {
                table_id = result.getInt(1);
                result.close();
            } else {
                result.close();
                return;
            }
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        boolean isExit = hasColumn(table_id, colName);
        if (isExit) {
            updateColname(table_id, colName, flag, chinese, xh);
        } else {
            insertColname(tablename, table_id, colName, flag, chinese, xh);
        }

    }

    //true
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

    //true
    public ArrayList<String> getAdornTablenameList() {
        ArrayList<String> list = new ArrayList<String>();
        try {
            PreparedStatement pre = null;
            pre = con.prepareStatement("select adorn_name from tablename where  flag='Y'");
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
        }
    }

    //true
// 注意到有一些方法的结构类似 ->使用sql 传出一维的list 是否可以将其抽象为一个方法
    public ArrayList<String[]> getRoleList(int type) {
        String getRoleList = "select role_id,role_name from role";
        ArrayList<String[]> list = new ArrayList<String[]>();
        try {
            String s;
            switch (type) {
                case 0:
                    s = getRoleList + " where not role_name='root'";
                    break;
                default:
                    s = getRoleList;
                    break;
            }
            PreparedStatement pre = null;
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
        }
    }

    //true
    public boolean hasRole(String rolename) {
        try {
            PreparedStatement pre = null;
            pre = con.prepareStatement("select count(*) from role where role_name=?");
            pre.setString(1, rolename);
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
        }
        return true;
    }

    //true
    public void deleteRole(String rolename) {
        try {
            PreparedStatement pre = null;
            pre = con.prepareStatement("delete from  role where role_name=?");
            pre.setString(1, rolename);
            pre.execute();
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除角色 将角色从角色表中删除
     * 将角色账号表中此角色的账号的角色id 设为-1
     *
     * @param role_id
     */
//true
    public void deleteRole(int role_id) {
        PreparedStatement pre1;
        PreparedStatement pre2;
        try {
            pre1 = con.prepareStatement("delete FROM role where role_id= " + role_id);
            pre1.execute();
            pre1.close();
            pre2 = con.prepareStatement("update roleaccount set role_id=-1 where role_id= " + role_id);
            pre2.execute();
            pre2.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //true
    public int updateRole(int role_id, String name) {
        PreparedStatement pre = null;
        try {
            pre = con.prepareStatement("update role set role_name= '" + name + "' where role_id= " + role_id);
            pre.execute();
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }
    //true

    public ArrayList<String> getAdornColumnList(String tablename) {
        ArrayList<String> list = new ArrayList<String>();
        try {
            PreparedStatement pre = null;
            pre = con.prepareStatement("select adorn_name from columnname where flag='Y' and table_id in (select table_id from tablename where adorn_name=?)");
            pre.setString(1, tablename);
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
        }
    }

    //true
    public void addRolePermission(int role_id, int table_id, ArrayList<String> list) {
        PreparedStatement pre = null;
        try {
            pre = con.prepareStatement("insert into rolepermission(role_id,table_id,column_name) select r.role_id ,c.table_id,c.column_name from columnname c,role r  where r.role_id=? and  c.table_id=? and  c.adorn_name=?");
            for (String val : list) {
                pre.setInt(1, role_id);
                pre.setInt(2, table_id);
                pre.setString(3, val);
                pre.execute();
            }
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    //true
    public int getTableid(String table_adorn_name) {
        PreparedStatement pre;
        try {
            pre = con.prepareStatement("select table_id from tablename where adorn_name=?");
            pre.setString(1, table_adorn_name);
            ResultSet result = pre.executeQuery();
            if (result.next())
                return result.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    //true
    public void deleteRolePermission(int role_id, int table_id) {
        PreparedStatement pre = null;
        try {
            pre = con.prepareStatement("delete from  rolepermission where role_id =? and  table_id=?");
            pre.setInt(1, role_id);
            pre.setInt(2, table_id);
            pre.execute();
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //    true
    public boolean hasRolePermission(int role_id, int table_id, String column) {
        String hasRolePermission = "select count(*) from rolepermission r where r.role_id=? and r.table_id=? and r.column_name in (select t.column_name from columnname t where t.table_id=? and t.adorn_name=?)";
        PreparedStatement pre = null;
        boolean flag = false;
        try {
            pre = con.prepareStatement(hasRolePermission);
            pre.setInt(1, role_id);
            pre.setInt(2, table_id);
            pre.setInt(3, table_id);
            pre.setString(4, column);
            ResultSet result = pre.executeQuery();
            flag = result.next() && result.getInt(1) == 1;
            result.close();
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }


    //true
    public ArrayList<String[]> getAllAccount() {
        ArrayList<String[]> list = new ArrayList<String[]>();
        String s;
        ResultSet result;
        try {
            PreparedStatement pre = con.prepareStatement("select a.account_id, a.name,a.password  from account a");
            result = pre.executeQuery();
            while (result.next()) {
                String[] acc = new String[3];
                for (int i = 0; i < acc.length; i++) {
                    acc[i] = result.getString(i + 1);
                }
                list.add(acc);
            }
            result.close();
            pre.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return list;
        }
    }

    //true
    public void updateAccount(int account_id, String name, String pass) {
        try {
            PreparedStatement pre = null;
            pre = con.prepareStatement("update account set name=?,password=? where account_id=?");
            pre.setString(1, name);
            pre.setString(2, pass);
            pre.setInt(3, account_id);
            pre.execute();
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 可能需要处理
     *
     * @param role_id
     * @return
     */
    //true
    public HashMap<String, String> getAccountByRole(int role_id) {
        String getAccountByRoleid = "select a.account_id ,a.name,a.password  from account a where a.account_id in (select account_id from roleaccount where role_id =?)";
        HashMap<String, String> list = new HashMap<String, String>();
        try {
            PreparedStatement pre = null;
            pre = con.prepareStatement(getAccountByRoleid);
            pre.setInt(1, role_id);
            ResultSet result = pre.executeQuery();
            while (result.next()) {
                list.put(result.getString(2), "");
                String[] acc = new String[3];
                for (int i = 0; i < acc.length; i++) {
                    acc[i] = result.getString(i + 1);
                }
            }
            result.close();
            pre.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return list;
        }

    }

    /**
     * 可能需要处理
     *
     * @param onselecttable
     * @return
     */
    //true
    public int getidByunadornname(String onselecttable) {
        try {
            PreparedStatement pre = null;
            pre = con.prepareStatement("select  table_id from tablename where  table_name=? ");
            pre.setString(1, onselecttable);
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
        }
    }

    public void deleteRoleAccount(int role_id, int account_id) {
        try {
            String deleRoleAccount = "delete from roleaccount where role_id=? and account_id=?";
            PreparedStatement pre = null;
            pre = con.prepareStatement(deleRoleAccount);
            pre.setInt(1, role_id);
            pre.setInt(2, account_id);
            pre.execute();
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //true
    public void addRoleAccount(int role_id, int accountId) {
        try {
            PreparedStatement pre = null;
            pre = con.prepareStatement("insert into roleaccount(role_id,account_id) values(?,?)");
            pre.setInt(1, role_id);
            pre.setInt(2, accountId);
            pre.execute();
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<String> getRoleAccountByRoleId(int roleId) {
        ArrayList<String> list = new ArrayList<String>();
        try {
            PreparedStatement pre = con.prepareStatement("select account_id from roleaccount where role_id =?");
            pre.setInt(1, roleId);
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
        }
    }

    public boolean hasRoleAccount(int role_id, int account_id) {
        System.out.println("--------------------     hasRoleAccount   ---------------------");
        try {
            PreparedStatement pre = con.prepareStatement("select count(*) from roleaccount where role_id =? and account_id=?");
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
        }
    }

    private final static String getAdornTablenameList = "select adorn_name from tablename where flag='Y'";
    private final static String updateRoleAccount = "update roleaccount set role_id=? where account_id=?";
    private final static String createView = "create or replace view ? as select * from ?";

    //true
    public Boolean userConnect() {
//        获取注册列表
        DB dba = new RegistDaoImpl().read();
        try {
            String url = "jdbc:mysql://" + dba.getAddress() + ":3306/" + dba.getDatabase() + "?useUnicode=true&characterEncoding=UTF-8";
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, dba.getUsername(), dba.getPasswrod());
            System.out.println(" --------------------      connect    DBMySql    succeed  -------------------------");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "数据库错误", "+_+", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        return true;
    }

    //true
    public boolean isRootAccount(int account_id) {
        PreparedStatement pre;
        try {
            pre = con.prepareStatement("select role_name from role where role_id in (select role_id from roleaccount where account_id=?)");
            pre.setInt(1, account_id);
            ResultSet result = pre.executeQuery();
            if (result.next() && result.getString(1).equals("root")) return true;
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //true
    public ArrayList<String> getColumnList(int account_id, int table_id) {
        PreparedStatement pre;
        ResultSet result = null;
        String sql = "select c.adorn_name from columnname c where c.table_id=? and c.column_name in (select r.column_name from rolepermission r where r.role_id in (select role_id from roleaccount where account_id=?) and r.table_id=?) order by c.no";
        ArrayList<String> list = new ArrayList<String>();
        try {
            pre = con.prepareStatement(sql);
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
        }
    }


    //需要处理
    public ResultSet getsetedcollist(String setname, int account_id) {
        PreparedStatement pre;
        String getsetedcollistSql = "select focus,colname,con1,con2 from querycondition where querycondition.setname=? and account_id=? ";
        try {
            pre = con.prepareStatement(getsetedcollistSql);
            pre.setString(1, setname);
            pre.setInt(2, account_id);
            return pre.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //true
    public void setquerycondition(int account_id, int table_id, String setName, String colname, String select, String con1, String con2) {
        PreparedStatement pre;
        try {
            //insert into querycondition(account_id,table_id,setname,column_name,flag,con1,con2)"
            String column_name = getcolumnname(table_id, colname);
            pre = con.prepareStatement("insert into querycondition(account_id,table_id,setname,column_name,flag,con1,con2) values(?,?,?,?,?,?,?)");
            pre.setInt(1, account_id);
            pre.setInt(2, table_id);
            pre.setString(3, setName);
            pre.setString(4, column_name);
            pre.setString(5, select);
            pre.setString(6, con1);
            pre.setString(7, con2);
            pre.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletequerycondition(int account_id, String setname) {
        PreparedStatement pre;
        try {
            pre = con.prepareStatement("delete from querycondition where account_id=? and setname=?");
            pre.setInt(1, account_id);
            pre.setString(2, setname);
            pre.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getcolumnname(int table_id, String colname) {
        PreparedStatement pre;
        try {
            pre = con.prepareStatement("select column_name from columnname where table_id=? and adorn_name=?");
            pre.setInt(1, table_id);
            pre.setString(2, colname);
            ResultSet result = pre.executeQuery();
            if (result.next())
                return result.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public ArrayList<String> getSetname(int account_id) {
        PreparedStatement pre;
        ArrayList<String> list = new ArrayList<String>();
        try {
            pre = con.prepareStatement("select distinct setname from querycondition where account_id=?");
            pre.setInt(1, account_id);
            ResultSet result = pre.executeQuery();
            while (result.next()) {
                list.add(result.getString(1));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return list;
        }

    }

    public String gettablename(String setname, int account_id) {
        PreparedStatement pre;
        try {
            pre = con.prepareStatement("select adorn_name from tablename where table_id in (select distinct table_id from querycondition where setname=? and account_id=?)");
            pre.setString(1, setname);
            pre.setInt(2, account_id);
            ResultSet result = pre.executeQuery();
            if (result.next())
                return result.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //列名 t 对应列的查询条件query 表名 tablename
    //TODO 这里有很多种方法
    public ResultSet getresultTable(ArrayList<String> column, ArrayList<String> query, int table_id) throws Exception {
        String table = getUnadornTablenameById(table_id);
        StringBuilder sql = new StringBuilder("select ");
        int size = query.size();
        ArrayList<String> unadorncolumn = new ArrayList<String>();
        for (int i = 0; i < query.size(); i++) {
            unadorncolumn.add(getcolumn(column.get(i), table_id));
        }
        for (int i = 0; i < size - 1; i++) {
            sql.append(unadorncolumn.get(i) + ",");
        }
        sql.append(unadorncolumn.get(size - 1) + " from " + table);
        boolean isStart = true;
        for (int i = 0; i < size; i++) {
            if (!query.get(i).equals("")) {
                if (isStart) {
                    sql.append(" where " + unadorncolumn.get(i) + " " + query.get(i));
                    isStart = false;
                } else {
                    sql.append(" and " + unadorncolumn.get(i) + " " + query.get(i));
                }
            }
        }
        return con.prepareStatement(sql.toString()).executeQuery();
    }

    //true
    public String getcolumn(String column, int table_id) {
        PreparedStatement pre;
        try {
            pre = con.prepareStatement("select column_name from  columnname where table_id=? and adorn_name=?");
            pre.setInt(1, table_id);
            pre.setString(2, column);
            ResultSet result = pre.executeQuery();
            if (result.next()) {
                return result.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "wrong";
    }

    //true
    public int logByAccount(String userName, String password) {
        System.out.println("userName=" + userName + " password=" + password);
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("select account_id from account where name=? and password=?");
            ps.setString(1, userName);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt(1);
                return id;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    //true
    public ArrayList<String> getTableList(int account_id) {
        PreparedStatement pre;
        ArrayList<String> list = new ArrayList<String>();
        try {
            if (isRootAccount(account_id)) {
                pre = con.prepareStatement("select adorn_name from tablename");
                ResultSet result = pre.executeQuery();
                while (result.next()) {
                    list.add(result.getString(1));
                }
                result.close();
            } else {
                pre = con.prepareStatement("select distinct adorn_name from tablename where table_id in (select  distinct table_id from rolepermission where role_id in (select role_id from roleaccount where account_id=?))");
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
        }
    }

    //    true
    public String[] getquerycondition(int account_id, int table_id, String setname, String columnname) {
        PreparedStatement pre;
        String[] set = {"N", "", ""};
        try {
            String column_name = getcolumnname(table_id, columnname);
            pre = con.prepareStatement("select flag,con1,con2 from  querycondition where account_id=? and table_id=? and column_name=? and setname=?");
            pre.setInt(1, account_id);
            pre.setInt(2, table_id);
            pre.setString(3, column_name);
            pre.setString(4, setname);
            ResultSet result = pre.executeQuery();
            if (result.next()) {
                for (int i = 0; i < set.length; i++) {
                    set[i] = result.getString(i + 1);
                }
            }
            return set;
        } catch (SQLException e) {
            e.printStackTrace();
            return set;
        }
    }

    //true
    public ArrayList<String> tranColumnlist(ArrayList<String> column, int table_id) {
        for (int i = 0; i < column.size(); i++) {
            column.set(i, getcolumn(column.get(i), table_id));
        }
        return column;
    }

    //true
    public ArrayList<ArrayList<String>> getGroup(int table_id, String hzx, String hzxsql, String tjx, String tjxsql) {

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
        try {
            ResultSet result = con.prepareStatement(sql).executeQuery();
            while (result.next()) {
                ArrayList<String> temp = new ArrayList<String>();
                temp.add(result.getString(1));
                temp.add(result.getString(2));
                data.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    //true
    public String getUnadornColumnByadorn(int table_id, String col) {
        PreparedStatement pre;
        try {
            pre = con.prepareStatement("select column_name from  columnname where table_id=? and adorn_name=?");
            pre.setInt(1, table_id);
            pre.setString(2, col);
            ResultSet result = pre.executeQuery();
            if (result.next()) return result.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //true
    public String getUnadornTablenameById(int table_id) {
        PreparedStatement pre;
        try {
            pre = con.prepareStatement("select table_name from tablename where table_id=?");
            pre.setInt(1, table_id);
            ResultSet result = pre.executeQuery();
            if (result.next()) return result.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //true
    public ArrayList<Integer> getRoleListByAccount(int account_id) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        try {
            PreparedStatement pre = null;
            pre.setInt(1, account_id);
            pre = con.prepareStatement("select role_id from roleaccount where account_id=?");
            ResultSet result = pre.executeQuery();
            while (result.next()) {
                list.add(result.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return list;
    }

    //true
    public boolean hasSetname(int account_id, String name) {
        PreparedStatement pre;
        try {
            pre = con.prepareStatement("select count(*) from querycondition  where account_id=? and setname=?");
            pre.setInt(1, account_id);
            pre.setString(2, name);
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

    //true
    public boolean getTypeColumn(String column, int table_id) {
        PreparedStatement pre;
        try {
            pre = con.prepareStatement("  SELECT a.COLUMN_TYPE  FROM information_schema.COLUMNS a,tablename t WHERE a.table_name=t.table_name AND t.table_id=? AND a.COLUMN_NAME=?");
            pre.setInt(1, table_id);
            pre.setString(2, column);
            ResultSet result = pre.executeQuery();
            if (result.next()) {
                String s = result.getString(1);
                System.out.println("column=" + column + "   type=>" + s);
                if (s.contains("int") || s.contains("double") || s.contains("decimal")) {
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

    //true
    public String getColumnName(String s) {
        PreparedStatement pre;
        try {
            pre = con.prepareStatement("  select COLUMN_NAME from columnname   where adorn_name=?");
            pre.setString(1, s);
            ResultSet result = pre.executeQuery();
            if (result.next()) {
                return result.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
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
}