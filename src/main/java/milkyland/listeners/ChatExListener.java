package milkyland.listeners;

import de.jeter.chatex.api.events.MessageBlockedByAdManagerEvent;
import de.jeter.chatex.api.events.MessageBlockedBySpamManagerEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatExListener implements Listener {

    @EventHandler
    public void onMessageBlockedBySpam(MessageBlockedBySpamManagerEvent e) {
    }

    @EventHandler
    public void onMessageBlockedByAd(MessageBlockedByAdManagerEvent e) {
    }
}
