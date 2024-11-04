package Service;

import Model.Message;

import java.util.ArrayList;

import DAO.MessageDAO;

public class MessageService {

    private MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }

    public Message addMessage(Message message) {
        if(!message.getMessage_text().isEmpty() && message.getMessage_text().length() < 255){
            return messageDAO.addMessage(message);
        }
        //System.out.println("Failed at service level");
        return null;
    }

    public ArrayList<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int id) {
        return messageDAO.getMessageById(id);
    }

    public Message deleteMessageById(int id) {
        return messageDAO.deleteMessageById(id);
    }

    public Message editMessageById(int id, String newMessage) {
        if(!newMessage.isEmpty() && newMessage.length() < 255){
            return messageDAO.editMessageById(id, newMessage);
        }
        System.out.println("Failed to update at service level");
        return null;
    }

    public ArrayList<Message> getAllMessagesByAccount(int id) {
        return messageDAO.getAllMessagesByAccount(id);
    }
    

}
