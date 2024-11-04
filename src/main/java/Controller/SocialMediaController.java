package Controller;



import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Message;
import Model.Account;
import Service.MessageService;
import Service.AccountService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    /**
     * Constructor
     */
    public SocialMediaController(){
        accountService = new AccountService();
        messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::postAccountHandler);
        app.post("/login", this::postLoginAccountHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIDHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIDHandler);
        app.patch("/messages/{message_id}", this::patchMessageByIDHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByAccount);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    /**
     * Registration of a new user account through POST.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void postAccountHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account createdAccount = accountService.addAccount(account);

        if(createdAccount != null){
            context.json(mapper.writeValueAsString(createdAccount));
            context.status(200);
        } else{
            context.status(400);
        }
    }

    /*
     * Processing of an account login
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void postLoginAccountHandler(Context context) throws JsonProcessingException{
        //context body = login credentials
        ObjectMapper mapper = new ObjectMapper();
        Account account = new Account();
        Account loggedAccount = null;

        try
        {
            account = mapper.readValue(context.body(), Account.class);
            loggedAccount = accountService.loginAccount(account);
        }catch(Exception e) {
            System.out.println(e.getMessage());
            context.status(401);
        }
        

        if(loggedAccount != null){
            context.json(mapper.writeValueAsString(loggedAccount));
            context.status(200);
        } else{
            context.status(401);
        }

    }

    /*
     * Handler to post a message
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void postMessageHandler(Context context) throws JsonProcessingException {
        //request body: posted_by, message_text, time_posted_epoch, linked user id. \
        //response body: same as request except contains message_id, status 200
        //successful if and only if message_text length < 255, not blank, and if posted_by refers to a real user
        //if not successful, status 400 (client error)

        ObjectMapper mapper = new ObjectMapper();
        Message message = new Message();

        try
        {
            message = mapper.readValue(context.body(), Message.class);
        }catch(Exception e) {
            System.out.println(e.getMessage());
            context.status(400);
        }

        if(accountService.identifyAccount(message.getPosted_by()) == null){
            context.status(400);
            System.out.println("Failed to find poster ID");
            return;
        }

        Message postedMessage = messageService.addMessage(message);
        if(postedMessage == null){
            context.status(400);
        }
        else{
            context.json(mapper.writeValueAsString(postedMessage));
            context.status(200);
        }

    }

    /*
     * Handler to get all messages
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getAllMessagesHandler(Context context) {

        ArrayList<Message> messages = new ArrayList<Message>();
        try {
            messages = messageService.getAllMessages();
            context.json(messages);
        } catch (Exception e) {
            System.out.println("Debug exception");
        }
    }

    /*
     * Handler to get message by ID
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getMessageByIDHandler(Context context) {
        Message message;
        int id;
        ObjectMapper mapper = new ObjectMapper();
        try {
            id = Integer.parseInt(context.pathParam("message_id")) ;
            message = messageService.getMessageById(id);
            if(message!=null)
                context.json(mapper.writeValueAsString(message));
            else    
                context.json("");
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }

    }

    /*
     * Handler for deleting a message by ID
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void deleteMessageByIDHandler(Context context) {
        Message message;
        int id;
        ObjectMapper mapper = new ObjectMapper();
        try {
            id = Integer.parseInt(context.pathParam("message_id")) ;
            message = messageService.deleteMessageById(id);
            if(message!=null)
                context.json(mapper.writeValueAsString(message));
            else    
                context.json("");
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }

    /*
     * Handler for updating a message found by ID
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void patchMessageByIDHandler(Context context) {
        Message message;
        int id;
        ObjectMapper mapper = new ObjectMapper();
        Message newMessage; //return new message
        try {
            id = Integer.parseInt(context.pathParam("message_id")) ;
            newMessage = mapper.readValue(context.body(), Message.class);
            message = messageService.editMessageById(id, newMessage.getMessage_text());
            if(message!=null)
                context.json(mapper.writeValueAsString(message));
            else    
                context.status(400);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }

    /*
     * Handler for getting all messages for a given user
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getAllMessagesByAccount(Context context) {
        ArrayList<Message> messages = new ArrayList<Message>();
        int id;
        try {
            id = Integer.parseInt(context.pathParam("account_id"));
            messages = messageService.getAllMessagesByAccount(id);
            context.json(messages);
        } catch (Exception e) {
            System.out.println("Debug exception");
        }
    }

}