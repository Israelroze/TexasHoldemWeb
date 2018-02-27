package GamesList;

import API.Engine;
import API.EngineManager;
import Containers.GameData;
import GameManager.GameManager;
import com.google.gson.Gson;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class GameListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();

        List<GameData> gamelist;

        for(Engine game: getManager().GetGamesList())
        {   
            gamelist.add(new GameData())
        }


    }

    private EngineManager getManager()
    {
        EngineManager manager=new GameManager();
        ServletContext context=getServletContext();
        return (EngineManager) context.getAttribute("EngineManager");
    }
}
