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
    

}
