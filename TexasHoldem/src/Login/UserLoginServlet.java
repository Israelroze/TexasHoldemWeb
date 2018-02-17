package Login;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UserLoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("fileupload/form.html");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String,String[]> collection=request.getParameterMap();

        for(Map.Entry<String,String[]> entry : collection.entrySet())
        {
            System.out.println(entry.getKey());
            for(String str:entry.getValue())
            {
                System.out.println(entry.getKey());
            }
        }
    }
}
