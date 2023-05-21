package server.controllers;

import org.codehaus.jackson.map.ObjectMapper;
import server.Server;
import shared.request.Request;
import shared.request.RequestType;
import shared.response.Response;
import shared.util.ByteStream;
import shared.util.Jackson;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final BufferedOutputStream writer;
    private final BufferedInputStream reader;
    private final ByteStream byteStream;
    private final ObjectMapper objectMapper = Jackson.getNetworkObjectMapper();
    private final Server server;
    private final String authToken;

    public ClientHandler(Socket socket , String authToken, Server server) throws IOException {
        writer = new BufferedOutputStream(socket.getOutputStream());
        reader = new BufferedInputStream(socket.getInputStream());
        byteStream = new ByteStream(writer , reader);
        this.server = server;
        this.authToken = authToken;
    }

    @Override
    public void run() {
        try {
            byteStream.sendByte(authToken);
            listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen() throws IOException {
        while (true){
            Request request = objectMapper.readValue(byteStream.readByte() , Request.class);
            Response response = server.getResponse(request , request.getAuthToken());
            byteStream.sendByte(objectMapper.writeValueAsString(response));
            if (request.getRequestType() == RequestType.CLOSE)
                break;
        }
    }

    public String getAuthToken() {
        return authToken;
    }
}
