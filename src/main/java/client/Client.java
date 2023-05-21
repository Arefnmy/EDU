package client;

import client.controller.MainController;
import client.controller.ServerController;
import client.filemanager.ResourceManager;
import client.gui.MainFrame;
import shared.util.Config;

import java.io.IOException;
import java.net.Socket;

public class Client implements Runnable{
    private final String host;
    private final int port;
    private Socket socket;
    private final Config config;

    private final ResourceManager resourceManager = ResourceManager.getInstance();
    private final MainController mainController = MainController.getInstance();
    public Client(Config config) throws IOException {
        this.config = config;
        host = config.getProperty(String.class , "host").orElse("localhost");
        port = config.getProperty(Integer.class , "port").orElse(8000);

        socket = new Socket(host , port);
    }

    @Override
    public void run() {
        try {
            ServerController serverController = new ServerController(socket , this::reconnect);
            MainFrame mainFrame = new MainFrame();
            mainController.setServerController(serverController);
            mainController.setMainFrame(mainFrame);
            mainController.setOnline(true);
            resourceManager.setMainPath(config);
            resourceManager.setGraphicConfig(new Config(config.getProperty("graphic-config")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket reconnect(){
        try {
            socket = new Socket(host , port);
            return socket;
        } catch (IOException e) {
            return null;
        }
    }
}
