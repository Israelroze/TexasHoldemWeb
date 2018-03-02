package GameTable;

import Card.Card;
import UserManager.UserManager;
import Utils.ServletUtils;
import com.google.gson.Gson;
import com.sun.xml.internal.ws.api.ha.StickyFeature;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


public class GameTableServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       // processRequest(request,response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        logServerMessage("I am here");

        String game_id=request.getParameter("gameID");
        ServletUtils.setSessionParam(request,"gameID",game_id);



        try (PrintWriter out = response.getWriter()) {

            GameData testGameData ;
            testGameData= TestGameData();
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(testGameData);
            logServerMessage(jsonResponse);
            out.print(jsonResponse);
            out.flush();


        }
    }



    class GameData{

        final private int dataVerion;
        final private List<UserData> userData;
        final private int  num_of_games;
        final private int current_game_number;
        final private int number_of_players;


        public GameData(int dataVerion, List<UserData> userData, int num_of_games, int current_game_number, int number_of_players) {
            this.dataVerion = dataVerion;
            this.userData = userData;
            this.num_of_games = num_of_games;
            this.current_game_number = current_game_number;
            this.number_of_players = number_of_players;

        }
    }

    class HandData
    {
        final private  int pot;
        final private List<String> communityCards;

        public HandData(int pot, List<String> communityCards) {
            this.pot = pot;
            this.communityCards = communityCards;
        }
    }

    class UserData
    {
        final private String name;
        final private int bid;
        final private int num_of_wins;
        final private int money;
        final private String type; //computer or human
        final private List<String> cards;
        final private String role; //small, big or dealer


        public UserData(String name, int bid, int num_of_wins, int money, String type,List<String> cards, String role) {
            this.name = name;
            this.bid = bid;
            this.num_of_wins = num_of_wins;
            this.money = money;
            this.type = type;
            this.cards = cards;
            this.role= role;
        }
    }



    public GameData TestGameData()
    {

        List<String> commCard  = new LinkedList<>();
        commCard.add("6D");
        commCard.add("5D");

        UserData p1 = new UserData("Avishay",100,2,2003,"Computer", commCard,"big");
        UserData p2 = new UserData("p123",100,2,2003,"Computer",commCard,"small");
        UserData p3 = new UserData("p345",100,2,2023403,"Computer",commCard,"dealer");
        UserData p4 = new UserData("jho",100,2,223003,"Human",commCard,"dealer");
        UserData p5 = new UserData("sdf",100,2,2003,"Computer",commCard,"small");
        UserData p6 = new UserData("sdf",1234200,23,234003,"Computer",commCard,"big");
        List<UserData> UserTest  = new LinkedList<>();
        UserTest.add(p1);
        UserTest.add(p2);
        UserTest.add(p3);
        UserTest.add(p4);
        UserTest.add(p5);
        UserTest.add(p6);


        HandData handTest= new HandData(1023, commCard);
        GameData gameTest = new GameData(10,UserTest,10,4,5);

        return gameTest;
    }

    private void logServerMessage(String message){
        System.out.println(message);
    }

}


