

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GoToGame
 */
@WebServlet("/GoToGame")
public class GoToGame extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoToGame() {
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
		
		PrintWriter out = response.getWriter();
		//System.out.println(username+" "+password);
		//String username = request.getParameter("username");
		//String balance = request.getParameter("balance");
		
			//response.sendRedirect("/CSCI_201_FinalProject/GamePage.html?name="+username+"&balance="+balance);
		
		
		//System.out.println("WE made it");
		//if(fieldToValidate != null && fieldToValidate.equals("LoginUsername") && fieldToValidate != null && fieldToValidate.equals("password")) {
			String username = request.getParameter("LoginUsername");
			String password = request.getParameter("LoginPassword");
			Connection conn = null;
			Statement st = null;
			ResultSet rs = null;
			String Password = "";
			System.out.println(password);
			int balance = 0;
			try {
				Class.forName("com.mysql.jdbc.Driver"); 
				conn = DriverManager.getConnection("jdbc:mysql://localhost/blackjack?user=root&password=root"); 
				st = conn.createStatement();
				//retreiving password DATA
				rs = st.executeQuery("SELECT blackjack.usernameandpassword.Password, blackjack.usernameandpassword.Balance FROM blackjack.usernameandpassword WHERE blackjack.usernameandpassword.Username = '"+ username +"';");
				while (rs.next()) {
					Password = rs.getString(1);
					balance = rs.getInt(2);
				}
			}
			catch (SQLException sql_e) {
				System.out.println (sql_e.getMessage()); 
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			finally {
				try {
					if (rs != null) {
						rs.close(); 
					}
					if (st != null) { 
						st.close();
					}
					if (conn != null) {
						conn.close(); 
					}
				}
				catch (SQLException sqle) {
					System.out.println(sqle.getMessage()); 
				}
			}
			//check if the password that the unser inputs and the password in the SQL Table match
			if(password.compareTo(Password) == 0) {
				response.sendRedirect("/CSCI_201_FinalProject/WelcomeBack.html?name="+username+"&balance="+balance);
			}
			else {
				//alert("Invalid Password/Username Combination");
				response.sendRedirect("/CSCI_201_FinalProject/WelcomeBack.html?name=Guest&balance=1000");
			}
		}

		
	//}

}
