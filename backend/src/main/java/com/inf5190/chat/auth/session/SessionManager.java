package com.inf5190.chat.auth.session;

import java.util.*;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.crypto.SecretKey;

/**
 * Classe qui gère les sessions utilisateur.
 * 
 * Pour le moment, on gère en mémoire.
 */
@Repository
public class SessionManager {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private static final String SECRET_KEY_BASE64 = "8bg27XEzTiSMe/2PDCY1DtqNxYjfh0Vpa+yr+gSDEjU=";
    //8bg27XEzTiSMe/2PDCY1DtqNxYjfh0Vpa+yr+gSDEjU=
    private final SecretKey secretKey;
    private final JwtParser jwtParser;
    private final Map<String, SessionData> sessions = new HashMap<String, SessionData>();

    public SessionManager() {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY_BASE64));
        this.jwtParser = Jwts.parserBuilder().setSigningKey(this.secretKey).build();
    }
    public String addSession(SessionData authData) {
        JwtBuilder builder = Jwts.builder();
        builder.setAudience("inf5190");
        Calendar dateActuel = Calendar.getInstance();
        dateActuel.setTime(new Date());
        builder.setIssuedAt(dateActuel.getTime());
        builder.setSubject(authData.username());
        dateActuel.add(Calendar.HOUR_OF_DAY,2);
        builder.setExpiration(dateActuel.getTime());
        builder.signWith(secretKey);
        return builder.compact();
    }

    public void removeSession(String token) {
        this.sessions.remove(token);
    }

    public SessionData getSession(String token) {
        try {
            return new SessionData(this.jwtParser.parseClaimsJws(token).getBody().getSubject());
        } catch (JwtException e) {
            this.logger.info("Invalid token", e);
            return null;
        }

    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

}
