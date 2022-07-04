package me.tapeline.quailstudio.utils;

import me.tapeline.quailstudio.Main;
import org.apache.commons.io.FileUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Configuration {

    public Map<String, Object> cfg;
    public String configPath;

    public Configuration(String configurationPath) throws IOException {
        configPath = configurationPath;
        Yaml yaml = new Yaml();
        if (!new File(configurationPath).exists())
            reset();
        cfg = yaml.load(FileUtils.readFileToString(new File(configurationPath), "UTF-8"));
        if (cfg == null || cfg.size() == 0) {
            reset();
        }
    }

    public void reset() throws IOException {
        cfg = new LinkedHashMap<>();
        cfg.put("font-size", 16);
        cfg.put("quail-name", "QuailReforged.jar");
        cfg.put("quail-home", "runtime");
        if (Main.isUnix()) {
            cfg.put("run-cmd", "java -jar %home/%name false %file");
        } else {
            cfg.put("run-cmd", "java -jar %home/%name false %file");
        }
        cfg.put("theme", "dark");
        cfg.put("last-projects", new ArrayList<String>());
        saveConfig();
    }

    public void saveConfig() throws IOException {
        Yaml yaml = new Yaml();
        FileUtils.writeStringToFile(new File(configPath), yaml.dump(cfg).trim(), "UTF-8");
    }

}
