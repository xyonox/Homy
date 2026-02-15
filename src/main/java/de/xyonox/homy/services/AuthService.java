package de.xyonox.homy.services;

import de.xyonox.homy.model.Token;
import de.xyonox.homy.model.User;
import de.xyonox.homy.repository.TokenRepository;
import de.xyonox.homy.repository.UserRepository;
import de.xyonox.homy.security.TokenUtils;
import org.mindrot.jbcrypt.BCrypt;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

public class AuthService {

    private static final int TOKEN_VALID_DAYS = 30;

    private final UserRepository userRepo;
    private final TokenRepository tokenRepo;
    private final SecureRandom secureRandom = new SecureRandom();

    public AuthService(UserRepository userRepo, TokenRepository tokenRepo) {
        this.userRepo = userRepo;
        this.tokenRepo = tokenRepo;
    }

    /* ========================= REGISTER ========================= */

    public Optional<User> register(String username, String email, String password) throws Exception {

        if (username == null || email == null || password == null)
            return Optional.empty();

        if (userRepo.findByUsername(username) != null)
            return Optional.empty();

        if (userRepo.findByEmail(email) != null)
            return Optional.empty();

        String hash = BCrypt.hashpw(password, BCrypt.gensalt());

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(hash);
        user.setCreatedAt(System.currentTimeMillis());

        userRepo.create(user);

        return Optional.of(user);
    }

    /* ========================= LOGIN ========================= */

    public Optional<String> login(String username, String password) throws Exception {

        if (username == null || password == null)
            return Optional.empty();

        User user = userRepo.findByUsername(username);
        if (user == null)
            return Optional.empty();

        if (!BCrypt.checkpw(password, user.getPasswordHash()))
            return Optional.empty();

        tokenRepo.deleteByUser(user);

        String rawToken = generateSecureToken();
        String hashedToken = TokenUtils.hashToken(rawToken);

        Token token = new Token(hashedToken, user);
        tokenRepo.create(token);

        return Optional.of(rawToken);
    }

    /* ========================= TOKEN AUTH ========================= */

    public Optional<User> authenticateToken(String rawToken) throws SQLException {

        if (rawToken == null)
            return Optional.empty();

        String hashedToken = TokenUtils.hashToken(rawToken);

        Token token = tokenRepo.findByHashToken(hashedToken);
        if (token == null)
            return Optional.empty();

        LocalDateTime expiresAt = token.getCreatedAt().plusDays(TOKEN_VALID_DAYS);
        if (LocalDateTime.now().isAfter(expiresAt)) {
            tokenRepo.delete(token);
            return Optional.empty();
        }

        return Optional.ofNullable(token.getUser());
    }

    /* ========================= LOGOUT ========================= */

    public boolean logout(String rawToken) throws SQLException {

        if (rawToken == null)
            return false;

        String hashedToken = TokenUtils.hashToken(rawToken);
        Token token = tokenRepo.findByHashToken(hashedToken);

        if (token == null)
            return false;

        tokenRepo.delete(token);
        return true;
    }

    /* ========================= TOKEN GENERATION ========================= */

    private String generateSecureToken() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}