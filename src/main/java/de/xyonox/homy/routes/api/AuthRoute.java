package de.xyonox.homy.routes.api;

import de.craftsblock.craftscore.json.Json;
import de.craftsblock.craftsnet.api.http.*;
import de.craftsblock.craftsnet.api.http.annotations.RequestMethod;
import de.craftsblock.craftsnet.api.http.annotations.RequireBody;
import de.craftsblock.craftsnet.api.http.annotations.Route;
import de.craftsblock.craftsnet.api.http.body.bodies.JsonBody;
import de.craftsblock.craftsnet.autoregister.meta.AutoRegister;
import de.xyonox.homy.Application;
import de.xyonox.homy.model.User;
import de.xyonox.homy.services.AuthService;

import java.util.Optional;

@AutoRegister
@Route("/api/auth")
public class AuthRoute implements RequestHandler {

    private static final int TOKEN_MAX_AGE_SECONDS = 30 * 24 * 60 * 60; // 30 Tage
    private static final int MAX_FIELD_LENGTH = 254;

    @Route("/login")
    @RequestMethod(HttpMethod.POST)
    @RequireBody(JsonBody.class)
    public void login(Exchange exchange) {
        Request request = exchange.request();
        Response response = exchange.response();
        Json body = request.getBody().getAsJsonBody().getBody();

        Json answer = Json.empty();

        String username = readRequiredString(body, "username");
        String password = readRequiredString(body, "password");

        if (username == null || password == null) {
            response.setCode(400);
            response.print(answer
                    .set("code", 400)
                    .set("error", "Missing or invalid username or password")
            );
            return;
        }

        AuthService as = Application.getInstance().getAuthService();
        try {
            Optional<String> tokenOption = as.login(username, password);
            if (tokenOption.isEmpty()) {
                response.setCode(401);
                response.print(answer
                        .set("code", 401)
                        .set("error", "Invalid username or password")
                );
            } else {
                response.setCookie("token", tokenOption.get())
                        .setPath("/")
                        .setHttpOnly(true)
                        .setSecure(true)
                        .setSameSite("Strict")
                        .setMaxAge(TOKEN_MAX_AGE_SECONDS);

                response.setCode(200);
                response.print(answer
                        .set("code", 200)
                        .set("message", "login successful")
                );
            }
        } catch (Exception e) {
            response.setCode(500);
            response.print(Json.empty()
                    .set("code", 500)
                    .set("error", "Internal authentication error")
            );
        }
    }

    @Route("/register")
    @RequestMethod(HttpMethod.POST)
    @RequireBody(JsonBody.class)
    public void register(Exchange exchange) {
        Request request = exchange.request();
        Response response = exchange.response();
        Json body = request.getBody().getAsJsonBody().getBody();

        Json answer = Json.empty();

        String username = readRequiredString(body, "username");
        String password = readRequiredString(body, "password");
        String email = readRequiredString(body, "email");

        if (username == null || password == null || email == null) {
            response.setCode(400);
            response.print(answer
                    .set("code", 400)
                    .set("error", "Missing or invalid username, password or email")
            );
            return;
        }

        AuthService as = Application.getInstance().getAuthService();
        Optional<User> userOpt;
        try {
            userOpt = as.register(username, email, password);
        } catch (Exception e) {
            response.setCode(500);
            response.print(Json.empty()
                    .set("code", 500)
                    .set("error", "Internal authentication error")
            );
            return;
        }

        if (userOpt.isEmpty()) {
            response.setCode(409);
            response.print(answer
                    .set("code", 409)
                    .set("error", "Username or Email already exists")
            );
            return;
        }

        response.setCode(201);
        response.print(Json.empty()
                .set("code", 201)
                .set("message", "Registration successful")
        );
    }

    private static String readRequiredString(Json body, String field) {
        if (body == null || !body.contains(field)) return null;

        String value;
        try {
            value = body.getString(field);
        } catch (Exception ignored) {
            return null;
        }

        if (value == null) return null;

        value = value.trim();
        if (value.isEmpty()) return null;
        if (value.length() > MAX_FIELD_LENGTH) return null;

        return value;
    }
}
