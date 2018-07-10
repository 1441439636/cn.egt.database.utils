package servlet;

import entity.DB;
import service.AdminDatabaseService;
import service.impl.AdminDatabaseServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by 14414 on 2017/4/25.
 */
public class AdminDatabaseServlet extends BaseServlet {
    /**
     * 添加配置信息
     *
     * @param req
     * @param res
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String addDB(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        DB db = new DB();
        db.setDbType(req.getParameter("dbType"));
        db.setUsername(req.getParameter("dbUsername"));
        db.setPasswrod(req.getParameter("dbPasswd"));
        db.setAddress(req.getParameter("dbAddress"));
        db.setDatabase(req.getParameter("database"));
        AdminDatabaseService adminDatabaseService = new AdminDatabaseServiceImpl();
        boolean flag = adminDatabaseService.write(db);
        System.out.println("  ----------       addDB      ----------  " + db.toString());
        if (flag)
            req.setAttribute("msg", "addDBSuccess");
        else
            req.setAttribute("msg", "addDBFail");
        return "/admin/msg_edit.jsp";
    }
}
