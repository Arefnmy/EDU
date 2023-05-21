package server;

import server.controllers.ChooseLessonController;
import server.controllers.ClientHandler;
import server.controllers.Controller;
import server.controllers.LoginController;
import server.database.LessonDatabase;
import server.filemanager.JsonManager;
import server.filemanager.ResourceManager;
import shared.request.Request;
import shared.request.RequestType;
import shared.response.Response;
import shared.response.ResponseState;
import shared.util.Config;
import shared.util.Loop;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.*;

public class Server {
    private final ServerSocket serverSocket;
    private final JsonManager jsonManager;
    private final Loop saveLoop;
    private final int port;

    private boolean running;
    private final List<ClientHandler> handlers;
    private final Map<ClientHandler , Controller> clientMap;
    private final Map<ClientHandler , LoginController> loginClientMap;

    private final SecureRandom secureRandom = new SecureRandom();
    private final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    public Server(Config config) throws IOException {
        port = config.getProperty(Integer.class, "port").orElse(8000);
        serverSocket = new ServerSocket(port);

        running = true;
        handlers = new ArrayList<>();
        clientMap = new HashMap<>();
        loginClientMap = new HashMap<>();
        jsonManager = new JsonManager();
        saveLoop = new Loop(config.getProperty(Double.class , "save-loop").orElse(.5) ,
                jsonManager::saveDatabase);
        ResourceManager.getInstance().setMainPath(config);
        ResourceManager.getInstance().setLogicConfig(new Config(config.getProperty("logic-config")));
    }

    public void accept() {
        while (running) {
            try {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket , generateAuthToken(),this);
                handlers.add(clientHandler);
                new Thread(clientHandler).start();
                System.out.println("New Client Added :" + socket.getRemoteSocketAddress().toString());
            } catch (IOException ignore) {
            }
        }
    }

    public void loadDatabase(){
        jsonManager.loadDatabase();
    }

    public void startSaveLoop(){
        saveLoop.start();
    }

    public void stopSaveLoop(){
        saveLoop.stop();
    }

    public String generateAuthToken(){
        byte[] bytes = new byte[24];
        secureRandom.nextBytes(bytes);
        return base64Encoder.encodeToString(bytes);
    }

    private ClientHandler getClientHandler(String authToken){
        for (ClientHandler c : handlers)
            if (c.getAuthToken().equals(authToken))
                return c;
        return null;
    }

    public Response getResponse(Request request, String authToken){
        ClientHandler clientHandler = getClientHandler(authToken);
        if (clientHandler == null)
            return new Response(ResponseState.WRONG_AUTH_TOKEN , "Wrong Auth Token!");

        if (request.getRequestType() == RequestType.LOGIN)
            return login(request, clientHandler);
        if (request.getRequestType() == RequestType.CLOSE)
            return new Response(null , null);

        Controller controller = clientMap.get(clientHandler);
        switch (request.getRequestType()){
            case GET_USER:
                return controller.getUser();
            case RESET_PASSWORD:
                return loginClientMap.get(clientHandler).changePassword(request);
            case CHANGE_PROFILE:
                return controller.changeProfile(request);
            case WEEKLY_SCHEDULE:
                return controller.getLessonInWeek();
            case EXAM_LIST:
                return controller.getExamList();
            case LESSON_LIST:
                return controller.getLessonList(request);
            case PROFESSOR_LIST:
                return controller.getProfessorList();
            case GET_ADD_USER:
                return controller.getAddUser();
            case ADD_USER:
                return controller.addUser(request);
            case EDIT_USER:
                return controller.editUser(request);
            case REMOVE_USER:
                return controller.removeUser(request);
            case GET_ADD_LESSON:
                return controller.getAddLesson();
            case ADD_LESSON:
                return controller.addLesson(request);
            case EDIT_LESSON:
                return controller.editLesson(request);
            case REMOVE_LESSON:
                return controller.removeLesson(request);
            case GET_REQUEST:
                return controller.getRequest();
            case ADD_REQUEST:
                return controller.addRequest(request);
            case HANDEL_REQUEST:
                return controller.handelRequest(request);
            case GET_All_CHATS:
                return controller.getAllChats();
            case GET_CREATE_CHAT:
                return controller.getCreateChat(request);
            case GET_CHAT:
                return controller.getChat(request);
            case SEND_MESSAGE:
                return controller.sendMessage(request);
            case SEND_REQUEST:
                return controller.sendRequestToMessage(request);
            case GET_STUDENTS:
                return controller.getStudents();
            case ADD_LESSON_TIME:
                return controller.addLessonTime(request);
            case GET_CHOOSE_LESSON:
                if (LessonDatabase.getInstance().getChooseLessonController() == null)
                    LessonDatabase.getInstance().setChooseLessonController(new ChooseLessonController());
                return controller.getChooseLesson(request);
            case GET_SEARCH:
                return controller.getSearch(request);
            case SET_END_CHOOSE_LESSON_TIME:
                return controller.setEndChooseLessonTime(request);
            case GET_NOTIFICATION:
                return controller.getNotification();
            case HANDEL_NOTIFICATION:
                return controller.handelNotification(request);
            case TAKE_LESSON:
                return controller.takeLesson(request);
            case TAKE_LESSON_FROM_ASSISTANT:
                return controller.takeLessonFromAssistant(request);
            case GET_OFFLINE_DATA:
                return controller.getOfflineData();
            case GET_COURSEWARE:
                return controller.getCourseware(request);
            case GET_ALL_COURSEWARES:
                return controller.getAllCourseware();
            case ADD_STUDENT_COURSEWARE:
                return controller.addStudentToCourseware(request);
            case EDIT_EDUCATIONAL_MATERIAL:
                return controller.editEducationalMaterial(request);
            case EDIT_EXERCISE:
                return controller.editExercise(request);
            case ADD_EDUCATIONAL_MATERIAL:
                return controller.addEducationalMaterial(request);
            case ADD_EXERCISE:
                return controller.addExercise(request);
            case GET_CALENDER:
                return controller.getCalender();
            case GET_EDUCATIONAL_STATUS:
                return controller.getEducationalStatus(request);
            case GET_TEMPORARY_SCORES:
                return controller.getTemporaryScores(request);
            case ADD_PROTEST:
                return controller.addProtest(request);
            case SET_REGISTER_PROTEST:
                return controller.setRegisterProtest(request);
            case REGISTER_SCORES:
                return controller.registerScores(request);
            case GET_SOLUTION:
                return controller.getSolution(request);
            case ADD_SOLUTION:
                return controller.addSolution(request);
            case REGISTER_SCORE_SOLUTION:
                return controller.registerScoreSolution(request);
        }
        return null;
    }

    public Response login(Request request, ClientHandler clientHandler){
        LoginController loginController = loginClientMap.get(clientHandler);
        if (loginController == null) {
            loginController = new LoginController();
            loginClientMap.put(clientHandler , loginController);
        }
        Response response = loginController.login(request);
        if (response.getResponseState() == ResponseState.OK) {
            clientMap.put(clientHandler , new Controller(loginController.getUser() , loginController.getMainMenuController()));
        }
        return response;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
