package servlet;

import db.Database;
import service.impl.AdminRoleServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by 14414 on 2017/4/25.
 */
public class AdminRoleServlet extends BaseServlet {

    /**
     * rolePermissionUI
     *
     * @param req
     * @param res
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String rolePermissionUI(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        Database database = (Database) req.getSession().getAttribute("database");
        //获得所有角色，type=0 不获得root
        ArrayList<String[]> roleList = database.getRoleList(0);
        req.setAttribute("roleList", roleList);
        req.setAttribute("roleName", roleList.get(0)[1]);
        //获得所有翻译表
        ArrayList<String> adornTableNameList =  database.getAdornTablenameList();
        req.setAttribute("adornTableNameList", adornTableNameList);
        //起始表
        String adornTableName = adornTableNameList.get(0);
        req.setAttribute("adornTableName", adornTableName);
        //根据翻译名获得翻译字段
        ArrayList<String> adornColumnList = database.getAdornColumnList(adornTableName);
        req.setAttribute("adornColumnList", adornColumnList);
        //根据翻译字段的名字，获取字段是否属于该角色
        ArrayList<String> adornColumnFlag = new ArrayList<String>();
        for (String adornColumn : adornColumnList) {
            if (database.hasRolePermission(Integer.parseInt(roleList.get(0)[0]), database.getTableid(adornTableNameList.get(0)), adornColumn))
                adornColumnFlag.add("true");
            else
                adornColumnFlag.add("false");
        }
        req.setAttribute("adornColumnFlag", adornColumnFlag);

        return "/admin/role/role_permission_edit.jsp";
    }

    public String updateRolePermission(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Database database = (Database) req.getSession().getAttribute("database");
        //获得所有角色，type=0 不获得root
        ArrayList<String[]> roleList = database.getRoleList(0);
        String roleName = req.getParameter("roleName");
        if (roleName == null || roleName.trim().equals(""))
            roleName = roleList.get(0)[1];
        req.setAttribute("roleList", roleList);
        req.setAttribute("roleName", roleName);
        int roleId = database.getRoleid(roleName);

        //获得所有翻译表
        ArrayList<String> adornTableNameList = database.getAdornTablenameList();
        req.setAttribute("adornTableNameList", adornTableNameList);
        //如果jsp无信息传回，则默认为列表第一个
        String adornTableName = req.getParameter("adornTableName");
        if (adornTableName == null || adornTableName.trim().equals(""))
            adornTableName = adornTableNameList.get(0);
        req.setAttribute("adornTableName", adornTableName);
        int tableId = database.getTableid(adornTableName);

        //根据翻译表名获得翻译字段
        ArrayList<String> adornColumnList = database.getAdornColumnList(adornTableName);
        req.setAttribute("adornColumnList", adornColumnList);
        //获取页面flag;
        ArrayList<String> adornColumnFlag = new ArrayList<String>();
        ArrayList<String> adorn_nameList = new ArrayList<String>();
        for (int i = 0; i < adornColumnList.size(); i++) {
            if (req.getParameter("adornColumnFlag" + i) != null) {
                adornColumnFlag.add("true");
                adorn_nameList.add(adornColumnList.get(i));
            } else
                adornColumnFlag.add("false");
        }
        database.deleteRolePermission(roleId, tableId);
        if (adorn_nameList.size() > 0)
            database.addRolePermission(roleId, tableId, adorn_nameList);
        req.setAttribute("adornColumnFlag", adornColumnFlag);

        return "/admin/role/role_permission_edit.jsp";
    }

    public String selectRolePermission(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Database database = (Database) req.getSession().getAttribute("database");

        //获得所有角色，type=0 不获得root
        ArrayList<String[]> roleList = database.getRoleList(0);
        String roleName = req.getParameter("roleNameVal");
        if (roleName == null || roleName.trim().equals(""))
            roleName = roleList.get(0)[1];
        req.setAttribute("roleList", roleList);
        req.setAttribute("roleName", roleName);
        //获得所有翻译表
        ArrayList<String> adornTableNameList = database.getAdornTablenameList();
        req.setAttribute("adornTableNameList", adornTableNameList);
        //如果jsp无信息传回，则默认为列表第一个
        String adornTableName = req.getParameter("adornTableNameVal");
        if (adornTableName == null || adornTableName.trim().equals(""))
            adornTableName = adornTableNameList.get(0);
        req.setAttribute("adornTableName", adornTableName);
        int tableId = database.getTableid(adornTableName);

        //根据翻译表名获得翻译字段
        ArrayList<String> adornColumnList = database.getAdornColumnList(adornTableName);
        req.setAttribute("adornColumnList", adornColumnList);

        //根据翻译字段的名字，获取字段是否属于该角色
        ArrayList<String> adornColumnFlag = new ArrayList<String>();
        for (String adornColumn : adornColumnList) {
            if (database.hasRolePermission(database.getRoleid(roleName), tableId, adornColumn))
                adornColumnFlag.add("true");
            else
                adornColumnFlag.add("false");
        }
        req.setAttribute("adornColumnFlag", adornColumnFlag);

        return "/admin/role/role_permission_edit.jsp";
    }

    /**
     * roleUI
     *
     * @param req
     * @param res
     * @return
     * @throws ServletException
     * @throws IOException
     */

    public String roleUI(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Database database = (Database) req.getSession().getAttribute("database");
        //type = 0  不查询root
        ArrayList<String[]> roleList = database.getRoleList(0);
        req.setAttribute("roleList", roleList);
        return "/admin/role/role_edit.jsp";
    }


    public String addRole(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String add_role_name = req.getParameter("add_role_name");
        if (add_role_name == null || add_role_name.trim().equals("")) {
            req.setAttribute("msg", "addRole0");
            return "404.jsp";
        }
        Database database = (Database) req.getSession().getAttribute("database");
        int id = database.addRole(add_role_name);

        if (id == -1) {
            req.setAttribute("msg", "addRole1");
            return "404.jsp";
        } else if (id == -2) {
            req.setAttribute("msg", "addRole2");
            return "404.jsp";
        }
        //type = 0  不查询root
        ArrayList<String[]> roleList = database.getRoleList(0);
        req.setAttribute("roleList", roleList);
        return "/admin/role/role_edit.jsp";
    }

    public String updateRole(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String update_role_name = req.getParameter("update_role_name");
        if (update_role_name == null || update_role_name.trim().equals("")) {
            req.setAttribute("msg", "updateRole0");
            return "404.jsp";
        }
        Database database = (Database) req.getSession().getAttribute("database");
        int update_role_id = Integer.parseInt(req.getParameter("update_role_id"));
        if (update_role_id == -1) {
            req.setAttribute("msg", "updateRole1");
            return "404.jsp";
        }
        int flag = database.updateRole(update_role_id, update_role_name);
        if (flag == -1) {
            req.setAttribute("msg", "updateRole2");
            return "404.jsp";
        }
        //type = 0  不查询root
        ArrayList<String[]> roleList = database.getRoleList(0);
        req.setAttribute("roleList", roleList);
        return "/admin/role/role_edit.jsp";
    }

    public String deleteRole(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String delete_role_name = req.getParameter("delete_role_name");
        Database database = (Database) req.getSession().getAttribute("database");
        int update_role_id = database.getRoleid(delete_role_name);
        if (update_role_id == -1) {
            req.setAttribute("msg", "deleteRole1");
            return "404.jsp";
        }
        database.deleteRole(update_role_id);
        //type = 0  不查询root
        ArrayList<String[]> roleList = database.getRoleList(0);
        req.setAttribute("roleList", roleList);
        return "/admin/role/role_edit.jsp";
    }


}
