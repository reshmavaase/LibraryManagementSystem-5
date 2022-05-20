package Com.revature.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Com.revature.app.ConnectionFactory;
public class Librarian {
	UserManagement um=new UserManagement();
	public boolean login(int userid,String password ) throws ClassNotFoundException, SQLException {
		int username1=userid;
		String password1=password;
		String role=UserManagement.validateUser(username1, password1);
		if(role!=null) {
			return true;
		}
		else {
			return false;
		}
	}
	public boolean add_book() throws ClassNotFoundException, SQLException {
		Book bk=new Book();
		return bk.insertbook();
	}
	public void display() throws ClassNotFoundException, SQLException {
		Connection co = ConnectionFactory.getConnection();
		System.out.println("BOOKS AVAILABLE");
		String s="Select * from book";
		Statement st=co.createStatement();
		ResultSet s1=st.executeQuery(s);
		while(s1.next()) {
			System.out.println(s1.getInt(1)+" "+s1.getString(2)+" "+s1.getInt(3));
		}
	}
	public boolean issue_Book(int userid, int bookid) throws SQLException, ClassNotFoundException {
		Connection conn = ConnectionFactory.getConnection();
		boolean bool=false;
		int username1=userid;
		String sql="select password from user where userid=?";
		PreparedStatement psmt=conn.prepareStatement(sql);
		psmt.setInt(1,userid);
		ResultSet rs= psmt.executeQuery();
		if(rs.next()) {
		String password1=rs.getString("password");
		String role=UserManagement.validateUser(username1, password1);
		if(role!=null) 
		{
			  String sql1="select * from book where bookid=?";
			  PreparedStatement psmt1=conn.prepareStatement(sql1);
			  psmt1.setInt(1,bookid);		
			  ResultSet rs1= psmt1.executeQuery();
			  if(rs1.next()) {
				  int q=rs1.getInt(3);
				  //checking book availability
				   //status update
				  if(q>0) {
					String sql3="insert into bookissue values(?,?.?,?)";
				    PreparedStatement pst2=conn.prepareStatement(sql3);
				    pst2.setInt(1,bookid);
				    pst2.setString(2,rs1.getString(2));
				    pst2.setInt(3,rs1.getInt(3));
					pst2.setInt(4,userid);
					if(pst2.executeUpdate()==1) {
						    System.out.println("book issue updated");
							System.out.println("");
						    q=q-1;
							String sql2="update book set count=? where bookid=?";
							PreparedStatement pst3=conn.prepareStatement(sql2);
							pst3.setInt(2,bookid);
							pst3.setInt(1,q);
						    if(pst3.executeUpdate()==1)
						    {
							    bool=true;
							}
							else
							{
								bool=false;
							}
					  }
					   else {
						   System.out.println("book issue is not completed");
						   System.out.println();
						   System.out.println("please try again");
					   		}
				  }
				  else {
					  System.out.println("no books available");
				  }
			  }
		  }
		}
	
		return bool;
	}
	public boolean return_Book(int userid,int bid) throws ClassNotFoundException, SQLException{
		  Connection conn = ConnectionFactory.getConnection();
		  boolean b=false;
		  String sql1="select bookid from bookissue where userid=?";
		  PreparedStatement psmt1=conn.prepareStatement(sql1);
		  psmt1.setInt(1,userid);	
		  ResultSet rs2= psmt1.executeQuery();
		  if(rs2.next()) {
			  String sql2="select * from bookissue where bookid=?";
			  PreparedStatement psmt2=conn.prepareStatement(sql2);
			  psmt2.setInt(1,bid);		
			  ResultSet rs1= psmt2.executeQuery();
			  if(rs1.next()) {
				  int q1=rs1.getInt(3);
				  if(q1>=0)
				  {
				     //status update
					 String sql4="delete from bookissue where bookid = ?";
					 PreparedStatement pst3=conn.prepareStatement(sql4);
					 pst3.setInt(1, bid);
					 if(pst3.executeUpdate()==1){
					     q1=q1+1;
						 String sql21="update book set count=? where bookid=?";
						 PreparedStatement pst2=conn.prepareStatement(sql21);
						 pst2.setInt(1,q1);
						 pst2.setInt(2,bid);
						 if(pst2.executeUpdate()==1)
					     {
						     b = true;
						 }
						 else
						 {
							 b = false;
						 }  
					 }
					 else {
						 System.out.println("book issue updatation not completed");
					 }
				  }
			  }
		  return b;
	}

}
