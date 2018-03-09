package GamesList;

import API.Engine;
import API.EngineManager;
import Card.Card;
import Containers.SideBarData;
import Containers.UserData;
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
import java.util.LinkedList;
import java.util.List;

public class SideBarDataServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username= ServletUtils.getSessionUser(request);
        response.setContentType("application/json");
        if(username==null)
        {
            ServletUtils.SendErrorMessage("User don't registered", response);
        }
        else {
            //String game_id=getManager().IsPlayerInReadyGame(username);
            String game_id = ServletUtils.getSessionParam(request, "gameID");
            if(game_id!=null) {
                try (PrintWriter out = response.getWriter()) {
                    String res=BuildSideBarData(getManager().getChatData(game_id),getManager().GetGame(game_id),username);
                    out.println(res);
                    out.flush();
                }
            }
        }
    }

    private String BuildSideBarData(List<String> chatdata, Engine game,String username)
    {
        Gson json=new Gson();

        List<UserData> users=new LinkedList<>();

        for(int i=0;i<game.GetRegisteredNumOfPlayers();i++)
        {
            String p_name=game.GetPlayerName(i);

            List<String> p_cards= new LinkedList<>();

            if(p_name.equals(username))//the player from the session
            {
                for(Card card:game.GetPlayersCards(i) )
                {
                    p_cards.add(card.toString());
                }
            }
            else
            {
                p_cards.add("back");
                p_cards.add("back");
            }

            String role;
            if (game.GetPlayerIsDealer(i)) role = "dealer";
            else if(game.GetPlayerIsBig(i)) role = "big";
            else if(game.GetPlayerIsSmall(i)) role = "small";
            else role ="no_role";

            users.add(new UserData(game.GetPlayerName(i),game.GetPlayerNumOfWins(i),game.GetPlayerNumOfBuy(i),game.GetPlayerPot(i),getManager().GetUserType(game.GetPlayerName(i)),null ,null,getManager().IsYourTurn(game,game.GetPlayerName(i))));
        }

        SideBarData s_data=new SideBarData(users,chatdata);
        return json.toJson(s_data);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{

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
