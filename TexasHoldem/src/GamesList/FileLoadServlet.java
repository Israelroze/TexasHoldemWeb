package GamesList;

//import API.Engine;
//import Exceptions.*;
import API.Engine;
import API.EngineManager;
import Exceptions.*;
import Game.Game;
import GameManager.GameManager;
import Utils.ServletUtils;

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
           getManager().AddGame(fstream, ServletUtils.getSessionUser(request));
            out.println("ok");
            out.flush();
        } catch (PlayerdIDmismatchException e) {
            ServletUtils.SendErrorMessage("Wrong player id",response);
        } catch (BigSmallMismatchException e) {
            ServletUtils.SendErrorMessage("Big Small Mismatch",response);
            //out.println(" Big Small Mismatch ");
        } catch (HandsCountDevideException e) {
            ServletUtils.SendErrorMessage("Num of hands not divided by num of players ",response);
            //out.println(" Num of hands not divided by num of players ");
        } catch (MaxBigMoreThanHalfBuyException e) {
            ServletUtils.SendErrorMessage("Maximum Big more that the half of the buy limit",response);
            //out.println(" Maximum Big more that the half of the buy limit");
        } catch (HandsCountSmallerException e) {
            ServletUtils.SendErrorMessage("Hands Count smaller that the number of players ",response);
            //out.println(" Hands Count smaller that the number of players ");
        } catch (MinusZeroValueException e) {
            ServletUtils.SendErrorMessage("One of the value is zero or negative. ",response);
            //out.println(" One of the value is zero or negative. ");
        } catch (UnexpectedObjectException e) {
            ServletUtils.SendErrorMessage("Unexpected error",response);
            //out.println("Unexpected error");
        } catch (JAXBException e) {
            ServletUtils.SendErrorMessage(" JAXB Error "+e.getMessage(),response);
            //out.println(" JAXB Error "+e.getMessage());
        } catch (GameStartedException e) {
            ServletUtils.SendErrorMessage(" Game already started",response);
            //out.println(" Game already started");
        } catch (BigBiggerThanBuyException e) {
            ServletUtils.SendErrorMessage(" Big value bigger that the Buy value.",response);
            //out.println(" Big value bigger that the Buy value.");
        } catch (GameTitleAllreadyExistException e) {
            ServletUtils.SendErrorMessage(" Game with the same title allready exist.",response);
            //out.println(" Game with the same title allready exist.");
        }
    }

    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
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