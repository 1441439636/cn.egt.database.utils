package service;

import entity.DB;
import servlet.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by 14414 on 2017/4/25.
 */
public interface AdminDatabaseService {
    boolean write(DB db);
    DB readDB();
}
