package client.controller;

import org.codehaus.jackson.map.ObjectMapper;
import shared.request.Request;
import shared.response.Response;

import java.io.*;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

import shared.util.ByteStream;
import shared.util.Jackson;

public class ServerController {
    private Socket socket;
    private final Supplier<Socket> socketSupplier;
    private String authToken;
    private BufferedOutputStream writer;
    private BufferedInputStream reader;
    private ByteStream byteStream;
    private final ObjectMapper objectMapper = Jackson.getNetworkObjectMapper();

    public ServerController(Socket socket , Supplier<Socket> socketSupplier) throws IOException {
        this.socketSupplier = socketSupplier;
        this.socket = socket;
        setConnection();
    }

    public Response sendMessage(Request request){
        try {
            request.setAuthToken(authToken);
            byteStream.sendByte(objectMapper.writeValueAsString(request));
            String response = byteStream.readByte();

            return objectMapper.readValue(response , Response.class);

        }catch (IOException | NoSuchElementException e){
            //offline
            //e.printStackTrace();
            MainController.getInstance().setOnline(false);
            System.out.println("Disconnected!");
            return null;
        }
    }

    public boolean reconnect() {
        socket = socketSupplier.get();
        try {
            setConnection();
            return true;
        }catch (IOException | NullPointerException e){
            return false;
        }
    }

    public void closeSocket(){
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setConnection() throws IOException {
        writer = new BufferedOutputStream(socket.getOutputStream());
        reader = new BufferedInputStream(socket.getInputStream());
        byteStream = new ByteStream(writer , reader);
        authToken = byteStream.readByte();
        System.out.println("Connected!");
    }
}
