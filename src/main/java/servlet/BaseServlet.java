package servlet;

import utils.ShowRequestUtil;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;







/**
 * Created by 14414 on 2017/4/19.
 */
public class BaseServlet extends HttpServlet {
    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        try {
            Class node = this.getClass();
            String methodVal = req.getParameter("method");
            System.out.println("ClassName=" + this.getClass().getName() + "    methodVal=" + methodVal);
            if (methodVal == null)
                methodVal = "index";
            Method method = node.getMethod(methodVal, HttpServletRequest.class, HttpServletResponse.class);
//            获取请求转发的路径，无为null
            String urlVal = (String) method.invoke(this, req, res);
            System.out.println("ClassName=" + this.getClass().getName() + "    urlVal=" + urlVal);
            if (urlVal != null)
                req.getRequestDispatcher(urlVal).forward(req, res);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public String index(HttpServletRequest re, HttpServletResponse res) throws ServletException, IOException {
        return "index.jsp";
    }
}
