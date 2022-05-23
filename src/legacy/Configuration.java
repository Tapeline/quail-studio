package me.tapeline.quailstudio;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class Configuration {

    public Map<String, Object> cfg;
    public String configPath;

    public Configuration(String configurationPath) {
        configPath = configurationPath;
        Yaml yaml = new Yaml();
        if (!new File(configurationPath).exists())
            reset();
        cfg = yaml.load(IOManager.fileInput(configurationPath));
        if (cfg == null || cfg.size() == 0) {
            reset();
        }
    }

    public void reset() {
        cfg = new LinkedHashMap<>();
        cfg.put("font-size", 16);
        cfg.put("quail-name", "QuailReforged.jar");
        cfg.put("quail-home", "runtime");
        if (Main.isUnix()) {
            cfg.put("run-cmd", "xterm -e bash quailStart.sh %home/%name %file");
        } else {
            cfg.put("run-cmd", "java -jar %home/%name false %file");
        }
        saveConfig();
    }

    public void saveConfig() {
        Yaml yaml = new Yaml();
        IOManager.fileSet(configPath, yaml.dump(cfg).trim());
    }

}
