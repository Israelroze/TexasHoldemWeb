package GamesList;

import API.InterfaceAPI;
import com.google.gson.Gson;
import Game.Game;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.http.HTTPException;
import java.io.IOException;

public class GameListServlet extends HttpServlet {

    public class GameData
    {
        String name;
        String user_name;
        int num_of_hands;
        int buy;
        int total_players;
        int current_players;
        int big;
        int small;

        public GameData(InterfaceAPI game)
        {
            this.num_of_hands=game.GetNumberOfHands();

        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();


    }

}
