package filter;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by ZLS on 2017/5/10.
 */
public class urlFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {

    }


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        req.setCharacterEncoding("UTF-8");
        res.setCharacterEncoding("UTF-8");
        String basePath = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath();
        String url = req.getServletPath();
        System.out.println("URL = >  " + url);
        if (url.contains("RegistDatabase")) {
            System.out.println("-----------------  转发  RegistDatabase  ------------------- ");
            req.getRequestDispatcher("regist/regist.jsp").forward(req, res);
        } else if (url.contains("AdminLoginUI")) {
            System.out.println("-----------------  转发  AdminLoginUI  ------------------- ");
//            req.getRequestDispatcher( "admin/url/adminDatabaseadminLoginUI.jsp").forward(req,res);
            req.getRequestDispatcher("admin/index.jsp").forward(req, res);
        } else
            chain.doFilter(req, response);
    }


    public void destroy() {

    }
}
