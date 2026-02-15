package de.xyonox.homy.listeners;

import de.craftsblock.craftscore.event.EventHandler;
import de.craftsblock.craftscore.event.ListenerAdapter;
import de.craftsblock.craftsnet.autoregister.meta.AutoRegister;
import de.craftsblock.craftsnet.events.requests.PreRequestEvent;

@AutoRegister
public class PreRequestListener implements ListenerAdapter {
    @EventHandler
    public void authHandle(PreRequestEvent event){

    }
}
