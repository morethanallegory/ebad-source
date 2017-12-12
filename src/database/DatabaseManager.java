package database;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.jdbc.ScriptRunner;

import util.EmailErrorReporter;
import util.StringUtils;
import application.ConfigurationException;
import bus.CommsBus;
import ebay.ItemView;

public class DatabaseManager {
	private Connector connector = new Connector();

	private Connection connection = null;

	//clears table and disconnects ***see also disconnect()
	public void shutdown() {
		try {
			this.clearTables();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			CommsBus.messageSent(e.getLocalizedMessage());
		}
		this.disconnect();
	}

	public void disconnect() {
		if (this.connection != null) {
			try {
				// with shutdown true in the db connection url this will
				// shutdown the db
				this.connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				CommsBus.messageSent(e.getLocalizedMessage());
			}
		}
		this.connection = null;
	}

	private void connect() {
		if (this.connection == null) {

			try {				
				connection = connector.getConnection();
				connection.setAutoCommit(false);
			} catch (SQLException | ConfigurationException e1) {
				e1.printStackTrace();
				CommsBus.messageSent(e1.getLocalizedMessage());
				if (connection != null) {
					try {
						connection.close();
					} catch (SQLException e2) {
					}
				}
				EmailErrorReporter.addError(e1, "Something went wrong connecting to the database", this.getClass().getName(), "connect");
				EmailErrorReporter.sendErrorEmail();
			}
		}
	}

	public void persistItem(ItemView item) throws SQLException {
		this.connect();
		String sql = "insert into ebayitems (id, wordcount, title, price, time_left, shipping,url,bids, image_url, category, update_time) values (?, ?, ?, ?, ?, ?, ?,?,? ,?, NOW())";
		// create a statement
		PreparedStatement statement = null;
		try {
			statement = this.connection.prepareStatement(sql);
			statement.setString(1, item.getValue("id"));
			statement.setInt(2, StringUtils.wordCount(item.getValue("title")));
			statement.setString(3, item.getValue("title"));
			statement.setString(4, item.getValue("price"));
			statement.setString(5, item.getValue("time"));
			statement.setString(6, item.getValue("shipping"));
			statement.setString(7, item.getValue("url"));
			statement.setString(8, item.getValue("bids"));
			statement.setString(9, item.getValue("image_url"));
			statement.setString(10, item.getValue("category"));

			// execute a query
			statement.executeUpdate();
			this.connection.commit();
			// System.out.println(sql);
			// statement.close();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
		}
	}

	// save an item in archive
	public void persistItemViewed(ItemView item) throws SQLException {
		this.connect();
		String sql = "insert into ebayitems_viewed (id, title, update_time) values (?,? , NOW())";
		// create a statement
		PreparedStatement statement = null;
		try {
			statement = this.connection.prepareStatement(sql);
			statement.setString(1, item.getValue("id"));
			statement.setString(2, item.getValue("title"));
			// execute a query
			statement.executeUpdate();
			this.connection.commit();
			// System.out.println(sql);
			// statement.close();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
		}
	}

	public boolean existsInItemsViewed(ItemView item) throws SQLException {
		this.connect();
		String sql = "select id from ebayitems_viewed where id =?";
		// create a statement
		PreparedStatement statement = null;
		boolean exists = false;
		try {
			statement = this.connection.prepareStatement(sql);
			statement.setString(1, item.getValue("id"));
			// execute a query
			ResultSet res = statement.executeQuery();
			this.connection.commit();
			// System.out.println(sql);
			// statement.close();

			exists = res.next();

		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
		}

		return exists;
	}
	
	public void persist(ItemView itemView) throws SQLException{
		if(! existsInItemsViewed(itemView)) {
			persistItem(itemView);
			persistItemViewed(itemView);
			incrementItemCount();
		}
	}

	public boolean existsInItems(ItemView item) throws SQLException {
		this.connect();
		String sql = "select id from ebayitems where id =?";
		// create a statement
		PreparedStatement statement = null;
		boolean exists = false;
		try {
			statement = this.connection.prepareStatement(sql);
			statement.setString(1, item.getValue("id"));
			// execute a query
			ResultSet res = statement.executeQuery();
			this.connection.commit();
			// System.out.println(sql);
			// statement.close();

			exists = res.next();

		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
		}

		return exists;
	}

	public List<ItemView> fetchAll(int wordcount) throws SQLException {
		this.connect();
		List<ItemView> items = new ArrayList<ItemView>();
		String sql = "select * from ebayitems where wordcount <= " + wordcount
				+ " order by category, wordcount asc";
		// create a statement
		PreparedStatement statement = null;
		ResultSet rs = null;
		ItemView item;
		try {
			statement = this.connection.prepareStatement(sql);
			// execute a query
			rs = statement.executeQuery();
			while (rs.next()) {
				item = new ItemView();
				item.addItem("wordcount", rs.getString("wordcount"));
				item.addItem("title", rs.getString("title"));
				item.addItem("price", rs.getString("price"));
				item.addItem("bids", rs.getString("bids"));
				item.addItem("shipping", rs.getString("shipping"));
				item.addItem("time", rs.getString("time_left"));
				item.addItem("url", rs.getString("url"));
				item.addItem("image_url", rs.getString("image_url"));
				item.addItem("id", rs.getString("id"));
				item.addItem("category", rs.getString("category"));
				items.add(item);
			}

		} finally {
			if (statement != null) {
				try {
					rs.close();
					statement.close();
				} catch (SQLException e) {

				}
			}
		}
		return items;
	}

	private void clearTables() throws SQLException {
		this.connect();
		String delItemsViewed = "delete from ebayitems_viewed";
		String delItems = "delete from ebayitems";
		String delCounts = "delete from counters";
		// create a statement
		PreparedStatement statement = null;

		try {
			statement = this.connection.prepareStatement(delItemsViewed);

			// execute a query
			statement.executeUpdate();
			this.connection.commit();
			statement.close();

			statement = this.connection.prepareStatement(delItems);

			// execute a query
			statement.executeUpdate();
			this.connection.commit();
			
			statement.close();

			statement = this.connection.prepareStatement(delCounts);

			// execute a query
			statement.executeUpdate();
			this.connection.commit();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
		}
	}
	
	public void incrementPageCount() throws SQLException {
		this.connect();
		String statmnt = "select page_count from counters";
		// create a statement
		PreparedStatement statement = null;

		try {
			statement = this.connection.prepareStatement(statmnt);

			// execute a query
			ResultSet res = statement.executeQuery();
			statement.close();
			if(res.next()){
				statmnt = "update counters set page_count = (page_count +1)";
			} 
			else{
				statmnt = "insert into counters (page_count)values(1)";
				
			}
			statement = this.connection.prepareStatement(statmnt);
			statement.executeUpdate();
			this.connection.commit();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
		}
	}
	
	public void incrementItemCount() throws SQLException {
		this.connect();
		String statmnt = "select item_count from counters";
		// create a statement
		PreparedStatement statement = null;

		try {
			statement = this.connection.prepareStatement(statmnt);

			// execute a query
			ResultSet res = statement.executeQuery();
			statement.close();
			if(res.next()){
				statmnt = "update counters set item_count = (item_count +1)";
			} 
			else{
				statmnt = "insert into counters (item_count)values(1)";
				
			}
			statement = this.connection.prepareStatement(statmnt);
			statement.executeUpdate();
			this.connection.commit();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
		}
	}
	
	
	public void incrementCategoryCount() throws SQLException {
		this.connect();
		String statmnt = "select category_count from counters";
		// create a statement
		PreparedStatement statement = null;

		try {
			statement = this.connection.prepareStatement(statmnt);

			// execute a query
			ResultSet res = statement.executeQuery();
			statement.close();
			if(res.next()){
				statmnt = "update counters set category_count = (category_count + 1)";
			} 
			else{
				statmnt = "insert into counters (category_count)values(1)";
				
			}
			statement = this.connection.prepareStatement(statmnt);
			statement.executeUpdate();
			this.connection.commit();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
		}
	}
	
	public void increaseItemCount(int count) throws SQLException {
		this.connect();
		String statmnt = "select item_count from counters";
		// create a statement
		PreparedStatement statement = null;

		try {
			statement = this.connection.prepareStatement(statmnt);

			// execute a query
			ResultSet res = statement.executeQuery();
			statement.close();
			if(res.next()){
				statmnt = "update counters set item_count = (item_count + ?)";
				
			} 
			else{
				statmnt = "insert into counters (item_count)values(?)";
				
			}
			statement = this.connection.prepareStatement(statmnt);
			statement.setInt(1, count);
			statement.executeUpdate();
			this.connection.commit();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
		}
	}
	
	
	public void increaseCategoryCount(int count) throws SQLException {
		this.connect();
		String statmnt = "select category_count from counters";
		// create a statement
		PreparedStatement statement = null;

		try {
			statement = this.connection.prepareStatement(statmnt);

			// execute a query
			ResultSet res = statement.executeQuery();
			statement.close();
			if(res.next()){
				statmnt = "update counters set category_count = (category_count + ?)";
				
			} 
			else{
				statmnt = "insert into counters (category_count)values(?)";
				
			}
			statement = this.connection.prepareStatement(statmnt);
			statement.setInt(1, count);
			statement.executeUpdate();
			this.connection.commit();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
		}
	}
	
	public int getPageCount() throws SQLException {
		this.connect();
		String sql = "select page_count from counters";
		// create a statement
		PreparedStatement statement = null;
		try {
			statement = this.connection.prepareStatement(sql);
			ResultSet res = statement.executeQuery();
            return res.next() ? res.getInt(1): 0;

		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
		}
	}
	
	public int getCategoryCount() throws SQLException {
		this.connect();
		String sql = "select category_count from counters";
		// create a statement
		PreparedStatement statement = null;
		try {
			statement = this.connection.prepareStatement(sql);
			ResultSet res = statement.executeQuery();
            return res.next() ? res.getInt(1): 0;

		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
		}
	}

	public int getItemCount() throws SQLException {
		this.connect();
		String sql = "select item_count from counters";
		// create a statement
		PreparedStatement statement = null;
		try {
			statement = this.connection.prepareStatement(sql);
			ResultSet res = statement.executeQuery();
            return res.next() ? res.getInt(1): 0;

		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
		}
	}


	public static void createSchema() throws SQLException,
			FileNotFoundException, ConfigurationException {
		Connector connector = new Connector();
		Connection connection = connector.getConnection();

		ScriptRunner runner = new ScriptRunner(connection);
		// runner.setLogWriter(new PrintWriter(System.out));

		InputStreamReader reader = new InputStreamReader(DatabaseManager.class
				.getClassLoader().getResourceAsStream("create.sql"));
		runner.runScript(reader);

		connection.commit();
		connection.close();
	}

}
