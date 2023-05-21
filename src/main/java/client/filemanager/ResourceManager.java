package client.filemanager;


import shared.util.Config;

import javax.swing.*;
import java.io.File;

public class ResourceManager {
    private String mainPath ="src/main/resources/client/";
    private final String IMAGE_PATH = mainPath + "images/";
    private final String FILE_PATH = mainPath + "files/";
    private Config graphicConfig = new Config(mainPath + "configs/graphic.properties");

    private static final ResourceManager RESOURCE_MANAGER = new ResourceManager();

    public static ResourceManager getInstance(){
        return RESOURCE_MANAGER;
    }

    public void setMainPath(Config config){
        mainPath = config.getProperty("resource-path");
    }

    public void setGraphicConfig(Config config){
        graphicConfig = config;
    }

    public ImageIcon getImage(ImageType imageType){
        return getImage(imageType.getPathName());
    }

    public ImageIcon getImage(String name){
        return new ImageIcon(IMAGE_PATH + name);
    }

    public ImageIcon getImage(byte[] bytes){
        return new ImageIcon(bytes);
    }

    public File getMessages(String name){
        return new File(FILE_PATH + "messages/" + name);
    }

    public File getMarkedLessons(String name){
        return new File(FILE_PATH + "markedlessons/" + name);
    }

    public Config getGraphicConfig(){
        return graphicConfig;
    }

    public String getMainPath() {
        return mainPath;
    }

    public <E> E getValue(Class<E> c , String key, E defaultValue){
        return graphicConfig.getProperty(c , key).orElse(defaultValue);
    }
}
