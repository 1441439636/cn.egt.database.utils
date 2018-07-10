package servlet;

import db.Database;
import entity.User;
import service.UserService;
import utils.DBConn;
import utils.ShowRequestUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by 14414 on 2017/4/19.
 */
public class UserServlet extends BaseServlet {

    public String loginUI(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, InvocationTargetException, IllegalAccessException {
        return "/login.jsp";
    }

    public String resultUI(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, InvocationTargetException, IllegalAccessException {
        ShowRequestUtil.showParams(req);
        String statisticalPattern = req.getParameter("statisticalPattern");
        ArrayList<ArrayList<String>> resultList = new ArrayList<>();
        String statisticalItem = req.getParameter("statisticalItem").trim();
        if (statisticalItem.equals("请选择！")) {
            //未选择查询条件
            req.setAttribute("msg", "resultUI0");
            return "404.jsp";
        }
        if (statisticalPattern.equals("全部汇总")) {
            String[] items = req.getParameterValues(statisticalItem);
            int sum = 0;
            for (int i = 1; i < items.length; i++) {
                sum += Double.parseDouble(items[i]);
            }
            ArrayList<String> list = new ArrayList<String>();
            list.add("条件");
            list.add(statisticalItem);
            resultList.add(list);
            list = new ArrayList<String>();
            list.add("全部汇总");
            list.add(String.valueOf(sum));
            resultList.add(list);
        } else if (statisticalPattern.equals("求和")) {
            String statisticalRule = req.getParameter("statisticalRule").trim();

            String[] rules = req.getParameterValues(statisticalRule);
            ArrayList<String> list = new ArrayList<String>();
            list.add("求和");
            list.add(statisticalItem);
            resultList.add(list);
            if (statisticalRule.equals("请选择！")) {
                list = new ArrayList<String>();
                list.add("默认全部");
                list.add("0");
                resultList.add(list);
                String[] items = req.getParameterValues(statisticalItem);
                for (int i = 1; i < items.length; i++) {
                    resultList.get(1).set(1, String.valueOf(Double.parseDouble(resultList.get(1).get(1)) + Double.parseDouble(items[i])));
                }
            } else {
                Set<String> set = new HashSet<>();
                set.addAll(Arrays.asList(rules).subList(1, rules.length));
                for (String val : set) {
                    list = new ArrayList<String>();
                    list.add(val);
                    list.add("0");
                    resultList.add(list);
                }


                String[] items = req.getParameterValues(statisticalItem);
                for (int i = 1; i < items.length; i++) {
                    for (ArrayList<String> listval : resultList) {
                        if (listval.get(0).equals(rules[i])) {
                            listval.set(1, String.valueOf(Double.parseDouble(listval.get(1)) + Double.parseDouble(items[i])));
                            break;
                        }
                    }
                }
            }
        } else if (statisticalPattern.equals("平均值")) {
            String statisticalRule = req.getParameter("statisticalRule").trim();
            String[] rules = req.getParameterValues(statisticalRule);
            ArrayList<String> list = new ArrayList<>();
            list.add("求平均值");
            list.add(statisticalItem);
            resultList.add(list);
            if (statisticalRule.equals("请选择！")) {
                list = new ArrayList<>();
                list.add("默认全部");
                list.add("0");
                list.add("0");
                resultList.add(list);
                String[] items = req.getParameterValues(statisticalItem);
                for (int i = 1; i < items.length; i++) {
                    resultList.get(1).set(1, String.valueOf(Double.parseDouble(resultList.get(1).get(1)) + Double.parseDouble(items[i])));
                    resultList.get(1).set(2, String.valueOf(Double.parseDouble(resultList.get(1).get(2)) + 1));
                }
                resultList.get(1).set(1, String.valueOf(Double.parseDouble(resultList.get(1).get(1)) / Double.parseDouble(resultList.get(1).get(2))));
                resultList.get(1).remove(2);
            } else {
                Set<String> set = new HashSet<>();
                for (int i = 1; i < rules.length; i++) {
                    set.add(rules[i]);
                }
                for (String val : set) {
                    list = new ArrayList<>();
                    list.add(val);
                    list.add("0");
                    list.add("0");
                    resultList.add(list);
                }


                String[] items = req.getParameterValues(statisticalItem);
                for (int i = 1; i < items.length; i++) {
                    for (ArrayList<String> listval : resultList) {
                        if (listval.get(0).equals(rules[i])) {
                            listval.set(1, String.valueOf(Double.parseDouble(listval.get(1)) + Double.parseDouble(items[i])));
                            listval.set(2, String.valueOf(Double.parseDouble(listval.get(2)) + 1));
                            break;
                        }
                    }
                }
                for (int i = 1; i < resultList.size(); i++) {
                    resultList.get(i).set(1, String.valueOf(Double.parseDouble(resultList.get(i).get(1)) / Double.parseDouble(resultList.get(i).get(2))));
                    resultList.get(i).remove(2);
                }
            }
        } else if (statisticalPattern.equals("最大值")) {
            String statisticalRule = req.getParameter("statisticalRule").trim();
            String[] rules = req.getParameterValues(statisticalRule);

            ArrayList<String> list = new ArrayList<String>();
            list.add("最大值");
            list.add(statisticalItem);
            resultList.add(list);
            if (statisticalRule.equals("请选择！")) {
                list = new ArrayList<String>();
                list.add("默认全部");
                list.add("999999999");
                resultList.add(list);
                String[] items = req.getParameterValues(statisticalItem);
                for (int i = 1; i < items.length; i++) {
                    if (Double.parseDouble(resultList.get(1).get(1)) < Double.parseDouble(items[i]))
                        resultList.get(1).set(1, String.valueOf(Double.parseDouble(items[i])));
                }
            } else {
                Set<String> set = new HashSet<String>();
                for (int i = 1; i < rules.length; i++) {
                    set.add(rules[i]);
                }
                for (String val : set) {
                    list = new ArrayList<String>();
                    list.add(val);
                    list.add("0");
                    resultList.add(list);
                }
                String[] items = req.getParameterValues(statisticalItem);
                for (int i = 1; i < items.length; i++) {
                    for (ArrayList<String> listval : resultList) {
                        if (listval.get(0).equals(rules[i])) {
                            if (Double.parseDouble(listval.get(1)) < Double.parseDouble(items[i]))
                                listval.set(1, String.valueOf(Double.parseDouble(items[i])));
                            break;
                        }
                    }
                }
            }
        } else if (statisticalPattern.equals("最小值")) {
            String statisticalRule = req.getParameter("statisticalRule").trim();
            String[] rules = req.getParameterValues(statisticalRule);
            ArrayList<String> list = new ArrayList<String>();
            list.add("最小值");
            list.add(statisticalItem);
            resultList.add(list);
            if (statisticalRule.equals("请选择！")) {

                list = new ArrayList<String>();
                list.add("默认全部");
                list.add("999999999");
                resultList.add(list);
                String[] items = req.getParameterValues(statisticalItem);
                for (int i = 1; i < items.length; i++) {
                    if (Double.parseDouble(resultList.get(1).get(1)) > Double.parseDouble(items[i]))
                        resultList.get(1).set(1, String.valueOf(Double.parseDouble(items[i])));
                }

            } else {
                Set<String> set = new HashSet<String>();
                for (int i = 1; i < rules.length; i++) {
                    set.add(rules[i]);
                }
                for (String val : set) {
                    list = new ArrayList<String>();
                    list.add(val);
                    list.add("999999999");
                    resultList.add(list);
                }
                String[] items = req.getParameterValues(statisticalItem);
                for (int i = 1; i < items.length; i++) {
                    for (ArrayList<String> listval : resultList) {
                        if (listval.get(0).equals(rules[i])) {
                            if (Double.parseDouble(listval.get(1)) > Double.parseDouble(items[i]))
                                listval.set(1, String.valueOf(Double.parseDouble(items[i])));
                            break;
                        }
                    }
                }
            }
        } else

        {
            //查询条件错误
            req.setAttribute("msg", "resultUI1");
            return "404.jsp";
        }
        req.getSession().setAttribute("resultList", resultList);

        return "/result.jsp";
    }

    /**
     * 进入查询结果页面
     *
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public String selectUI(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, InvocationTargetException, IllegalAccessException {
        ShowRequestUtil.showParams(req);
        Database database = (Database) req.getSession().getAttribute("database");
        String adornTable = req.getParameter("adornTable");
        int tableId = database.getTableid(adornTable);//获取table_id

        String[] adornColumnArray = req.getParameterValues("adornColumn");
        String[] con1Array = req.getParameterValues("con1");
        String[] con2Array = req.getParameterValues("con2");
        //查詢字段与查询条件  adornColumn，con1，con2，type
        ArrayList<String[]> columnList = new ArrayList<String[]>();
        ArrayList<String> adornColumn = new ArrayList<String>();
        ArrayList<String> adornColumnType = new ArrayList<String>();
        for (int i = 0; i < adornColumnArray.length; i++) {
            if (req.getParameter("flag" + i) != null) {
                String[] strings = new String[3];
                adornColumn.add(adornColumnArray[i]);
                strings[0] = database.getcolumn(adornColumnArray[i], tableId);
                strings[2] = req.getParameter("columnType" + i);
                String con1 = con1Array[i];
                String con2 = con2Array[i];
                adornColumnType.add(strings[2].trim());
                if (strings[2].trim().equals("int")) {
                    if (con1.trim().equals("") && !con2.trim().equals("")) {
                        strings[1] = " " + strings[0] + "<=" + con2.trim() + " ";
                    } else if (!con1.trim().equals("") && con2.trim().equals("")) {
                        strings[1] = " " + strings[0] + ">=" + con1.trim() + " ";
                    } else if (!con1.trim().equals("") && !con2.trim().equals("")) {
                        strings[1] = " " + strings[0] + ">=" + con1.trim() + " AND " + strings[0] + "<=" + con2.trim() + " ";
                    } else {
                        strings[1] = "";
                    }
                } else {
                    strings[1] = " (" + strings[0] + " like '%" + con1.trim() + "%'  AND  " + strings[0] + " like '%" + con2.trim() + "%')";
                }
                columnList.add(strings);
            }
        }
        ArrayList<ArrayList<String>> resultList = new ArrayList<ArrayList<String>>();
        if (columnList.size() != 0) {
            resultList = database.getResultTableList(columnList, tableId);
            resultList.add(0, adornColumn);
        }


        req.getSession().setAttribute("resultList", resultList);
        req.getSession().setAttribute("adornColumnType", adornColumnType);
        return "/select.jsp";
    }

    public String selectTable(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, InvocationTargetException, IllegalAccessException {
        ShowRequestUtil.showParams(req);
        String adornTable = req.getParameter("adornTable");
        User userVal = (User) req.getSession().getAttribute("user");
        int account_id = userVal.getUid();
        Database database = (Database) req.getSession().getAttribute("database");

        //获得用户所有表
        ArrayList<String> adornTableList = database.getTableList(account_id);
        //获得配置
        ArrayList<String> setNameList = database.getSetname(account_id);
        setNameList.add("请选择配置！");
        //初始化查询第1个
        int tableid = database.getTableid(adornTable);

        System.out.println(" getTableList     adornTable="+adornTable);
        ArrayList<String> adornColumnList = database.getColumnList(account_id, tableid);
        ArrayList<String> adornColumnTypeList = new ArrayList<>();
        for (String val : adornColumnList) {
            if (database.getTypeColumn(database.getColumnName(val), tableid))
                adornColumnTypeList.add("int");
            else
                adornColumnTypeList.add("string");
        }

//flag con1 con2
        ArrayList<String[]> tableFlagList = new ArrayList<>();
        for (String adornColumn : adornColumnList) {
            tableFlagList.add(new String[]{"N", "", ""});
        }
        req.getSession().setAttribute("user", userVal);

        req.setAttribute("adornTableVal", adornTable);
        req.setAttribute("adornTableList", adornTableList);

        req.setAttribute("setNameVal", "请选择配置！");
        req.setAttribute("setNameList", setNameList);

        req.setAttribute("adornColumnList", adornColumnList);
        req.setAttribute("adornColumnTypeList", adornColumnTypeList);
        req.setAttribute("tableFlagList", tableFlagList);

        return "info.jsp";
    }

    public String selectSet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, InvocationTargetException, IllegalAccessException {
        ShowRequestUtil.showParams(req);
        String setName = req.getParameter("setName");
        Database database = (Database) req.getSession().getAttribute("database");
        User userVal = (User) req.getSession().getAttribute("user");
        int account_id = userVal.getUid();

        String adornTable = database.gettablename(setName, account_id);
//        重新获取所有数据，同切换配置
        int tableId = database.getTableid(adornTable);
        ArrayList<String> adornColumnList = database.getColumnList(account_id, tableId);
//flag con1 con2
        ArrayList<String[]> tableFlagList = new ArrayList<String[]>();
        for (String adornColumn : adornColumnList) {
            tableFlagList.add(database.getquerycondition(account_id, tableId, setName, adornColumn));
        }
        //获得用户所有表
        ArrayList<String> adornTableList = database.getTableList(account_id);
        //获得配置
        ArrayList<String> setNameList = database.getSetname(account_id);
        ArrayList<String> adornColumnTypeList = new ArrayList<String>();

        for (String val : adornColumnList) {
            if (database.getTypeColumn(database.getColumnName(val), tableId))
                adornColumnTypeList.add("int");
            else
                adornColumnTypeList.add("string");
        }
        req.setAttribute("adornTableVal", adornTable);
        req.setAttribute("adornTableList", adornTableList);

        req.setAttribute("setNameVal", setName);
        req.setAttribute("setNameList", setNameList);

        req.setAttribute("adornColumnList", adornColumnList);
        req.setAttribute("adornColumnTypeList", adornColumnTypeList);
        req.setAttribute("tableFlagList", tableFlagList);
        return "info.jsp";
    }

    public String deleteSet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, InvocationTargetException, IllegalAccessException {
        ShowRequestUtil.showParams(req);
        String setName = req.getParameter("setName");
        String adornTable = req.getParameter("adornTable");
        Database database = (Database) req.getSession().getAttribute("database");
        User userVal = (User) req.getSession().getAttribute("user");
        int account_id = userVal.getUid();
        if (!database.hasSetname(account_id, setName)) {
            req.setAttribute("msg", "deleteSet1");
            return "404.jsp";
        }
        database.deletequerycondition(account_id, setName);

//        重新获取所有数据，同切换配置
        int tableId = database.getTableid(adornTable);
        ArrayList<String> adornColumnList = database.getColumnList(account_id, tableId);
//flag con1 con2
        ArrayList<String[]> tableFlagList = new ArrayList<String[]>();
        for (String adornColumn : adornColumnList) {
            tableFlagList.add(database.getquerycondition(account_id, tableId, setName, adornColumn));
        }
        //获得用户所有表
        ArrayList<String> adornTableList = database.getTableList(account_id);
        //获得配置
        ArrayList<String> setNameList = database.getSetname(account_id);
        ArrayList<String> adornColumnTypeList = new ArrayList<String>();

        for (String val : adornColumnList) {
            if (database.getTypeColumn(database.getColumnName(val), tableId))
                adornColumnTypeList.add("int");
            else
                adornColumnTypeList.add("string");
        }
        req.setAttribute("adornTableVal", adornTable);
        req.setAttribute("adornTableList", adornTableList);

        req.setAttribute("setNameVal", "请选择配置！");
        req.setAttribute("setNameList", setNameList);

        req.setAttribute("adornColumnList", adornColumnList);
        req.setAttribute("adornColumnTypeList", adornColumnTypeList);
        req.setAttribute("tableFlagList", tableFlagList);
        return "info.jsp";
    }

    /**
     * 添加配置
     *
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public String addSet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, InvocationTargetException, IllegalAccessException {
        ShowRequestUtil.showParams(req);
        Database database = (Database) req.getSession().getAttribute("database");
        User userVal = (User) req.getSession().getAttribute("user");
        int account_id = userVal.getUid();
        if (account_id + 1 == 0) {
            //账户不存在，请重新输入，或请管理员确认！
            req.setAttribute("msg", "login1");
            return "404.jsp";
        }
        String setName = req.getParameter("setName");
        String adornTable = req.getParameter("adornTable");

        if (database.hasSetname(account_id, setName)) {
            req.setAttribute("msg", "addSet1");
            return "404.jsp";
        }
        int tableId = database.getTableid(adornTable);//获取table_id
        String[] adornColumnArray = req.getParameterValues("adornColumn");
        String[] con1Array = req.getParameterValues("con1");
        String[] con2Array = req.getParameterValues("con2");
        String[] flagArray = new String[adornColumnArray.length];
        for (int i = 0; i < flagArray.length; i++) {
            flagArray[i] = req.getParameter("flag" + i) == null ? "N" : "Y";
            database.setquerycondition(account_id, tableId, setName, adornColumnArray[i], flagArray[i], con1Array[i] == null ? "" : con1Array[i], con2Array[i] == null ? "" : con2Array[i]);
        }

//        重新获取所有数据，同切换配置
        adornTable = database.gettablename(setName, account_id);
        tableId = database.getTableid(adornTable);
        ArrayList<String> adornColumnList = database.getColumnList(account_id, tableId);
//flag con1 con2
        ArrayList<String[]> tableFlagList = new ArrayList<String[]>();
        for (String adornColumn : adornColumnList) {
            tableFlagList.add(database.getquerycondition(account_id, tableId, setName, adornColumn));
        }
        //获得用户所有表
        ArrayList<String> adornTableList = database.getTableList(account_id);
        //获得配置
        ArrayList<String> setNameList = database.getSetname(account_id);
        ArrayList<String> adornColumnTypeList = new ArrayList<String>();

        for (String val : adornColumnList) {
            if (database.getTypeColumn(database.getColumnName(val), tableId))
                adornColumnTypeList.add("int");
            else
                adornColumnTypeList.add("string");
        }
        req.setAttribute("adornTableVal", adornTable);
        req.setAttribute("adornTableList", adornTableList);

        req.setAttribute("setNameVal", setName);
        req.setAttribute("setNameList", setNameList);

        req.setAttribute("adornColumnList", adornColumnList);
        req.setAttribute("adornColumnTypeList", adornColumnTypeList);
        req.setAttribute("tableFlagList", tableFlagList);
        return "info.jsp";
    }

    /**
     * 用户退出
     *
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public String logout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, InvocationTargetException, IllegalAccessException {
        req.getSession().removeAttribute("user");
        resp.sendRedirect(req.getContextPath() + "/");
        return null;
    }

    /**
     * 登录
     *
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public String login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, InvocationTargetException, IllegalAccessException {
        ShowRequestUtil.showParams(req);
        User userVal = new User();
        userVal.setUsername(req.getParameter("username"));
        userVal.setPassword(req.getParameter("password"));

        Database database = DBConn.createDB();
        database.userConnect();
        req.getSession().setAttribute("database", database);

        int account_id = database.logByAccount(userVal.getUsername(), userVal.getPassword());
        userVal.setUid(account_id);
        if (account_id + 1 == 0) {
            //账户不存在，请重新输入，或请管理员确认！
            req.setAttribute("msg", "login1");
            return "404.jsp";
        }
        //获得用户所有表
        ArrayList<String> adornTableList = database.getTableList(account_id);
        //获得配置
        ArrayList<String> setNameList = database.getSetname(account_id);
        setNameList.add("请选择配置！");
        //初始化查询第1个
        int tableid = database.getTableid(adornTableList.get(0));
        ArrayList<String> adornColumnList = database.getColumnList(account_id, tableid);
//获取
//            database.getTypeColumn()
        ArrayList<String> adornColumnTypeList = new ArrayList<String>();
        for (String val : adornColumnList) {
            if (database.getTypeColumn(database.getColumnName(val), tableid))
                adornColumnTypeList.add("int");
            else
                adornColumnTypeList.add("string");
        }

//flag con1 con2
        ArrayList<String[]> tableFlagList = new ArrayList<String[]>();
        for (String adornColumn : adornColumnList) {
            tableFlagList.add(new String[]{"N", "", ""});
        }
        req.getSession().setAttribute("user", userVal);

        req.setAttribute("adornTableVal", adornTableList.size() > 0 ? adornTableList.get(0) : null);
        req.setAttribute("adornTableList", adornTableList);

        req.setAttribute("setNameVal", "请选择配置！");
        req.setAttribute("setNameList", setNameList);

        req.setAttribute("adornColumnList", adornColumnList);
        req.setAttribute("adornColumnTypeList", adornColumnTypeList);
        req.setAttribute("tableFlagList", tableFlagList);

        return "info.jsp";
    }
}
