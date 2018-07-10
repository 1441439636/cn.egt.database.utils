package servlet;

import db.Database;
import service.AdminColumnService;
import service.impl.AdminColumnServiceImpl;
import utils.ArrayToStringUtil;
import utils.ShowRequestUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by 14414 on 2017/4/25.
 */
public class AdminColumnServlet extends BaseServlet {
    public String columnUI(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        AdminColumnService adminColumnService = new AdminColumnServiceImpl();

        Database database = (Database) req.getSession().getAttribute("database");
        //获取视图信息
        ArrayList<String> viewList = database.getUnadornViewList();
        String tableName = null;
        tableName = req.getParameter("tableName");
        //获取某一table表的所有信息
        if (tableName == null)
            tableName = viewList.get(0);
        int table_id = database.getidByunadornname(tableName);
        String tableAdornName = database.getAdornTableName(tableName);
        ArrayList<String> columnList = database.getColumnList(tableName);
        ArrayList<ArrayList<String>> columnAdornList = new ArrayList<ArrayList<String>>();

        for (String s : columnList) {
            //获取字段信息，flag、adorn_name、no.
            ArrayList<String> columnAdorn = database.getRecord(table_id, s);
            if (columnAdorn == null) {
                columnAdorn = new ArrayList<String>();
                columnAdorn.add("N");
                columnAdorn.add("");
                columnAdorn.add("1");
            }
            //column 字段名
            columnAdorn.add(s);
            columnAdornList.add(columnAdorn);
        }
        req.setAttribute("tableName", tableName);
        req.setAttribute("tableAdornName", tableAdornName);
        req.setAttribute("viewList", viewList);
        req.setAttribute("columnAdornList", columnAdornList);
        return "/admin/column/column_edit.jsp";
    }

    public String updateColumn(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        AdminColumnService adminColumnService = new AdminColumnServiceImpl();
        Database database = (Database) req.getSession().getAttribute("database");
        //获取视图信息
        ArrayList<String> viewList = database.getUnadornViewList();
        String tableName = null;
        tableName = req.getParameter("tableName");
        String tableAdornName = req.getParameter("tableAdornName");
        database.setTableName(tableName, tableAdornName);
        String[] columnName = req.getParameterValues("columnName");
        String[] columnFlag = new String[columnName.length];
        for (int i = 1; i <= columnName.length; i++) {
            columnFlag[i - 1] = req.getParameter("flag" + i);
        }
        String[] columnAdornName = req.getParameterValues("columnAdornName");
        String[] columnNo = req.getParameterValues("columnNo");
        for (int i = 0; i < columnName.length; i++) {
            String flag;
            if (columnFlag[i] != null)
                flag = "Y";
            else flag = "N";
            database.setColname(tableName, columnName[i], flag, columnAdornName[i], Integer.parseInt(columnNo[i]));
        }
        //获取某一table表的所有信息
        if (tableName == null)
            tableName = viewList.get(0);
        int table_id = database.getidByunadornname(tableName);
        ArrayList<String> columnList = database.getColumnList(tableName);
        ArrayList<ArrayList<String>> columnAdornList = new ArrayList<ArrayList<String>>();
        for (String s : columnList) {
            //获取字段信息，flag、adorn_name、no.
            ArrayList<String> columnAdorn = database.getRecord(table_id, s);
            if (columnAdorn == null) {
                columnAdorn = new ArrayList<String>();
                columnAdorn.add("N");
                columnAdorn.add("");
                columnAdorn.add("1");
            }
            //column 字段名
            columnAdorn.add(s);
            columnAdornList.add(columnAdorn);
        }
        req.setAttribute("tableName", tableName);
        req.setAttribute("tableAdornName", tableAdornName);
        req.setAttribute("viewList", viewList);
        req.setAttribute("columnAdornList", columnAdornList);

        return "/admin/column/column_edit.jsp";
    }

    ///*********************************
    public String refreshUI(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        AdminColumnService adminColumnService = new AdminColumnServiceImpl();
        Database database = (Database) req.getSession().getAttribute("database");
        //获取视图信息
        ArrayList<String> viewList = database.getUnadornViewList();
        String tableName = req.getParameter("tableName");
        //获取某一table表的所有信息
        if (tableName == null)
            tableName = viewList.get(0);
        int table_id = database.getidByunadornname(tableName);
        ArrayList<String> columnList = database.getColumnList(tableName);
        ArrayList<ArrayList<String>> columnAdornList = new ArrayList<ArrayList<String>>();
        for (String s : columnList) {
            //获取字段信息，flag、adorn_name、no.
            ArrayList<String> columnAdorn = database.getRecord(table_id, s);
            if (columnAdorn == null) {
                columnAdorn = new ArrayList<String>();
                columnAdorn.add("N");
                columnAdorn.add("");
                columnAdorn.add("1");
            }
            //column 字段名
            columnAdorn.add(s);
            columnAdornList.add(columnAdorn);
        }
        String tableAdornName = database.getAdornTableName(tableName);
        req.setAttribute("tableName", tableName);
        req.setAttribute("tableAdornName", tableAdornName);
        req.setAttribute("viewList", viewList);
        req.setAttribute("columnAdornList", columnAdornList);
        return "/admin/column/column_edit.jsp";
    }
}
