package de.xyonox.homy.repository;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import de.xyonox.homy.config.DatabaseManager;
import de.xyonox.homy.model.Token;
import de.xyonox.homy.model.User;

import java.sql.SQLException;
import java.util.Optional;

public class TokenRepository {

    private final Dao<Token, Integer> dao;

    public TokenRepository() throws SQLException {
        this.dao = DaoManager.createDao(
                DatabaseManager.getConnection(),
                Token.class
        );
    }

    public void create(Token token) throws SQLException {
        dao.create(token);
    }

    public Token findByHashToken(String hashToken) throws SQLException {
        Token token = dao.queryBuilder()
                .where()
                .eq("hashToken", hashToken)
                .queryForFirst();

        return token;
    }

    public void deleteByUser(User user) throws SQLException {
        dao.delete(
                dao.queryBuilder()
                        .where()
                        .eq("user_id", user.getId())
                        .query()
        );
    }

    public Optional<Token> findById(int id) throws SQLException {
        return Optional.ofNullable(dao.queryForId(id));
    }

    public void delete(Token token) throws SQLException {
        dao.delete(token);
    }
}