package de.xyonox.homy;

import de.craftsblock.craftsnet.CraftsNet;
import de.craftsblock.craftsnet.addon.Addon;
import de.craftsblock.craftsnet.addon.meta.annotations.Meta;
import de.craftsblock.craftsnet.builder.ActivateType;

import java.io.IOException;

@Meta(name = "Homy")
public class Application extends Addon {

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
    }
}
