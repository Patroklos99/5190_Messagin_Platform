package com.inf5190.chat.messages;

import com.inf5190.chat.auth.session.SessionDataAccessor;
import com.inf5190.chat.messages.model.Message;
import com.inf5190.chat.messages.model.MessageRequest;
import com.inf5190.chat.messages.repository.MessageRepository;
import com.inf5190.chat.websocket.WebSocketManager;

import javax.servlet.ServletContext;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Contrôleur qui gère l'API de messages.
 * 
 * Implémente ServletContextAware pour recevoir le contexte de la requête HTTP.
 */
@RestController
public class MessageController implements ServletContextAware {
    public static final String ROOT_PATH = "/messages";

    private MessageRepository messageRepository;
    private WebSocketManager webSocketManager;
    private ServletContext servletContext;
    private SessionDataAccessor sessionDataAccessor;

    public MessageController(MessageRepository messageRepository,
            WebSocketManager webSocketManager,
            SessionDataAccessor sessionDataAccessor) {
        this.messageRepository = messageRepository;
        this.webSocketManager = webSocketManager;
        this.sessionDataAccessor = sessionDataAccessor;
    }

    // À faire...
    @GetMapping("/messages")
    public Message[] getMessages(@RequestParam Optional<String> fromId) throws ExecutionException, InterruptedException {
        List<Message> list = new ArrayList<>();
        try {
            list = messageRepository.getMessages(fromId);
        }catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unexpected error on get message.");
        }
        return list.toArray(new Message[0]);
    }

    @PostMapping("/messages")
    public Message postMessage(@RequestBody MessageRequest message) throws ExecutionException, InterruptedException {
        Message mess;
        try {
            mess = messageRepository.createMessage(message);
        }catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unexpected error on post message.");
        }
        this.webSocketManager.notifySessions();
        return mess;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
