package de.xyonox.homy.services;

import de.xyonox.homy.model.User;
import de.xyonox.homy.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {

    private final UserRepository userRepo;

    public AuthService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public void register(String username, String email, String password) throws Exception {

        String hash = BCrypt.hashpw(password, BCrypt.gensalt());

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(hash);
        user.setCreatedAt(System.currentTimeMillis());

        userRepo.create(user);
    }

    public User login(String username, String password) throws Exception {

        User user = userRepo.findByUsername(username);

        if (user == null)
            throw new RuntimeException("User not found");

        if (!BCrypt.checkpw(password, user.getPasswordHash()))
            throw new RuntimeException("Wrong password");

        return user;
    }
}
