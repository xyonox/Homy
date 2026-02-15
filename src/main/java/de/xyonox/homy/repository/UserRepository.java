package de.xyonox.homy.repository;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import de.xyonox.homy.config.DatabaseManager;
import de.xyonox.homy.model.User;

public class UserRepository {

    private final Dao<User, Integer> dao;

    public UserRepository() throws Exception {
        dao = DaoManager.createDao(
                DatabaseManager.getConnection(),
                User.class
        );
    }

    public void create(User user) throws Exception {
        dao.create(user);
    }

    public User findByUsername(String username) throws Exception {
        return dao.queryBuilder()
                .where()
                .eq("username", username)
                .queryForFirst();
    }

    public User findById(int id) throws Exception {
        return dao.queryForId(id);
    }
}