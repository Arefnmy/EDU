package shared.response;

import java.util.HashMap;
import java.util.Map;

public class Response {
    private ResponseState responseState;
    private String message;
    private Map<Object , Object> data;

    private Response(){};
    public Response(ResponseState responseState, String message) {
        this.responseState = responseState;
        this.message = message;
        data = new HashMap<>();
    }

    public void addData(Object key , Object value){
        data.put(key , value);
    }

    public Object getData(Object key){
        return data.get(key);
    }

    public ResponseState getResponseState() {
        return responseState;
    }

    public String getMessage() {
        return message;
    }
}
