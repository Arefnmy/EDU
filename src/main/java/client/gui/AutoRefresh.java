package client.gui;

import client.controller.MainController;
import shared.util.Loop;

public interface AutoRefresh {

    void getResponse() throws NullPointerException;
    void setInfo();

    private Runnable refresh(){
        return ()->{
            try {
                getResponse();
            }catch (NullPointerException ignored){}
            setInfo();
        };
    }
    default void setLoop(double fps){
        MainController.getInstance().setLoop(new Loop(fps , refresh()));
    }
}
