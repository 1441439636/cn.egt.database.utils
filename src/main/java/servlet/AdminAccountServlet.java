package servlet;

import db.Database;
import utils.ShowRequestUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by 14414 on 2017/4/25.
 */
public class AdminAccountServlet extends BaseServlet {
    /**
     * account操作
     *
     * @param req
     * @param res
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String accountUI(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        ShowRequestUtil.showParams(req);
        Database database = (Database) req.getSession().getAttribute("database");
        //String[] account_id name password
        ArrayList<String[]> accountList = database.getAllAccount();
        req.setAttribute("accountList", accountList);
        return "/admin/account/account_edit.jsp";
    }

    public String addAccount(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        ShowRequestUtil.showParams(req);
        Database database = (Database) req.getSession().getAttribute("database");
        String acountName = req.getParameter("addAcountNameVal");
        String acountPasswd = req.getParameter("addAcountPasswdVal");
        int flag = database.addAccount(acountName, acountPasswd);
        if (flag == -1) {
            req.setAttribute("msg", "addAcount1");
            return "404.jsp";
        } else if (flag == -2) {
            req.setAttribute("msg", "addAcount2");
            return "404.jsp";
        }
        //String[] account_id name password
        ArrayList<String[]> accountList = database.getAllAccount();
        req.setAttribute("accountList", accountList);
        return "/admin/account/account_edit.jsp";
    }

    public String updateAccount(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        ShowRequestUtil.showParams(req);
        Database database = (Database) req.getSession().getAttribute("database");
        int accountId = Integer.parseInt(req.getParameter("accountIdVal").trim());
        String acountName = req.getParameter("accountNameVal");
        String acountPasswd = req.getParameter("accountPasswdVal");
        database.updateAccount(accountId, acountName, acountPasswd);
        //String[] account_id name password
        ArrayList<String[]> accountList = database.getAllAccount();
        req.setAttribute("accountList", accountList);
        return "/admin/account/account_edit.jsp";
    }

    public String deleteAccount(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        ShowRequestUtil.showParams(req);
        Database database = (Database) req.getSession().getAttribute("database");
        int accountId = Integer.parseInt(req.getParameter("accountIdVal"));
        database.deleteAccount(accountId);
        //String[] account_id name password
        ArrayList<String[]> accountList = database.getAllAccount();
        req.setAttribute("accountList", accountList);
        return "/admin/account/account_edit.jsp";
    }

    /**
     * role account操作
     *
     * @param req
     * @param res
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String roleAccountUI(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        ShowRequestUtil.showParams(req);
        Database database = (Database) req.getSession().getAttribute("database");
        //type！=0 取出root用户  id name
        ArrayList<String[]> roleList = database.getRoleList(1);
        String[] role = roleList.get(0);
        //id name passwd
        ArrayList<String[]> accountList = database.getAllAccount();
        ArrayList<String> accountIdList = database.getRoleAccountByRoleId(Integer.parseInt(role[0]));
        //把密码替换成true or false，表示是否属于该角色
        for (String[] account : accountList) {
            account[2] = "" + accountIdList.contains(account[0]);
        }
        req.setAttribute("roleList", roleList);
        req.setAttribute("role_name", role[1]);
        req.setAttribute("accountList", accountList);
        return "/admin/account/role_account_edit.jsp";
    }

    public String updateRoleAccount(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        ShowRequestUtil.showParams(req);
        Database database = (Database) req.getSession().getAttribute("database");
        //type！=0 取出root用户  id name
        ArrayList<String[]> roleList = database.getRoleList(1);
        String roleNameVal = req.getParameter("roleName");
//        int roleId = database.getRoleid(roleNameVal);
        int roleId = 0;
        for (String[] s : roleList) {
            if (s[1].equals(roleNameVal)) {
                roleId = Integer.parseInt(s[0]);
                break;
            }
        }
        //id name passwd
        ArrayList<String[]> accountList = database.getAllAccount();
        ArrayList<String> accountIdList = database.getRoleAccountByRoleId(roleId);
        //把密码替换成true or false，表示是否属于该角色
        for (String[] account : accountList) {
            account[2] = "" + accountIdList.contains(account[0]);
        }

        for (String[] accountVal : accountList) {
            int accountId = Integer.parseInt(accountVal[0]);
            if (req.getParameter("accountId" + accountId) != null && !accountVal[2].equals("true")) {
                accountVal[2] = "true";
                if (!database.hasRoleAccount(roleId, accountId))
                    database.addRoleAccount(roleId, accountId);
            } else if (req.getParameter("accountId" + accountId) == null && accountVal[2].equals("true")) {
                accountVal[2] = "false";
                database.deleteRoleAccount(roleId, accountId);
            }
        }
        req.setAttribute("roleList", roleList);
        req.setAttribute("role_name", roleNameVal);
        req.setAttribute("accountList", accountList);
        return "/admin/account/role_account_edit.jsp";
    }

    public String selectRoleAcount(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        ShowRequestUtil.showParams(req);
        Database database = (Database) req.getSession().getAttribute("database");
        //type！=0 取出root用户  id name
        ArrayList<String[]> roleList = database.getRoleList(1);
        String roleNameVal = req.getParameter("roleNameVal");
        int roleId = 0;
        if (roleNameVal == null || roleNameVal.equals("")) {
            roleNameVal = roleList.get(0)[1];
            roleId = Integer.parseInt(roleList.get(0)[0]);
        } else {
            for (String[] s : roleList) {
                if (s[1].equals(roleNameVal)) {
                    roleId = Integer.parseInt(s[0]);
                    break;
                }
            }
//            roleId = database.getRoleid(roleNameVal);
        }
        //id name passwd
        ArrayList<String[]> accountList = database.getAllAccount();
        ArrayList<String> accountIdList = database.getRoleAccountByRoleId(roleId);
        //把密码替换成true or false，表示是否属于该角色
        for (String[] account : accountList) {
            account[2] = "" + accountIdList.contains(account[0]);
        }
        req.setAttribute("roleList", roleList);
        req.setAttribute("role_name", roleNameVal);
        req.setAttribute("accountList", accountList);
        return "/admin/account/role_account_edit.jsp";
    }
}
