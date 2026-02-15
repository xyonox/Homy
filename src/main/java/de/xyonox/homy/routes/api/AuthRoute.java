package de.xyonox.homy.routes.api;

import de.craftsblock.craftscore.json.Json;
import de.craftsblock.craftsnet.api.http.*;
import de.craftsblock.craftsnet.api.http.annotations.RequestMethod;
import de.craftsblock.craftsnet.api.http.annotations.RequireBody;
import de.craftsblock.craftsnet.api.http.annotations.Route;
import de.craftsblock.craftsnet.api.http.body.bodies.JsonBody;
import de.craftsblock.craftsnet.autoregister.meta.AutoRegister;
import de.xyonox.homy.Application;
import de.xyonox.homy.services.AuthService;

import java.util.Optional;

@AutoRegister
@Route("/api/auth")
public class AuthRoute implements RequestHandler {
    @Route("/login")
    @RequestMethod(HttpMethod.POST)
    @RequireBody(JsonBody.class)
    public void login(Exchange exchange) {
        Request request = exchange.request();
        Response response = exchange.response();
        Json body = request.getBody().getAsJsonBody().getBody();

        Json answer = Json.empty();

        if (!body.contains("username") || !body.contains("password")){
            response.setCode(400);
            response.print(answer
                    .set("error", "Missing username or password")
                    .set("code", 400)
            );
            return;
        }

        String username = body.getString("username");
        String password = body.getString("password");

        AuthService as = Application.getInstance().getAuthService();
        try {
            Optional<String> tokenOption = as.login(username, password);
            if(tokenOption.isEmpty()){
                response.setCode(401);
                response.print(answer
                        .set("error", "Invalid username or password")
                        .set("code", 401)
                );
            } else {
                response.setCookie("token", tokenOption.get()).setPath("/");
                response.setCode(200);
                response.print(answer
                        .set("code", 200)
                        .set("message", "login successful")
                );
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
