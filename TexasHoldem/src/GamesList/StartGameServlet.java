package GamesList;

import API.EngineManager;
import Exceptions.GameIDNotProvidedException;
import Exceptions.PlayerAlreadyInGameException;
import Exceptions.UserNameNotProvidedException;
import GameManager.GameManager;
import Utils.ServletUtils;
import com.google.gson.Gson;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class StartGameServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username= ServletUtils.getSessionUser(request);

        if(username==null)
        {
            ServletUtils.SendErrorMessage("User don't registered", response);
        }
        else {
            String game_id=getManager().IsPlayerInReadyGame(username);
            if(game_id!=null){
                Gson json=new Gson();
                Map<String,String> 
                //"localhost:8080/pages/lobby/lobby.html";
                //"./pages/table/table.html";

                //"/page/lobby/page/table/table.html";
                ServletUtils.SendRedirectURL("/pages/table/table.html",response);
            }
            else
            {
                try (PrintWriter out = response.getWriter()) {
                    out.println("false");
                    out.flush();
                }
            }
        }
    }

    private EngineManager getManager()
    {
        ServletContext context=getServletContext();
        Object objManager=context.getAttribute("EngineManager");

        if(objManager!= null)
        {
            return (EngineManager) context.getAttribute("EngineManager");
        }
        else
        {
            EngineManager manager=new GameManager();
            context.setAttribute("EngineManager",manager);
            return (EngineManager) context.getAttribute("EngineManager");
        }
    }
}


