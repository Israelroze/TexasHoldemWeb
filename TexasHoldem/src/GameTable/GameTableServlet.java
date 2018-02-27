package GameTable;

import Card.Card;
import Containers.GameData;
import Containers.HandData;
import Containers.UserData;
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
        processRequest(request,response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");


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

    public GameData TestGameData()
    {

        List<String> commCard  = new LinkedList<>();
        commCard.add("6D");
        commCard.add("5D");

        UserData p1 = new UserData("Avishay",100,2,2003,"Computer", commCard);
        UserData p2 = new UserData("p123",100,2,2003,"Computer",commCard);
        UserData p3 = new UserData("p345",100,2,2023403,"Computer",commCard);
        UserData p4 = new UserData("jho",100,2,223003,"Human",commCard);
        UserData p5 = new UserData("sdf",100,2,2003,"Computer",commCard);
        UserData p6 = new UserData("sdf",1234200,23,234003,"Computer",commCard);
        List<UserData> UserTest  = new LinkedList<>();
        UserTest.add(p1);
        UserTest.add(p2);
        UserTest.add(p3);
        UserTest.add(p4);
        UserTest.add(p5);
        UserTest.add(p6);


        HandData handTest= new HandData(1023, commCard);
        GameData gameTest = new GameData(10,UserTest,10,4,6);

        return gameTest;
    }

    private void logServerMessage(String message){
        System.out.println(message);
    }

}


