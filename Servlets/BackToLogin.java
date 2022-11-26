

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
 * Servlet implementation class BackToLogin
 */
@WebServlet("/BackToLogin")
public class BackToLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BackToLogin() {
        super();
        // TODO Auto-generated constructor stub
    }

protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
			String status = request.getParameter("logout");
			String username = request.getParameter("username");
			String balance = request.getParameter("balance");
			if(status.equals("false")) {
				response.sendRedirect("/CSCI_201_FinalProject/GamePage.html?name="+username+"&balance="+balance);
			}else {
				if(!username.equals("Guest")) {
					Connection conn = null;
					Statement st = null;
					try {
						Class.forName("com.mysql.jdbc.Driver"); 
						conn = DriverManager.getConnection("jdbc:mysql://localhost/blackjack?user=root&password=root"); 
						st = conn.createStatement();
						//retreiving password DATA
						String query = "UPDATE blackjack.usernameandpassword SET Balance = "+balance+" WHERE blackjack.usernameandpassword.Username = '"+username+"';";
						
						int i = st.executeUpdate(query);
						
					}
					catch (SQLException sql_e) {
						System.out.println (sql_e.getMessage()); 
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					finally {
						try {
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
				}
				
				response.sendRedirect("/CSCI_201_FinalProject/index.html");
			}
		
		
	}

}
