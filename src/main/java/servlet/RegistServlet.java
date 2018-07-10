package servlet;

import entity.DB;
import service.impl.AdminDatabaseServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Created by ZLS on 2017/5/10.
 */
@WebServlet(name = "RegistServlet")
public class RegistServlet extends BaseServlet {
    /**
     * 配置默认显示UI
     *
     * @param req
     * @param res
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String registUI(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        System.out.println("  ----------  RegistServlet databaseUI   ------------");
        DB db = new AdminDatabaseServiceImpl().readDB();
        System.out.println("   db.toString   " + db.toString());
        if (db == null)
            req.setAttribute("db", new DB());
        else
            req.setAttribute("db", db);
        return "/regist/regist.jsp";
    }
}
