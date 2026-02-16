package de.xyonox.homy.config;


import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import de.xyonox.homy.model.*;

public class DatabaseManager {

    private static final String DATABASE_URL =
            "jdbc:mysql://localhost:3306/homy";

    private static ConnectionSource connectionSource;

    public static void init(String url, String username,String password) throws Exception {
        connectionSource = new JdbcConnectionSource(
                url, username, password);

        Runtime.getRuntime().addShutdownHook(new Thread(DatabaseManager::shutdown));
    }

    public static void init() throws Exception {
        // TODO: .env
        init(DATABASE_URL, "root", "MeinSicheresPW123!");

        TableUtils.createTableIfNotExists(connectionSource, User.class);
        TableUtils.createTableIfNotExists(connectionSource, Token.class);
        TableUtils.createTableIfNotExists(connectionSource, Video.class);
    }

    public static ConnectionSource getConnection() {
        return connectionSource;
    }

    public static void shutdown() {
        try {
            if (connectionSource != null) {
                connectionSource.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}