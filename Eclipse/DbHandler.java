package serv7;

import java.sql.*;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DbHandler {
	// connection strings
	private static String connString = "jdbc:postgresql://localhost:5970/dbproject";
	private static String userName = "blessy";
	private static String passWord = "";
	
	public static JSONObject register(String student_id, String name, String photo, String password, HttpServletRequest request){		
		JSONObject obj = new JSONObject();
		try{
			// Create the connection
			Connection conn = DriverManager.getConnection(connString, userName, passWord);
			String query = "insert into student(student_id,name,photo) values(?,?,?);";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, student_id);
			preparedStmt.setString(2, name);
			preparedStmt.setString(3, photo);
			if(preparedStmt.executeUpdate()>0)
			{
				String Query = "insert into stud_password(student_id,password) values(?,?);";
				PreparedStatement PreparedStmt = conn.prepareStatement(Query);
				PreparedStmt.setString(1, student_id);
				PreparedStmt.setString(2, password);
				if(PreparedStmt.executeUpdate()>0) {
					String Query1 = "insert into account values(nextval('accountid'), ?,?, ?);";
					PreparedStatement PreparedStmt1 = conn.prepareStatement(Query1);
					PreparedStmt1.setInt(1, 0);
					PreparedStmt1.setInt(2, 0);
					PreparedStmt1.setString(3, student_id);
					if(PreparedStmt1.executeUpdate()>0) {
						obj.put("status", true);
						obj.put("data","Successfully Registered");
					} else {
						obj.put("status", false);
						obj.put("data","Registration unsuccessful");
					}
				}
				else
				{
					obj.put("status",false);
					obj.put("message", "Registration unsuccessful");
				}					
			}
			else
			{
				obj.put("status",false);
				obj.put("message", "Student is already registered");
			}			
		} 
		catch(Exception e){
			e.printStackTrace();
		}
		return obj;
	}
	
	public static JSONObject authenticate(String student_id, String password,HttpServletRequest request){		
		JSONObject obj = new JSONObject();
		try{
			// Create the connection
			Connection conn = DriverManager.getConnection(connString, userName, passWord);
			String query = "select count(*) from stud_password where student_id=? and password=?;";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, student_id);
			preparedStmt.setString(2, password);
			ResultSet result =  preparedStmt.executeQuery();
			result.next();
			boolean ans = (result.getInt(1) > 0); 
			preparedStmt.close();
			conn.close();
			if(ans==true){
				request.getSession(true).setAttribute("student_id", student_id);
				obj.put("status",true);				
				obj.put("data", student_id);			
			}
			else{						
					obj.put("status",false);
					obj.put("message", "Authentication Failed");					
			}			
		} 
		catch(Exception e){
			e.printStackTrace();
		}
		return obj;
	}
	
	public static JSONObject authenticateM(String canteen_id, String password,HttpServletRequest request){		
		JSONObject obj = new JSONObject();
		try{
			// Create the connection
			Connection conn = DriverManager.getConnection(connString, userName, passWord);
			String query = "select count(*) from canteen_password where canteen_id=? and password=?;";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, canteen_id);
			preparedStmt.setString(2, password);
			ResultSet result =  preparedStmt.executeQuery();
			result.next();
			boolean ans = (result.getInt(1) > 0); 
			preparedStmt.close();
			conn.close();
			if(ans==true){
				request.getSession(true).setAttribute("canteen_id", canteen_id);
				obj.put("status",true);				
				obj.put("data", canteen_id);			
			}
			else{						
					obj.put("status",false);
					obj.put("message", "Authentication Failed");					
			}			
		} 
		catch(Exception e){
			e.printStackTrace();
		}
		return obj;
	}
	
	public static JSONObject createmenu(String canteen_id, String dish_id, Integer price)
	{
		JSONObject obj = new JSONObject();
		try{   
			Connection conn = DriverManager.getConnection(connString, userName, passWord);
			PreparedStatement pStmt = conn.prepareStatement("insert into menu(canteen_id,dish_id,price) values(?,?,?);");
			pStmt.setString(1, canteen_id);
			pStmt.setString(2, dish_id);
			pStmt.setInt(3, price);
			if(pStmt.executeUpdate()>0)
			{
				obj.put("status", true);
				obj.put("data","Dish added to the Menu");				
			}
			else
			{
				obj.put("status",false);
				obj.put("message", "Unable to add dish to the Menu");
			}	
			}catch (Exception sqle)
			{
				sqle.printStackTrace();
			}
		return obj;
	}
	
	
	public static JSONObject createdish(String name, String veg,String cooking_drn)
	{
		JSONObject obj = new JSONObject();
		try{   
			Connection conn = DriverManager.getConnection(connString, userName, passWord);
			PreparedStatement pStmt = conn.prepareStatement("insert into dish(dish_id,name,veg,cooking_drn) values(nextval('dishid'),?,?,?);");
			
			pStmt.setString(1, name);
			pStmt.setString(2,veg);
			pStmt.setString(3, cooking_drn);
			if(pStmt.executeUpdate()>0)
			{
				obj.put("status", true);
				obj.put("data","Created Dish Successfully");				
			}
			else
			{
				obj.put("status",false);
				obj.put("message", "Could not create dish");
			}	
			}catch (Exception sqle)
			{
				sqle.printStackTrace();
			}
		return obj;
	}
	
public static JSONObject deleteFromMenu(String canteen_id,String dish_id){
		
	JSONObject jsonObj = new JSONObject();
		try{
			// Create the connection
			Connection conn = DriverManager.getConnection(connString, userName, passWord);
			String query = "delete from menu where canteen_id=? and dish_id=?";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, canteen_id);
			preparedStmt.setString(2, dish_id);
			if(preparedStmt.executeUpdate()>0)
			{
				jsonObj.put("status", true);
				jsonObj.put("data","Deleted Dish Successfully");				
			}
			else
			{
				jsonObj.put("status",false);
				jsonObj.put("message", "Could not delete dish");
			}	
			
			 
		} catch(Exception e){
			e.printStackTrace();
		}
		
		return jsonObj;
	}
	
	public static JSONObject deauth(HttpServletRequest request) throws JSONException
	{
		JSONObject obj = new JSONObject();
		if (request.getSession(false) == null) {
			obj.put("status", false);
			obj.put("message", "Invalid Session");
			return obj;
		}else 
		{
			request.getSession(false).invalidate();
			obj.put("status", true);
			obj.put("data", "sucessfully logged out");
			return obj;
		}
	}
	
	public static JSONObject canteenHome(String canteen_id){
		JSONArray json = new JSONArray();
		JSONObject obj = new JSONObject();
		try (
		    Connection conn = DriverManager.getConnection(
		    		connString, userName, "");
		    PreparedStatement postSt = conn.prepareStatement("select * from canteen where canteen_id = ? ");
			PreparedStatement postSt1 = conn.prepareStatement("select count(*) from notification where to_id = ? and seen_status like 'UNSEEN'");
		){
			postSt.setString(1, canteen_id);
			ResultSet rs = postSt.executeQuery();
			json = ResultSetConverter(rs);
			
			postSt1.setString(1, canteen_id);
			ResultSet rs1 = postSt1.executeQuery();
			
			Integer count = 0;
			while(rs1.next()) {
				count = rs1.getInt(1);
			}
			obj.put("status",true);				
			obj.put("data",json );		
			obj.put("notifyCount", count);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	public static JSONObject canteenList(String student_id){
		JSONArray json = new JSONArray();
		JSONObject obj = new JSONObject();
		try (
		    Connection conn = DriverManager.getConnection(
		    		connString, userName, "");
		    PreparedStatement postSt = conn.prepareStatement("select * from canteen");
		){
			ResultSet rs = postSt.executeQuery();
			json = ResultSetConverter(rs);
			obj.put("status",true);				
			obj.put("data",json );			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
	public static JSONObject studentHome(String student_id){
		JSONArray json = new JSONArray();
		JSONObject obj = new JSONObject();
		try (
		    Connection conn = DriverManager.getConnection(
		    		connString, userName, "");
		    PreparedStatement postSt = conn.prepareStatement("select * from student where student_id=?");
			PreparedStatement postSt1 = conn.prepareStatement("select count(*) from notification where to_id = ? and seen_status like 'UNSEEN'");
		){
			postSt.setString(1,student_id);
			ResultSet rs = postSt.executeQuery();
			json = ResultSetConverter(rs);
			
			postSt1.setString(1,student_id);
			ResultSet rs1 = postSt1.executeQuery();
			Integer nc = 0;
			while(rs1.next()) {
				nc = rs1.getInt(1);
			}
			obj.put("status",true);				
			obj.put("data",json );
			obj.put("notifyCount", nc);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println("studtls" + obj);
		return obj;
	}
	
	public static JSONObject getMenu(String canteen_id){
		JSONArray json = new JSONArray();
		JSONObject obj = new JSONObject();
		try (
		    Connection conn = DriverManager.getConnection(
		    		connString, userName, "");
		    PreparedStatement postSt = conn.prepareStatement("select * from menu,dish where canteen_id = ? and menu.dish_id=dish.dish_id ");
		){
			postSt.setString(1, canteen_id);
			ResultSet rs = postSt.executeQuery();
			json = ResultSetConverter(rs);
			obj.put("status",true);				
			obj.put("data",json );			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	public static JSONObject getDishes(String canteen_id){
		JSONArray json = new JSONArray();
		JSONObject obj = new JSONObject();
		try (
		    Connection conn = DriverManager.getConnection(
		    		connString, userName, "");
		    PreparedStatement postSt = conn.prepareStatement("select * from dish where dish_id "
		    		+ "not in (select dish_id from menu where canteen_id = ?) ");
		){
			postSt.setString(1, canteen_id);
			ResultSet rs = postSt.executeQuery();
			json = ResultSetConverter(rs);
			obj.put("status",true);				
			obj.put("data",json );			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	
	public static JSONObject studentAccount(String student_id){
		JSONArray json = new JSONArray();
		JSONArray json1 = new JSONArray();
		JSONObject obj = new JSONObject();
		try (
		    Connection conn = DriverManager.getConnection(
		    		connString, userName, "");
			PreparedStatement postSt1 = conn.prepareStatement("select * from account where student_id = ? ;");
			PreparedStatement postSt = conn.prepareStatement("select * from account where student_id = ? ;");
		){
			postSt.setString(1, student_id);
			postSt1.setString(1, student_id);
			ResultSet rs = postSt1.executeQuery();
			ResultSet r = postSt.executeQuery();
			json = ResultSetConverter(r);
			while(rs.next()) {
				PreparedStatement pst = conn.prepareStatement(
						"select * from bills where account_id = ?;"
				);
				pst.setString(1, rs.getString("account_id"));
				ResultSet rs1 = pst.executeQuery();
				while(rs1.next()) {
					JSONObject obj1 = new JSONObject();
					obj1.put("bill_id", rs1.getString("bill_id"));
					obj1.put("amount", rs1.getString("amount"));
					obj1.put("payment_status", rs1.getString("payment_status"));
					obj1.put("order_id", rs1.getString("order_id"));
					obj1.put("details", rs1.getString("details"));
					json1.put(obj1);
				}
			}
			json.getJSONObject(0).put("bill_data",json1);
			obj.put("status",true);				
			obj.put("data",json );
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	public static JSONObject canteenAccount(String canteen_id){
		JSONArray json = new JSONArray();
		JSONArray json1 = new JSONArray();
		JSONObject obj = new JSONObject();
		try (
		    Connection conn = DriverManager.getConnection(
		    		connString, userName, "");
			PreparedStatement postSt = conn.prepareStatement("select * from canteen_account where canteen_id = ? ;");
		){
			postSt.setString(1, canteen_id);
			ResultSet rs = postSt.executeQuery();
			json = ResultSetConverter(rs);

			PreparedStatement pst = conn.prepareStatement(
					"select * from bills, \"order\" where canteen_id = ? and bills.order_id = \"order\".order_id;"
			);
			pst.setString(1, canteen_id);
			ResultSet rs1 = pst.executeQuery();
			while(rs1.next()) {
				JSONObject obj1 = new JSONObject();
				obj1.put("bill_id", rs1.getString("bill_id"));
				obj1.put("amount", rs1.getString("amount"));
				obj1.put("payment_status", rs1.getString("payment_status"));
				obj1.put("order_id", rs1.getString("order_id"));
				obj1.put("details", rs1.getString("details"));
				json1.put(obj1);
			}
			json.getJSONObject(0).put("bill_data",json1);
			obj.put("status",true);				
			obj.put("data",json );
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	
	public static JSONObject payBill(String bill_id){
		JSONObject obj = new JSONObject();
		String account_id ="", ctn_account_id="";
		Integer amount =0;
		try (
		    Connection conn = DriverManager.getConnection(
		    		connString, userName, "");
			PreparedStatement postSt = conn.prepareStatement("update bills set payment_status = ? where bill_id = ? ;");
			PreparedStatement pst = conn.prepareStatement(
					"update account set dues = dues - ? , balance = balance - ? where account_id = ? ");
			PreparedStatement pst1 = conn.prepareStatement("select bills.account_id as account_id, amount, canteen_account.account_id as ctn_account_id from bills, \"order\", canteen_account "
					+ "where bill_id=? and bills.order_id = \"order\".order_id and "
					+ "\"order\".canteen_id = canteen_account.canteen_id;");
			PreparedStatement pstC = conn.prepareStatement(
					"update canteen_account set dues = dues - ? , balance = balance + ? where account_id = ? ");
		){	
			pst1.setString(1,bill_id);
			ResultSet rs = pst1.executeQuery();
			while(rs.next()) {
				account_id =  rs.getString("account_id");
				amount =  rs.getInt("amount");
				ctn_account_id = rs.getString("ctn_account_id");
			}
			postSt.setString(1, "PAID");
			postSt.setString(2, bill_id);
			if(postSt.executeUpdate()>0) {
				pst.setInt(1, amount);
				pst.setInt(2, amount);
				pst.setString(3, account_id);
				if(pst.executeUpdate()>0) {
					pstC.setInt(1, amount);
					pstC.setInt(2, amount);
					pstC.setString(3, ctn_account_id);
					if(pstC.executeUpdate()>0)
						obj.put("status",true);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		return obj;
	}
	
	/*
	 * Takes student order and notifies canteen about the order.
	 */
	public static JSONObject takeorder(String student_id, String time_of_collecting, String ordered_items, String canteen_id)
	{
		JSONObject obj = new JSONObject();
		Connection conn = null;
		try{   
			conn = DriverManager.getConnection(connString, userName, passWord);
			
			String dish_id;
			Integer quantity;
			PreparedStatement pStmt;
			JSONObject jObj;
			JSONArray Array = new JSONArray(ordered_items);
			
			conn.setAutoCommit (false);
			
	        pStmt = conn.prepareStatement("insert into \"order\"(order_id,time_of_collecting,time_of_order,status_of_order,student_id,canteen_id) "
	        		+ "values(nextval('orderid'),?,?,?,?,?);");
			pStmt.setTimestamp(1,Timestamp.valueOf(time_of_collecting));
			Calendar calendar = Calendar.getInstance();
			java.util.Date now = calendar.getTime();
			Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
			pStmt.setTimestamp(2,currentTimestamp);
			pStmt.setString(3, "PENDING");
			pStmt.setString(4,student_id);
			pStmt.setString(5,canteen_id);
			pStmt.executeUpdate();
			
			for (int i = 0; i < Array.length(); i++) {
		        jObj = Array.getJSONObject(i);
		        dish_id = jObj.getString("dish_id");
		        quantity = jObj.getInt("quantity"); 
		        pStmt = conn.prepareStatement("insert into pending_dish_list(order_id,dish_id,item_id,quantity) values(currval('orderid'),?,nextval('itemid'),?);");
				pStmt.setString(1,dish_id);
				pStmt.setInt(2,quantity);
				pStmt.executeUpdate();
			}
			
			conn.commit();
			
			obj.put("status",true);
			
			// Notify canteen from student.
			String query1 = "insert into notification values (nextval('notifyid'), ?, ?, ?, ?, ?, ?)";
			PreparedStatement New_preparedStmt = conn.prepareStatement(query1);
			New_preparedStmt.setString(1, canteen_id);
			New_preparedStmt.setString(2, student_id);
			New_preparedStmt.setString(3, "New order is placed");
			New_preparedStmt.setString(4, "UNSEEN");
			calendar = Calendar.getInstance();
			now = calendar.getTime();
			currentTimestamp = new java.sql.Timestamp(now.getTime());
			New_preparedStmt.setTimestamp(5, currentTimestamp);
			New_preparedStmt.setString(6, "ALERT");
			if(New_preparedStmt.executeUpdate()>0) {
				System.out.println("Student notified Canteen");	
			}
			else {
				System.out.println("Failed to notify canteen");
			}	
			conn.commit();
		} catch (SQLException | JSONException sqle) {
				sqle.printStackTrace();
				try{
					 if(conn!=null) {
						 conn.rollback();
						 obj.put("status",false);
					 }
			            
			    }catch(SQLException | JSONException se2){
			         se2.printStackTrace();
			    }
			}
		return obj;
	}
	
	public static JSONObject pendingorders(String canteen_id){
		JSONArray json = new JSONArray();
		JSONObject obj = new JSONObject();
		JSONArray json1 = new JSONArray();
		try (
		    Connection conn = DriverManager.getConnection(
		    		connString, userName, "");
		    PreparedStatement postSt = conn.prepareStatement(
		    		"select * from \"order\" where canteen_id=? and status_of_order<>'REJECTED' order by time_of_order;");
		){
			postSt.setString(1, canteen_id);
			ResultSet rs = postSt.executeQuery();
			while(rs.next()) {
				PreparedStatement pst = conn.prepareStatement(
						"select * from pending_dish_list, dish where order_id=? and "
						+ "dish.dish_id=pending_dish_list.dish_id;"
				);
				pst.setString(1, rs.getString("order_id"));
				ResultSet rs1 = pst.executeQuery();
				json = ResultSetConverter(rs1);
				JSONObject obj1 = new JSONObject();
				obj1.put("order_id",rs.getString("order_id"));
				obj1.put("time_of_order",rs.getString("time_of_order"));
				obj1.put("time_of_collecting",rs.getString("time_of_collecting"));
				obj1.put("status_of_order",rs.getString("status_of_order"));
				obj1.put("student_id",rs.getString("student_id"));
				obj1.put("items",json );
				json1.put(obj1);
			}
			obj.put("status",true);				
			obj.put("data",json1);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	public static JSONObject studentOrder(String student_id){
		JSONArray json = new JSONArray();
		JSONArray json1 = new JSONArray();
		JSONObject obj = new JSONObject();
		try (
		    Connection conn = DriverManager.getConnection(
		    		connString, userName, "");
		    PreparedStatement postSt = conn.prepareStatement(
		    		"select * from \"order\" where student_id=? order by time_of_order;");
		){
					postSt.setString(1, student_id);
					ResultSet rs = postSt.executeQuery();
					while(rs.next()) {
						PreparedStatement pst = conn.prepareStatement(
								"select * from pending_dish_list, dish where order_id=? and "
								+ "dish.dish_id=pending_dish_list.dish_id;"
						);
						pst.setString(1, rs.getString("order_id"));
						ResultSet rs1 = pst.executeQuery();
						json = ResultSetConverter(rs1);
						JSONObject obj1 = new JSONObject();
						obj1.put("order_id",rs.getString("order_id"));
						obj1.put("time_of_order",rs.getString("time_of_order"));
						obj1.put("time_of_collecting",rs.getString("time_of_collecting"));
						obj1.put("status_of_order",rs.getString("status_of_order"));
						obj1.put("canteen_id",rs.getString("canteen_id"));
						obj1.put("items",json );
						json1.put(obj1);
					}
					obj.put("status",true);				
					obj.put("data",json1);
			} catch (SQLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	
	public static JSONObject changeorderstatus(String canteen_id,String order_id,String status){
		JSONObject jsonObj = new JSONObject();
			try{
				// Create the connection
				Connection conn = DriverManager.getConnection(connString, userName, passWord);
				
				// Get student_id for given order_id.
				String queryforstuid="select student_id from \"order\" where order_id=?";
				PreparedStatement New_preparedStmt1 = conn.prepareStatement(queryforstuid);
				New_preparedStmt1.setString(1, order_id);
				ResultSet n_rs= New_preparedStmt1.executeQuery();
				String student_id="";
				while(n_rs.next()) {
					 student_id=n_rs.getString("student_id");
				}
				
				if(status.equals("READY")) {
					String query1 = "select * from pending_dish_list, menu, \"order\", dish "
							+ "where menu.dish_id = pending_dish_list.dish_id "
							+ "and menu.canteen_id = \"order\".canteen_id "
							+ "and \"order\".order_id = ? "
							+ "and pending_dish_list.dish_id = dish.dish_id "
							+ "and pending_dish_list.order_id = \"order\".order_id;";
					PreparedStatement preparedStmt1 = conn.prepareStatement(query1);
					preparedStmt1.setString(1, order_id);
					ResultSet rst1 = preparedStmt1.executeQuery();
					Integer amount = 0;
					// Get the details of items in that order.
					String orderDtls = '\n' + "dish" + '\t' + "quantity" + '\t' + "price" + '\n';
					while(rst1.next()) {
						Integer price = rst1.getInt("price");
						Integer quan = rst1.getInt("quantity");
						String name = rst1.getString("name");
						String dish_id = rst1.getString("dish_id");
						System.out.println("dishid"+dish_id);
						amount += price*quan;
						orderDtls += name + '\t' + quan + '\t' + price + '\n';
						
						// Notify student to give review for this dish.
						String queryfori = "insert into notification values (nextval('notifyid'), ?, ?, ?, ?, ?, ?)";
						PreparedStatement New_preparedStmt = conn.prepareStatement(queryfori);
						System.out.println("para  " + student_id + canteen_id);
						New_preparedStmt.setString(1, student_id);
						New_preparedStmt.setString(2, canteen_id);
						New_preparedStmt.setString(3, name + "[" + dish_id + "]");
						New_preparedStmt.setString(4, "UNSEEN");
						Calendar calendar = Calendar.getInstance();
						java.util.Date now = calendar.getTime();
						Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
						New_preparedStmt.setTimestamp(5, currentTimestamp);
						New_preparedStmt.setString(6, "REVIEW");
						New_preparedStmt.executeUpdate();
					}
					System.out.println("amt" + amount);
					
					String query2 = "select account_id from account, \"order\" "
							+ "where account.student_id = \"order\".student_id "
							+ "and \"order\".order_id = ?";
					PreparedStatement preparedStmt2 = conn.prepareStatement(query2);
					preparedStmt2.setString(1, order_id);
					ResultSet rst2 = preparedStmt2.executeQuery();
					String acntid = "";
					while(rst2.next()) {
						acntid = rst2.getString("account_id");
					}
					System.out.println("acc" + acntid);
					
					// Create bill.
					String queryf = "insert into bills(bill_id, amount, order_id, payment_status, details, account_id) "
							+ "values (nextval('billid'), ?, ?, ?, ?, ?)";
					PreparedStatement preparedStmtf = conn.prepareStatement(queryf);
					preparedStmtf.setInt(1, amount);
					preparedStmtf.setString(2, order_id);
					preparedStmtf.setString(3, "UNPAID");
					preparedStmtf.setString(4, orderDtls);
					preparedStmtf.setString(5, acntid);
					if(preparedStmtf.executeUpdate()>0) {
						System.out.println("BILL generated");
						PreparedStatement pSt = conn.prepareStatement(
								"update account set dues = dues + ? where account_id =?;");
						pSt.setInt(1, amount);
						pSt.setString(2, acntid);
						if(pSt.executeUpdate()>0) {
							System.out.println("Student Account updated");
						}
						
						PreparedStatement pStC = conn.prepareStatement(
								"update canteen_account set dues = dues + ? where canteen_id =?;");
						pStC.setInt(1, amount);
						pStC.setString(2, canteen_id);
						if(pStC.executeUpdate()>0) {
							System.out.println("canteen Account updated");
						}
					}
				}
				
				// Update order status.
				String query = "update \"order\" set status_of_order=? where canteen_id=? and order_id=?";
				PreparedStatement preparedStmt = conn.prepareStatement(query);
				preparedStmt.setString(1, status);
				preparedStmt.setString(2, canteen_id);
				preparedStmt.setString(3, order_id);
				if(preparedStmt.executeUpdate()>0)
				{
					jsonObj.put("status", true);
					jsonObj.put("data","Status of order updated");				
				}
				else
				{
					jsonObj.put("status",false);
					jsonObj.put("message", "Could not update order status");
				}	
				
				// Add to notification to student.
				String query1 = "insert into notification values (nextval('notifyid'), ?, ?, ?, ?, ?, ?)";
				PreparedStatement New_preparedStmt = conn.prepareStatement(query1);
				New_preparedStmt.setString(1, student_id);
				New_preparedStmt.setString(2, canteen_id);
				New_preparedStmt.setString(3, "Your order is " + status);
				New_preparedStmt.setString(4, "UNSEEN");
				Calendar calendar = Calendar.getInstance();
				java.util.Date now = calendar.getTime();
				Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
				New_preparedStmt.setTimestamp(5, currentTimestamp);
				New_preparedStmt.setString(6, "ALERT");
				if(New_preparedStmt.executeUpdate()>0)
				{
					System.out.println("Canteen notified student");				
				}
				else
				{
					System.out.println("Failed to notify student");
				}	
				 
			} catch(Exception e){
				e.printStackTrace();
			}
			
			return jsonObj;
		}
	
	public static JSONObject getNotifications(String id){
		JSONArray json = new JSONArray();
		JSONObject obj = new JSONObject();
		try (
		    Connection conn = DriverManager.getConnection(
		    		connString, userName, "");
		    PreparedStatement postSt = conn.prepareStatement(
		    		"select * from notification where to_id=?;");
		){
			postSt.setString(1, id);
			ResultSet rs = postSt.executeQuery();
			json = ResultSetConverter(rs);
			obj.put("status",true);				
			obj.put("data",json);  
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	public static JSONObject updateSeenStatus(String nid){
		JSONObject obj = new JSONObject();
		try (
		    Connection conn = DriverManager.getConnection(
		    		connString, userName, "");
		    PreparedStatement postSt = conn.prepareStatement(
		    		"update notification set seen_status = 'SEEN' where notification_id=?;");
		){
			postSt.setString(1, nid);
			if(postSt.executeUpdate()>0) {
				obj.put("status",true);	
				obj.put("message", "Seen Notification");
			} else {
				obj.put("status",false);	
				obj.put("message", "Can't See Notification");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	/*
	 * Adds review for a dish in a canteen by a student.
	 */
	public static JSONObject addReview(String canteen_id, String student_id, String dish_id, Integer rating, String review_text, String time){
		JSONObject obj = new JSONObject();
		try (
		    Connection conn = DriverManager.getConnection(
		    		connString, userName, "");
		    PreparedStatement postSt = conn.prepareStatement(
		    		"insert into reviews values(nextval('reviewid'), ?, ?, ?, ?, ?, ?)");
		){
			postSt.setString(1, canteen_id);
			postSt.setString(2, student_id);
			postSt.setString(3, dish_id);
			postSt.setInt(4, rating);
			postSt.setString(5, review_text);
			postSt.setTimestamp(6, Timestamp.valueOf(time));
			if(postSt.executeUpdate()>0) {
				obj.put("status",true);	
				obj.put("message", "Seen Notification");
			} else {
				obj.put("status",false);	
				obj.put("message", "Can't See Notification");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}

private static JSONArray ResultSetConverter(ResultSet rs) throws SQLException, JSONException {
		
		// TODO Auto-generated method stub
		JSONArray json = new JSONArray();
	    ResultSetMetaData rsmd = rs.getMetaData();
	    while(rs.next()) {
	        int numColumns = rsmd.getColumnCount();
	        JSONObject obj = new JSONObject();
	        
	        for (int i=1; i<numColumns+1; i++) {
	          String column_name = rsmd.getColumnName(i);

	          if(rsmd.getColumnType(i)==java.sql.Types.ARRAY){
	           obj.put(column_name, rs.getArray(column_name));
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.BIGINT){
	           obj.put(column_name, rs.getInt(column_name));
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.BOOLEAN){
	           obj.put(column_name, rs.getBoolean(column_name));
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.BLOB){
	           obj.put(column_name, rs.getBlob(column_name));
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.DOUBLE){
	           obj.put(column_name, rs.getDouble(column_name)); 
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.FLOAT){
	           obj.put(column_name, rs.getFloat(column_name));
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.INTEGER){
	           obj.put(column_name, rs.getInt(column_name));
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.NVARCHAR){
	           obj.put(column_name, rs.getNString(column_name));
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.VARCHAR){
	           obj.put(column_name, rs.getString(column_name));
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.TINYINT){
	           obj.put(column_name, rs.getInt(column_name));
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.SMALLINT){
	           obj.put(column_name, rs.getInt(column_name));
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.DATE){
	           obj.put(column_name, rs.getDate(column_name));
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.TIMESTAMP){
	          obj.put(column_name, rs.getTimestamp(column_name));   
	          }
	          else{
	           obj.put(column_name, rs.getObject(column_name));
	          }
	          
	        }
	        json.put(obj);
	        
	      }
	    return json;
	}
	
}