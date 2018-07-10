package servlet;

import dao.impl.RegistDaoImpl;
import db.DBFactory;
import db.Database;
import entity.Admin;
import entity.DB;
import service.impl.AdminLoginServiceImpl;
import utils.DBConn;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by 14414 on 2017/4/25.
 */
public class AdminLoginServlet extends BaseServlet {
    public String adminHomeUI(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Database database = DBConn.createDB();
        req.getSession().setAttribute("database", database);
        Admin admin = new Admin();
        admin.setAdminName(req.getParameter("username"));
        admin.setPassword(req.getParameter("password"));
        AdminLoginServiceImpl adminLoginService=new AdminLoginServiceImpl();
//        adminLoginService.adminConnect(admin);
        if (database.adminConnect(admin.getAdminName(), admin.getPassword())) {
            req.getSession().setAttribute("admin", admin);
            return "/admin/home.jsp";
        } else {
            req.setAttribute("msg", "adminHomeUI");
            return "404.jsp";
        }
    }

    public String adminLoginUI(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        return "/admin/index.jsp";
    }

}
