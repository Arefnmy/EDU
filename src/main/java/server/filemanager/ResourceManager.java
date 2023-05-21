package server.filemanager;

import shared.util.Config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ResourceManager {
    private String mainPath = "src/main/resources/server/";
    private final String IMAGE_PATH = mainPath + "images/";
    private final String UNKNOWN_IMAGE_PATH = IMAGE_PATH + "users/unknown.png";
    private Config logicConfig = new Config(mainPath + "configs/logic.properties");
    private static final ResourceManager RESOURCE_MANAGER = new ResourceManager();

    public static ResourceManager getInstance(){
        return RESOURCE_MANAGER;
    }

    public void setMainPath(Config config){
        mainPath = config.getProperty("resource-path");
    }

    public void setLogicConfig(Config config){
        logicConfig = config;
    }

    public byte[] getUserImage(long code){
        String path = IMAGE_PATH + "users/" + code + ".png";
        try {
            return Files.readAllBytes(Path.of(path));
        } catch (IOException e) {
            try {
                return Files.readAllBytes(Path.of(UNKNOWN_IMAGE_PATH));
            } catch (IOException ex) {
                return null;
            }
        }
    }

    public String getMainPath() {
        return mainPath;
    }

    public Config getLogicConfig() {
        return logicConfig;
    }

    public <E> E getValue(Class<E> c , String key , E defaultValue){
        return logicConfig.getProperty(c , key).orElse(defaultValue);
    }
}
