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
 * Servlet implementation class Validate
 */
@WebServlet("/Validate")
public class Validate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Validate() {
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
		
		PrintWriter out = response.getWriter();
		
			String username = request.getParameter("CreateAccount_Username");
			String password = request.getParameter("CreateAccount_Password");
			Connection conn = null;
			Statement st = null;
			ResultSet rs = null;
			try {
				Class.forName("com.mysql.jdbc.Driver"); 
				conn = DriverManager.getConnection("jdbc:mysql://localhost/blackjack?user=root&password=root"); 
				st = conn.createStatement();
				//retreiving password DATA
				String query = "SELECT blackjack.usernameandpassword.Username FROM blackjack.usernameandpassword WHERE blackjack.usernameandpassword.Username = '"+ username +"';";
				
				rs = st.executeQuery(query);
				int i = 0;
				while(rs.next()) {
					i++;
				}
				if(i > 0 ) {//username is taken
					response.sendRedirect("/CSCI_201_FinalProject/NewAccount.html?taken=yes");
				}
				else {//new user
					String newquery = "INSERT INTO usernameandpassword (Username, Password, Balance) VALUES ('"+username+"', '"+password+"', 1500);";
					st.executeUpdate(newquery);
					response.sendRedirect("/CSCI_201_FinalProject/WelcomeBack.html?name="+username+"&balance=1500");
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
			//if(password.compareTo(Password) == 0) {
				//response.sendRedirect("/CSCI_201_FinalProject/GamePage.html?name="+username+"&balance="+1000);
			//}
			//else {
				out.println("Invalid Password/Username Combination");
			//}
		
	}
}
