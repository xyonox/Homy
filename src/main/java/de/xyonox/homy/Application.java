package de.xyonox.homy;

import de.craftsblock.craftsnet.CraftsNet;
import de.craftsblock.craftsnet.addon.Addon;
import de.craftsblock.craftsnet.addon.meta.annotations.Meta;
import de.craftsblock.craftsnet.builder.ActivateType;
import de.xyonox.homy.config.DatabaseManager;
import de.xyonox.homy.repository.TokenRepository;
import de.xyonox.homy.repository.UserRepository;
import de.xyonox.homy.services.AuthService;
import lombok.Getter;

import java.io.IOException;

@Meta(name = "Homy")
public class Application extends Addon {

    @Getter
    private static Application instance;

    @Getter
    private TokenRepository tokenRepository;
    @Getter
    private UserRepository userRepository;
    @Getter
    private AuthService authService;


    static void main(String[] args) {
        try {
            CraftsNet craftsNet = CraftsNet.create(Application.class)
                    .withArgs(args)
                    .withAddonSystem(ActivateType.ENABLED)
                    .withDebug(true)
                    .withLogRotate(50)
                    .withSkipVersionCheck(true)
                    .withWebServer(ActivateType.DYNAMIC, 9090)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;

        try {
            DatabaseManager.init();
            this.tokenRepository = new TokenRepository();
            this.userRepository = new UserRepository();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.authService = new AuthService(userRepository, tokenRepository);

    }
}
