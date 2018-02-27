package GamesList;

//import API.Engine;
//import Exceptions.*;
import API.Engine;
import API.EngineManager;
import Exceptions.*;
import Game.Game;
import GameManager.GameManager;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

@MultipartConfig
public class FileLoadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("fileupload/form.html");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Part filepart=request.getPart("gameFile");
        InputStream fstream=filepart.getInputStream();

        // check fo xml extension
        String fname=filepart.getName();

        response.setContentType("text");
        PrintWriter out = response.getWriter();

        try{
           getManager().AddGame(fstream);

        } catch (PlayerdIDmismatchException e) {
            out.println("");
        } catch (BigSmallMismatchException e) {
            out.println(" Big Small Mismatch ");
        } catch (HandsCountDevideException e) {
            out.println(" Num of hands not divided by num of players ");
        } catch (MaxBigMoreThanHalfBuyException e) {
            out.println(" Maximum Big more that the half of the buy limit");
        } catch (HandsCountSmallerException e) {
            out.println(" Hands Count smaller that the number of players ");
        } catch (MinusZeroValueException e) {
            out.println(" One of the value is zero or negative. ");
        } catch (UnexpectedObjectException e) {
            out.println("Unexpected error");
        } catch (JAXBException e) {
            out.println(" JAXB Error "+e.getMessage());
        } catch (GameStartedException e) {
            out.println(" Game already started");
        } catch (BigBiggerThanBuyException e) {
            out.println(" Big value bigger that the Buy value.");
        } catch (GameTitleAllreadyExistException e) {
            out.println(" Game with the same title allready exist.");
        }
    }

    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }

    private EngineManager getManager()
    {
        EngineManager manager=new GameManager();
        ServletContext context=getServletContext();
        return (EngineManager) context.getAttribute("EngineManager");
    }
}