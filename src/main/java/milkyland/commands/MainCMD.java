package milkyland.commands;

import milkyland.utils.ChatUtil;
import milkyland.utils.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.Arrays;
import java.util.List;

public class MainCMD implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("zChatBubble.use")) {
            ChatUtil.sendConfigMessage(sender, "error.not_enough_perms");
            return true;
        }

        if (args.length == 0) {
            sendHelpMessage(sender);
            return true;
        } else {
            switch (args[0].toLowerCase()) {
                case "reload":
                    ConfigManager.instance.reload();
                    ChatUtil.sendConfigMessage(sender, "messages.reloaded");
                    return true;

                default:
                    sender.sendMessage(ChatUtil.color("&c[⚠] Unknown Command!"));
                    sendHelpMessage(sender);
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (sender.hasPermission("zChatBubble.use")) {
            return Arrays.asList("reload");
        }

        return null;
    }

    public void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(ChatUtil.color("&6zChatBubble"));
        sender.sendMessage(ChatUtil.color("&eCommands available:"));
        sender.sendMessage(ChatUtil.color("&a- /zch reload"));
    }
}
