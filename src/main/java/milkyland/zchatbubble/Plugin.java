package milkyland.zchatbubble;

import lombok.Getter;
import milkyland.listeners.ChatExListener;
import milkyland.listeners.PlayerChatListener;
import milkyland.utils.ConfigManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Plugin extends JavaPlugin {
    @Getter
    private static Plugin instance;
    @Getter
    private boolean chatExEnabled = false;

    @Override
    public void onEnable() {
        instance = this;
        ConfigManager.instance.init("config", "messages");
        PluginManager pm = getServer().getPluginManager();

        boolean chatExInstalled = pm.getPlugin("ChatEx") != null;
        boolean userHasChatEx = ConfigManager.instance.get("config")
                .getBoolean("additional_plugins.chat_ex", false);

        credits();

        if (chatExInstalled && userHasChatEx) {
            getLogger().info("ChatEx was found and enabled in config.");
            chatExEnabled = true;
            pm.registerEvents(new ChatExListener(), this);
        } else {
            getLogger().info("ChatEx wasn't found in config or in plugins folder. Working without any additional plugins.");
        }

        pm.registerEvents(new PlayerChatListener(chatExEnabled), this);
    }

    @Override
    public void onDisable() {
        credits();
    }

    private void credits() {
        getLogger().info("\n" +
                "███████╗░█████╗░██╗░░██╗░█████╗░████████╗██████╗░██╗░░░██╗██████╗░██████╗░██╗░░░░░███████╗\n" +
                "╚════██║██╔══██╗██║░░██║██╔══██╗╚══██╔══╝██╔══██╗██║░░░██║██╔══██╗██╔══██╗██║░░░░░██╔════╝\n" +
                "░░███╔═╝██║░░╚═╝███████║███████║░░░██║░░░██████╦╝██║░░░██║██████╦╝██████╦╝██║░░░░░█████╗░░\n" +
                "██╔══╝░░██║░░██╗██╔══██║██╔══██║░░░██║░░░██╔══██╗██║░░░██║██╔══██╗██╔══██╗██║░░░░░██╔══╝░░\n" +
                "███████╗╚█████╔╝██║░░██║██║░░██║░░░██║░░░██████╦╝╚██████╔╝██████╦╝██████╦╝███████╗███████╗\n" +
                "╚══════╝░╚════╝░╚═╝░░╚═╝╚═╝░░╚═╝░░░╚═╝░░░╚═════╝░░╚═════╝░╚═════╝░╚═════╝░╚══════╝╚══════╝");
        getLogger().info("Made by Zunynz");
        getLogger().info(" ");
    }
}
