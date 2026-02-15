package de.xyonox.homy.config;


import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import de.xyonox.homy.model.*;

public class DatabaseManager {

    private static final String DATABASE_URL =
            "jdbc:postgresql://localhost:5432/homy";

    private static ConnectionSource connectionSource;

    public static void init(String url, String username,String password) throws Exception {
        connectionSource = new JdbcConnectionSource(
                url, username, password);
    }

    public static void init() throws Exception {
        init(DATABASE_URL, "homy", "qwertzuikmjLOeAe1");

        TableUtils.createTableIfNotExists(connectionSource, User.class);
    }

    public static ConnectionSource getConnection() {
        return connectionSource;
    }
}