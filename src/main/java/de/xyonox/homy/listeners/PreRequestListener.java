package de.xyonox.homy.listeners;

import de.craftsblock.craftscore.event.EventHandler;
import de.craftsblock.craftscore.event.ListenerAdapter;
import de.craftsblock.craftscore.json.Json;
import de.craftsblock.craftsnet.api.http.Exchange;
import de.craftsblock.craftsnet.api.http.Request;
import de.craftsblock.craftsnet.api.http.Response;
import de.craftsblock.craftsnet.api.http.cookies.Cookie;
import de.craftsblock.craftsnet.autoregister.meta.AutoRegister;
import de.craftsblock.craftsnet.events.requests.PreRequestEvent;
import de.xyonox.homy.Application;
import de.xyonox.homy.model.User;

import java.sql.SQLException;
import java.util.Optional;

@AutoRegister
public class PreRequestListener implements ListenerAdapter {

    private static final String TOKEN_COOKIE = "token";

    @EventHandler
    public void authHandle(PreRequestEvent event) {
        Exchange exchange = event.getExchange();
        Request request = exchange.request();
        Response response = exchange.response();

        // String path = request.getUrl();
        // TODO: Routen auch ohne Auth oder nur ohne auth erreichbar (regex)

        if (!request.getCookies().containsKey(TOKEN_COOKIE)) {
            // Falls möglich: echten HTTP-Status setzen (API abhängig)
            response.print(Json.empty()
                    .set("status", 401)
                    .set("auth", "Authentication failed: no existing token!")
            );
            event.setCancelled(true);
            return;
        }

        Cookie tokenCookie = request.getCookies().get(TOKEN_COOKIE);
        String rawToken = tokenCookie != null ? tokenCookie.getValue() : null;

        if (rawToken == null || rawToken.isBlank()) {
            response.print(Json.empty()
                    .set("status", 401)
                    .set("auth", "Authentication failed: empty token!")
            );
            event.setCancelled(true);
            return;
        }

        final Optional<User> token;
        try {
            token = Application.getInstance().getAuthService().authenticateToken(rawToken);
        } catch (SQLException e) {
            response.print(Json.empty()
                    .set("status", 500)
                    .set("error", "Internal authentication error")
            );
            event.setCancelled(true);
            return;
        }

        if (token.isEmpty()) {
            response.print(Json.empty()
                    .set("status", 401)
                    .set("auth", "Authentication failed: invalid token!")
            );
            event.setCancelled(true);
            return;
        }

        token.ifPresent(user -> exchange.context().put(User.class, user));
    }
}
