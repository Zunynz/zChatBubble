package milkyland.listeners;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import milkyland.utils.ChatUtil;
import milkyland.utils.ConfigManager;
import milkyland.zchatbubble.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerChatListener implements Listener {
    private final Map<Player, Hologram> playerHolograms = new HashMap<>();
    private static final ConfigurationSection config = ConfigManager.instance.get("config");
    private static final double BASE_HEIGHT = config.getDouble("settings.base_line_height");
    private static final double ADDED_HEIGHT = config.getDouble("settings.added_height");
    private static final int limit = config.getInt("settings.letters_in_message_limit");
    private final boolean isChatExEnabled;

    public PlayerChatListener(boolean isChatExEnabled) {
        this.isChatExEnabled = isChatExEnabled;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();

        if (limit > 0 && e.getMessage().length() > limit) {
            ChatUtil.sendConfigMessage(player,
                    "messages.letters_in_message_limit"
                            .replace("%limitNumber%", String.valueOf(limit)));
            e.setCancelled(true);
            return;
        }

        if (isChatExEnabled && e.isCancelled()) {
            return;
        }

        String message = e.getMessage();
        List<String> lines = new ArrayList<>();
        int maxLineLength = config.getInt("settings.max_letters_per_line", 25);
        String symbol = config.getString("settings.symbol_at_the_beginning", "");

        if (message.length() <= maxLineLength) {
            lines.add(symbol + message);
        } else {
            lines.add(symbol + message.substring(0, maxLineLength));
            for (int i = maxLineLength; i < message.length(); i += maxLineLength) {
                lines.add(message.substring(i, Math.min(i + maxLineLength, message.length())));
            }
        }

        if (playerHolograms.containsKey(player)) {
            Hologram oldHologram = playerHolograms.get(player);
            DHAPI.removeHologram(oldHologram.getId());
        }

        double hologramHeight = BASE_HEIGHT + (lines.size() - 1) * ADDED_HEIGHT;
        Location loc = player.getLocation().clone();

        Hologram hologram = DHAPI.createHologram(player.getUniqueId().toString(),
                loc.add(0, hologramHeight, 0),
                false,
                lines);
        hologram.setDefaultVisibleState(false);
        hologram.setHidePlayer(player);

        for (Player players : Bukkit.getOnlinePlayers()) {
            if (players.getWorld().equals(loc.getWorld()) && players.getLocation().distance(loc) <= 15) {
                hologram.setShowPlayer(players);
            }
        }

        playerHolograms.put(player, hologram);

        new BukkitRunnable() {
            @Override
            public void run() {
                DHAPI.removeHologram(hologram.getId());
                playerHolograms.remove(player);
            }
        }.runTaskLater(Plugin.getInstance(), config.getInt("settings.hologram_show_time") * 20L);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline() || !playerHolograms.containsKey(player)) {
                    cancel();
                    return;
                }
                Hologram current = playerHolograms.get(player);
                Location playerLoc = player.getLocation().clone();
                playerLoc.add(0, hologramHeight, 0);

                DHAPI.moveHologram(current, playerLoc);
            }
        }.runTaskTimer(Plugin.getInstance(), 0L, 1L);
    }
}
