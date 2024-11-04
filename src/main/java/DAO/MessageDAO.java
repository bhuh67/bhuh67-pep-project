package DAO;

import Model.Message;
import Util.ConnectionUtil;
import java.sql.*;
import java.util.ArrayList;

public class MessageDAO {

    public Message addMessage(Message message) {

        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql , Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if(resultSet.next()){
                int generated_post_id = (int) resultSet.getLong(1);
                return new Message(generated_post_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        //System.out.println("failed at DAO level");
        return null;
    }

    public ArrayList<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        ArrayList<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
    
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Message message = new Message(resultSet.getInt("message_id"), 
                                              resultSet.getInt("posted_by"), 
                                              resultSet.getString("message_text"), 
                                              resultSet.getLong("time_posted_epoch"));
                messages.add(message);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public Message getMessageById(int id) {

        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM Message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return new Message(rs.getInt("message_id"), 
                                              rs.getInt("posted_by"), 
                                              rs.getString("message_text"), 
                                              rs.getLong("time_posted_epoch"));
            }

        } catch(Exception e){
            System.out.print(e.getMessage());
        }
        return null;
    }

    public Message deleteMessageById(int id) {
        Connection connection = ConnectionUtil.getConnection();
        Message message = getMessageById(id);
        try{
            String sql = "DELETE FROM Message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, id);

            ps.executeUpdate();

            return message;

        } catch(Exception e){
            System.out.print(e.getMessage());
        }
        return null;
    }

    public Message editMessageById(int id, String newMessage) {
        Connection connection = ConnectionUtil.getConnection();

        try{
            String sql = "UPDATE Message SET message_text = ? WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            
            ps.setString(1, newMessage);
            ps.setInt(2, id);

            ps.executeUpdate();

            Message message = getMessageById(id);

            return message;

        } catch(Exception e){
            System.out.print(e.getMessage());
        }
        System.out.println("Update failed on DAO layer");
        return null;
    }

    public ArrayList<Message> getAllMessagesByAccount(int id) {
        Connection connection = ConnectionUtil.getConnection();
        ArrayList<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Message WHERE posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
    
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Message message = new Message(resultSet.getInt("message_id"), 
                                              resultSet.getInt("posted_by"), 
                                              resultSet.getString("message_text"), 
                                              resultSet.getLong("time_posted_epoch"));
                messages.add(message);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }
    
}
