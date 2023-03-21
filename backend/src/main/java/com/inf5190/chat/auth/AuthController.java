package com.inf5190.chat.auth;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.inf5190.chat.auth.repository.FirestoreUserAccount;
import com.inf5190.chat.auth.repository.UserAccountRepository;
import com.inf5190.chat.auth.session.SessionData;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.ServletContextAware;

import com.inf5190.chat.auth.model.LoginRequest;
import com.inf5190.chat.auth.model.LoginResponse;
import com.inf5190.chat.auth.session.SessionDataAccessor;
import com.inf5190.chat.auth.session.SessionManager;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * Contrôleur qui gère l'API de login et logout.
 * 
 * Implémente ServletContextAware pour recevoir le contexte de la requête HTTP.
 */
@RestController()
public class AuthController  {

    private final SessionManager sessionManager;
    private final SessionDataAccessor sessionDataAccessor;
    private final PasswordEncoder passwordEncoder;

    private final UserAccountRepository userAccountRepository;

    public AuthController(SessionManager sessionManager, SessionDataAccessor sessionDataAccessor, PasswordEncoder passwordEncoder,UserAccountRepository userAccountRepository) {
        this.sessionManager = sessionManager;
        this.sessionDataAccessor = sessionDataAccessor;
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("auth/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) throws InterruptedException, ExecutionException {
        try {
            FirestoreUserAccount client = this.userAccountRepository.getUserAccount(loginRequest.username());
            if (client != null) {
                if (this.passwordEncoder.matches(loginRequest.password(), client.getEncodedPassword())) {
                    SessionData sessionData = new SessionData(client.getUsername());
                    String token = sessionManager.addSession(sessionData);
                    return new LoginResponse(token);
                }
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
            this.userAccountRepository.setUserAccount(new FirestoreUserAccount(loginRequest.username(), this.passwordEncoder.encode(loginRequest.password())));
            SessionData sessionData = new SessionData(loginRequest.username());
            String token = sessionManager.addSession(sessionData);
            return new LoginResponse(token);
        }catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unexpected error on login.");
        }

    }

    @PostMapping("auth/logout")
    public void logout(HttpServletRequest servletContext) {
        try {
            String token = sessionDataAccessor.getToken(servletContext);
            sessionManager.removeSession(token);
        }catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unexpected error on logout.");
        }
    }

/*    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }*/
}
