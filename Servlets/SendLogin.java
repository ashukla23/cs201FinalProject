

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SendLogin
 */
@WebServlet("/SendLogin")
public class SendLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendLogin() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		String username = request.getParameter("uname");
		String password = request.getParameter("psw");
		int balance = 1000;
		//System.out.println(username+" "+password);
		boolean condition = false;
		if(condition) {
			response.sendRedirect("/CSCI_201_FinalProject/WelcomeBack.html?name="+username+"&balance="+balance);
		}
		else {
			response.sendRedirect("/CSCI_201_FinalProject/WelcomeBack.html?name=Guest&balance=1000");
			//response.sendRedirect("/CSCI_201_FinalProject/index.html");
		}
		
	}
}
