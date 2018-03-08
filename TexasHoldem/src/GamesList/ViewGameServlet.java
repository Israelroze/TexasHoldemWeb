package GamesList;

import API.EngineManager;
import Exceptions.GameIDNotProvidedException;
import Exceptions.PlayerAlreadyInGameException;
import Exceptions.UserNameNotProvidedException;
import GameManager.GameManager;
import Utils.ServletUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ViewGameServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username= ServletUtils.getSessionUser(request);
        if(username==null)
        {
            ServletUtils.SendErrorMessage("User don't registered", response);
        }
        else {
            Object objGameID=request.getParameter("GameId");
            if(objGameID!=null)
            {
                ServletUtils.setSessionParam(request,"gameID",(String)objGameID);
                ServletUtils.SendRedirectURL("../table/table.html",response);
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
