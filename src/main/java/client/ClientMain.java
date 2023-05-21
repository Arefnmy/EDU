package client;

import shared.constants.Constants;
import shared.util.Config;
import shared.util.FileUploader;

import java.io.IOException;

public class ClientMain {
    public static void main(String[] args){
        Config config = new Config(Constants.CONFIG.getProperty("client-config"));
        FileUploader.setPath(config);
        Client client = null;
        try {
            client = new Client(config);
        } catch (IOException e) {
            System.out.println("Server not found!");
        }
        new Thread(client).start();
    }
}
