package GamesList;

import API.Engine;
import API.EngineManager;
import Containers.GameData;
import Containers.GameNodeData;
import GameManager.GameManager;
import UserManager.UserManager;
import Utils.ServletUtils;
import com.google.gson.Gson;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GameListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username= ServletUtils.getSessionUser(request);

        if(username==null)
        {
            ServletUtils.SendErrorMessage("User dont registered", response);
        }
        else {
            Gson gson = new Gson();
            List<GameNodeData> gamelist = new LinkedList<>();
            for (Engine game : getManager().GetGamesList()) {
                gamelist.add(new GameNodeData(game.GetGameID(), game.GetUploaderName(), game.GetBuy(), game.GetBig(), game.GetSmall(), game.GetNumberOfHands(), game.GetTotalNumberOfPlayers(), game.GetRegisteredNumOfPlayers()));
            }

            if (!gamelist.isEmpty()) {
                response.setContentType("application/json");
                try (PrintWriter out = response.getWriter()) {

                    String json = gson.toJson(gamelist);

                    System.out.println(json); //DEBUG

                    out.println(json);
                    out.flush();
                }
            } else {
                ServletUtils.SendErrorMessage("Game list dont exist!", response);
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
