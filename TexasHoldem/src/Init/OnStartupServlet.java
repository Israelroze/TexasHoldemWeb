package Init;

import API.EngineManager;
import GameManager.GameManager;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class OnStartupServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();

        EngineManager manager=new GameManager();
        ServletContext context=getServletContext();
        context.setAttribute("EngineManager",manager);
    }

}
