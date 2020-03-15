package serv7;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class TakeOrder
 */
@WebServlet("/TakeOrder")
public class TakeOrder extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TakeOrder() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();	
		response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
		if (request.getSession(false) == null) 
		{
			JSONObject obj = new JSONObject();
			try {
				obj.put("status", false);
				obj.put("message", "Invalid session");
				out.print(obj);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else 
		{
			String student_id = (String)request.getSession().getAttribute("student_id");
			String ordered_items = (String)request.getParameter("ordered_items");
			String time_of_collecting = (String)request.getParameter("time_of_collecting"); 
			String canteen_id = (String)request.getParameter("canteen_id"); 
			out.print(DbHandler.takeorder(student_id,time_of_collecting,ordered_items,canteen_id));
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}