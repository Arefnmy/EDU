package shared.request;

import java.util.HashMap;
import java.util.Map;

public class Request {
    private RequestType requestType;
    private Map<Object , Object> data;
    private String authToken;

    private Request(){};
    public Request(RequestType requestType){
        this.requestType = requestType;
        data = new HashMap<>();
    }

    public void addData(Object key , Object value){
        data.put(key , value);
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public Object getData(Object key){
        return data.get(key);
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
