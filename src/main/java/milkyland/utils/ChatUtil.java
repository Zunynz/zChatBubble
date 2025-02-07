package milkyland.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class ChatUtil {

    public static String prefix = color("&e&l[Milky&6&lPlugin] ");

    public static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static List<String> applyArgs(List<String> text, Map<String, String> args) { // (list, Map.of(Pair("key", "value"))
        for (int i = 0; i < text.size(); i++) {
            String line = text.get(i);

            for (String arg: args.keySet()) {
                line = line.replace(arg, args.get(arg));
            }

            text.set(i, color(line));
        }

        return text;
    }

    public static void sendMessage(CommandSender recipient, String message) {
        recipient.sendMessage(color(message));
    }

    public static void sendConfigMessage(CommandSender recipient, String configPath) {
        sendMessage(recipient, ConfigManager.instance.get("messages").getString(configPath));
    }

    public static void sendConfigMessage(CommandSender recipient, String configPath, Map<String, String> args) {
        String message = ConfigManager.instance.get("messages").getString(configPath);

        for (String key: args.keySet()) {
            message = message.replace(key, args.get(key));
        }

        sendMessage(recipient, message);
    }

    public static void sendTitle(Player recipient, String message, String subMsg) {
        recipient.sendTitle(message, subMsg, 10, 40, 10);
    }

    public static void sendConfigTitle(Player recipient, String messagePath, String subMessagePath) {
        if (ConfigManager.instance.get("messages").getString(subMessagePath) == null) {
            sendTitle(
                    recipient,
                    ConfigManager.instance.get("messages").getString(messagePath),
                    ""
            );
        }
        else {
            sendTitle(
                    recipient,
                    ConfigManager.instance.get("messages").getString(messagePath),
                    ConfigManager.instance.get("messages").getString(subMessagePath)
            );
        }
    }

    public static void sendConfigTitle(Player recipient, String messagePath, String subMessagePath, Map<String, String> args) {
        String message = ConfigManager.instance.get("messages").getString(messagePath);
        String subMessage;

        if (!subMessagePath.equals("")) {
            subMessage = ConfigManager.instance.get("messages").getString(subMessagePath);
        }
        else {
            subMessage = "";
        }

        assert message != null;

        for (String key: args.keySet()) {
            message = message.replace(key, args.get(key));
        }

        if (subMessage != null) {
            for (String key: args.keySet()) {
                subMessage = subMessage.replace(key, args.get(key));
            }
        }

        if (subMessage == null) {
            sendTitle(
                    recipient,
                    color(message),
                    ""
            );
        }
        else {
            sendTitle(
                    recipient,
                    color(message),
                    color(subMessage)
            );
        }
    }
}