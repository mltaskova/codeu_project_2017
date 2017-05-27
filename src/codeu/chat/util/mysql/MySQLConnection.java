package codeu.chat.util.mysql;

import java.sql.*;
import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Vector;
import java.util.Date;
import java.text.SimpleDateFormat;


public class MySQLConnection{
    private static final String DATABASE_DRIVER = "com.mysql.jdbc.Driver";
    // Is my hosting information correct here?
    // Upon further testing I believe it is. Will likely need to be changed depending
    // 	on the computer that is running it.
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/CodeUChat?autoReconnect=true&&useSSL=false";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "king";

    // Create connection object
    private Connection connection;
    // Creat properties object
    private Properties properties;

    // Create the actual properties
    private Properties getProperties(){
        if (properties == null){
            properties = new Properties();
            properties.setProperty("user", USERNAME);
            properties.setProperty("password", PASSWORD);
        }
        return properties;
    }

    // Connect to the database
    private Connection connect(){
        if(connection == null){
            try{
                Class.forName(DATABASE_DRIVER);
                connection = DriverManager.getConnection(DATABASE_URL, getProperties());
            } catch (ClassNotFoundException | SQLException e){
            	System.out.println("There was an error connecting to the database.");
                e.printStackTrace();
            }
        }
        return connection;
    }

    public Connection getConnection(){
        return connect();
    }

    // Closing the database connection
    private void disconnect(){
        if(connection != null){
            try{
                connection.close();
                connection = null;
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    public void disconnectConnection(){
        disconnect();
    }



    //WILL NEED TO MAKE SURE STRING AND VARCHAR ARE COMPATIBLE!!

    public void writeConversations(String title, String owner, String password) throws SQLException {
        Connection connect = getConnection();

        // PreparedStatements can use variables and are more efficient
        PreparedStatement preparedStatement = connect
                .prepareStatement("insert into  CodeUChat.Conversations values (default, ?, ?, ?)");
        // Parameters start with 1
        preparedStatement.setString(1, title);
        preparedStatement.setString(2, owner);
        preparedStatement.setString(3, password);
        preparedStatement.executeUpdate();
    }

    public void writeMessages(String owner, String body, String conversation) throws SQLException
    {
        Connection connect = getConnection();

        // PreparedStatements can use variables and are more efficient
        PreparedStatement preparedStatement = connect
                .prepareStatement("insert into  CodeUChat.Messages values (default, ?, ?, ?, ?)");

        java.util.Date date = new java.util.Date();
        java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());

        // Parameters start with 1
        preparedStatement.setString(1, owner);
        preparedStatement.setString(2, body);
        preparedStatement.setString(3, conversation);
        preparedStatement.setTimestamp(4, timestamp);
        preparedStatement.executeUpdate();
    }

    public String[] readConversations() throws SQLException {

        Connection connect = getConnection();

        // Statements allow to issue SQL queries to the database
        Statement statement = connect.createStatement();
        // Result set get the result of the SQL query
        ResultSet ownerResultSet = statement
                .executeQuery("select distinct Owner from CodeUChat.Conversations");

        String[] arr = null;
        while (ownerResultSet.next()) {
            String em = ownerResultSet.getString("Owner");
            arr = em.split("\n");
            for (int i = 0; i < arr.length; i++) {
                System.out.println(arr[i]);
            }
        }

        return arr;

//        ResultSet titleResultSet = statement
//                .executeQuery("select distinct Title from CodeUChat.Conversations");
        //writeResultSet(resultSet);
    }

    //read all the messages from owner
    public String[] readMessages(String owner) throws SQLException
    {
        Connection connect = getConnection();

        // Statements allow to issue SQL queries to the database
        PreparedStatement statement = connect.prepareStatement("select Body from CodeUChat.Messages where Owner = ? ");
        // Result set get the result of the SQL query

        statement.setString(1, owner);

        ResultSet ownerResultSet = statement
                .executeQuery();
        //writeResultSet(resultSet);

        String[] arr = null;
        while (ownerResultSet.next()) {
            String em = ownerResultSet.getString("Body");
            arr = em.split("\n");
            for (int i = 0; i < arr.length; i++) {
                System.out.println(arr[i]);
            }
        }

        return arr;

    }

//    public void writeResultSet()
//    {
//        //writing owners into the current session so they can appear in the gui
//    }



}