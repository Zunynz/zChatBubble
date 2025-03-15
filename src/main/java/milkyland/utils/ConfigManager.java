package milkyland.utils;

import milkyland.zchatbubble.Plugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {

    public final static ConfigManager instance = new ConfigManager();

    private Map<String, YamlConfiguration> configs = new HashMap<>();

    public void init(String... fileNames) {
        for (String fileName: fileNames) {
            fileName = fileName + ".yml";

            File file = new File(Plugin.getInstance().getDataFolder().getAbsolutePath() + "/" + fileName);

            if (!file.exists()) {
                Plugin.getInstance().saveResource(fileName, false);
            }

            configs.put(fileName, YamlConfiguration.loadConfiguration(file));
        }
    }

    public void reload() {
        for (String fileName : configs.keySet()) {
            File file = new File(Plugin.getInstance().getDataFolder().getAbsolutePath() + "/" + fileName);
            if (file.exists()) {
                configs.put(fileName, YamlConfiguration.loadConfiguration(file));
            }
        }
    }

    public YamlConfiguration get(String configName) {
        return configs.get(configName + ".yml");
    }
}