package server;

import shared.constants.Constants;
import server.filemanager.JsonManager;
import shared.util.Config;

import java.io.IOException;
import java.util.Scanner;

public class ServerMain {
    public static void main(String[] args) throws IOException {

        Config config = new Config(Constants.CONFIG.getProperty("server-config"));
        Server server = new Server(config);

        server.loadDatabase();
        new Thread(server::accept).start();
        server.startSaveLoop();

        new Thread(
                ()->{
                    Scanner scanner = new Scanner(System.in);
                    while (true){
                        String line = scanner.nextLine();
                        switch (line){
                            case "stop accepting" :
                                server.setRunning(false);
                                break;
                            case "start accepting" :
                                server.setRunning(true);
                                break;
                            case "exit":
                                server.stopSaveLoop();
                                System.exit(0);
                        }
                    }
                }
        ).start();

    }
}
