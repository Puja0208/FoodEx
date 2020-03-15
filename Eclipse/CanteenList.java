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
 * Servlet implementation class SeePosts
 */
@WebServlet("/CanteenList")
public class CanteenList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public CanteenList() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();	
		response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
	    JSONObject obj = new JSONObject();
		if (request.getSession(false) == null) 
		{
			
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
			out.print(DbHandler.canteenList(student_id));
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