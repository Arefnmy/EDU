package server.filemanager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import server.controllers.ChooseLessonController;
import server.database.*;
import server.model.College;
import server.model.Courseware;
import server.model.Lesson;
import server.model.chatroom.Chat;
import server.model.chatroom.ChatMessage;
import server.model.educationalrequest.*;
import server.model.users.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import shared.model.*;
import shared.model.courseware.EducationalMaterial;
import shared.model.courseware.Exercise;
import shared.model.courseware.Solution;
import shared.model.educaionalrequests.Protest;
import shared.model.media.Media;
import shared.model.media.MediaType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class JsonManager {
     static UserDatabase userDatabase = UserDatabase.getInstance();
     static LessonDatabase lessonDatabase = LessonDatabase.getInstance();
     static CollegeDatabase collegeDatabase = CollegeDatabase.getInstance();
     static RequestDatabase requestDatabase = RequestDatabase.getInstance();
     static ChatDatabase chatDatabase = ChatDatabase.getInstance();
     private final Gson gson;
     private final String path;

    public JsonManager() {
        GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting().serializeNulls();
        gson = gsonBuilder.create();

        path = ResourceManager.getInstance().getMainPath() + "jsons/";
    }

    public void saveDatabase(){
        try {
            Files.walk(Paths.get(path))
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .forEach(File::delete);

            saveColleges();
            saveUsers();
            saveLessons();
            saveRequests();
            saveChat();
            saveCourseware();
            saveSolution();
            saveProtest();
            saveChooseLesson();
        }catch (IOException ignored){}
    }

    public void saveColleges() throws IOException {
        for (College c : collegeDatabase.getColleges()){
            String path = this.path + "colleges/" + c.getName() + ".json" ;
            write(path , c);
        }
    }

    public void saveUsers() throws IOException {
        for (Student s : userDatabase.getStudents()){
            String path = this.path + "users/students/" + s.getUserCode() + ".json" ;
            write(path , s);
        }

        for (AdminEdu a : userDatabase.getAdmins()){
            String path = this.path + "users/admins/" + a.getUserCode() + ".json" ;
            write(path , a);
        }

        Mohseni m = userDatabase.getMohseni();
        write(this.path + "users/mohseni/" + m.getUserCode() + ".json" , m);

        for (Professor p : userDatabase.getProfessors()){
            String path = this.path + "users/professors/" ;
            if (p instanceof EducationalAssistant)
                path += "educationalassistants/";
            if (p instanceof  CollegeBoss)
                path += "collegebosses/";
            path += p.getUserCode() +".json";
            write(path , p);
        }
    }

    public void saveLessons() throws IOException {
        for (Lesson l : lessonDatabase.getLessons()){
            String path = this.path + "lessons/" + l.getNumber() + ".json";
            write(path , l);
        }
    }

    public void saveRequests() throws IOException {
        String path = this.path + "requests/";
        for (Recommendation request : requestDatabase.getRecommendations())
            write(path + "recommendation/" + request.getRequestNumber() + ".json" , request);
        for (BusyStudying request : requestDatabase.getBusyStudyings())
            write(path +"busystundying/" + request.getRequestNumber() + ".json" , request);
        for (Minor request : requestDatabase.getMinors())
            write(path + "minor/" + request.getRequestNumber() + ".json" , request);
        for (Dorm request : requestDatabase.getDorms())
            write(path + "dorm/" + request.getRequestNumber() + ".json", request);
        for (Cancel request : requestDatabase.getCancels())
            write(path + "cancel/" + request.getRequestNumber() + ".json" , request);
        for (Defence request : requestDatabase.getDefences())
            write(path + "defence/" + request.getRequestNumber() + ".json", request);
    }

    public void saveChat() throws IOException{
        String path = this.path + "chats/";
        for (Chat c : chatDatabase.getChatList())
            write(path + c.getName() +".json" , c);
    }

    public void saveCourseware() throws IOException {
        String path = this.path + "coursewares/";
        synchronized (lessonDatabase.getCoursewares()) {
            for (Courseware c : lessonDatabase.getCoursewares())
                write(path + c.getLesson().getNumber() + ".json", c);
        }
    }

    public void saveSolution() throws IOException {
        String path = this.path + "solutions/";
        for (Solution s : lessonDatabase.getSolutions())
            write(path + s.getName() + ".json" , s);
    }

    public void saveProtest() throws IOException {
        String path = this.path + "protests/";
        for (Protest p : requestDatabase.getProtestList())
            write(path + p.getName() + ".json" , p);
    }

    public void saveChooseLesson() throws IOException {
        String path = this.path + "chooselesson/";
        ChooseLessonController chooseLesson = lessonDatabase.getChooseLessonController();
        if (chooseLesson != null)
            write(path + ChooseLessonController.getTerm() + ".json" , chooseLesson);
    }

    public void write(String path , Object object) throws IOException {
        FileWriter fileWriter = new FileWriter(path , false);
        fileWriter.write(gson.toJson(object));
        fileWriter.close();
    }

    public void loadDatabase(){
        try {
            collegeLoader();
            lessonLoader();
            userLoader();
            requestLoader();
            chatLoader();
            coursewareLoader();
            solutionLoader();
            protestLoader();
            chooseLessonLoader();
        }catch (IOException ignored){}
    }

    public void requestLoader() throws IOException {
        String path = this.path + "/requests";
        //recommendation
        List<File> recommendationFile = getFiles(path + "/recommendation");
        for (File file : recommendationFile){
            Recommendation recommendation = gson.fromJson(getFileStr(file) , Recommendation.class);
            Recommendation.codeUpdate();
            requestDatabase.addRequest(recommendation);
        }
        //busy studying
        List<File> bustStudyingFile = getFiles(path + "/busystudying");
        for (File file : bustStudyingFile){
            BusyStudying busyStudying = gson.fromJson(getFileStr(file) , BusyStudying.class);
            Recommendation.codeUpdate();
            requestDatabase.addRequest(busyStudying);
        }
        //dorm
        List<File> dormFile = getFiles(path + "/dorm");
        for (File file : dormFile){
            Dorm dorm = gson.fromJson(getFileStr(file) , Dorm.class);
            Recommendation.codeUpdate();
            requestDatabase.addRequest(dorm);
        }
        //minor
        List<File> minorFile = getFiles(path + "/minor");
        for (File file : minorFile){
            Minor minor = gson.fromJson(getFileStr(file) , Minor.class);
            Recommendation.codeUpdate();
            requestDatabase.addRequest(minor);
        }
        //cancel
        List<File> cancelFile = getFiles(path + "/cancel");
        for (File file : cancelFile){
            Cancel cancel = gson.fromJson(getFileStr(file) , Cancel.class);
            Recommendation.codeUpdate();
            requestDatabase.addRequest(cancel);
        }
        //defence
        List<File> defenceFile = getFiles(path + "/defence");
        for (File file : defenceFile){
            Defence defence = gson.fromJson(getFileStr(file) , Defence.class);
            Recommendation.codeUpdate();
            requestDatabase.addRequest(defence);
        }

    }

    public void chatLoader() throws IOException {
        String path = this.path + "/chats";
        List<File> chatFiles = getFiles(path);
        for (File file : chatFiles){
            Chat chat = gson.fromJson(getFileStr(file) , Chat.class);
            chatDatabase.addChat(chat);
        }
    }

    public void collegeLoader() throws IOException {
        List<File> collegeFiles = getFiles(path + "/colleges");
        for (File file : collegeFiles){
            College college = gson.fromJson(getFileStr(file) , College.class);
            collegeDatabase.addCollege(college);
        }
    }

    public void lessonLoader() throws IOException {
        List<File> lessonFile = getFiles(path + "/lessons");
        for (File file : lessonFile){
            Lesson lesson = gson.fromJson(getFileStr(file) , Lesson.class);
            Lesson.codeUpdate();
            lessonDatabase.addLesson(lesson);
        }
    }

    public void coursewareLoader() throws IOException {
        List<File> coursewareFile = getFiles(path + "/coursewares");
        for (File file : coursewareFile){
            Courseware courseware = gson.fromJson(getFileStr(file) , Courseware.class);
            lessonDatabase.addCourseware(courseware);
        }
    }

    public void solutionLoader() throws IOException {
        List<File> solutionFile = getFiles(path + "/solutions");
        for (File file : solutionFile){
            Solution solution = gson.fromJson(getFileStr(file) , Solution.class);
            lessonDatabase.addSolution(solution);
        }
    }

    public void protestLoader() throws IOException {
        List<File> protestFile = getFiles(path + "/protests");
        for (File file : protestFile){
            Protest protest = gson.fromJson(getFileStr(file) , Protest.class);
            requestDatabase.addProtest(protest);
        }
    }

    public void chooseLessonLoader() throws IOException {
        List<File> chooseLessonFile = getFiles(path + "/chooselesson");
        for (File file : chooseLessonFile){
            ChooseLessonController chooseLesson = gson.fromJson(getFileStr(file) , ChooseLessonController.class);
            lessonDatabase.setChooseLessonController(chooseLesson);
            chooseLesson.setTimer();
        }
    }

    public void userLoader() throws IOException {
        //college boss
        List<File> bossFile = getFiles(path + "/users/professors/educationalassistants/collegebosses");
        for (File file : bossFile){
            CollegeBoss collegeBoss = gson.fromJson(getFileStr(file) , CollegeBoss.class);
            CollegeBoss.codeUpdate();
            userDatabase.addUser(collegeBoss);
        }
        //educational assistant
        List<File> assistantFile = getFiles(path + "/users/professors/educationalassistants");
        for (File file : assistantFile){
            EducationalAssistant educationalAssistant = gson.fromJson(getFileStr(file) , EducationalAssistant.class);
            EducationalAssistant.codeUpdate();
            userDatabase.addUser(educationalAssistant);
        }
        //professors
        List<File> professorFile = getFiles(path + "/users/professors");
        for (File file : professorFile){
            Professor professor = gson.fromJson(getFileStr(file) , Professor.class);
            Professor.codeUpdate();
            userDatabase.addUser(professor);
        }
        //students
        List<File> studentFile = getFiles(path + "/users/students");
        for (File file : studentFile){
            Student student = gson.fromJson(getFileStr(file) , Student.class);
            Student.codeUpdate();
            userDatabase.addUser(student);
        }

        //admins
        List<File> adminsFile = getFiles(path + "/users/admins");
        for (File file : adminsFile){
            AdminEdu adminEdu = gson.fromJson(getFileStr(file) , AdminEdu.class);
            AdminEdu.codeUpdate();
            userDatabase.addUser(adminEdu);
        }

        //mohseni
        List<File> mohseniFile = getFiles(path + "/users/mohseni");
        for (File file : mohseniFile){
            Mohseni mohseni = gson.fromJson(getFileStr(file) , Mohseni.class);
            userDatabase.addUser(mohseni);
        }
    }

    public List<File> getFiles(String path) throws IOException {
        return  Files.list(Paths.get(path))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .collect(Collectors.toList());
    }

    public String getFileStr(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        StringBuilder str = new StringBuilder();
        while (scanner.hasNext())
            str.append(scanner.nextLine());
        scanner.close();
        return str.toString();
    }

    public static void main(String[] args) throws IOException {
        College college1 = new College("Math");
        College college2 = new College("Physic");
        College college3 = new College("Ma`aref");

        Student student1 = new Student(college1, "aref", "1381", "Aref", "Najjar",
                "arefnemayandeh@gmail.com", "9130992452", "4421343424", Grade.BACHELOR,
                1400, EducationalStatus.STUDYING);
        Student student2 = new Student(college1, "elato", "1234", "Elahe", "Tohidi",
                "elaheTo@gmail.com", "9133995295", "234492023", Grade.BACHELOR,
                1400, EducationalStatus.STUDYING);
        Student student3 = new Student(college1, "1111", "0000", "Sadat", "Amiri",
                "amiri@outlook.com", "9134203949", "34424442989", Grade.DOCTORATE,
                1397, EducationalStatus.GRADUATING);
        Student student4 = new Student(college2, "xxx", "1234", "Aeirya", "mhmdi",
                "aeiryammdi@gmail.com", "9121234924", "0234253891", Grade.MASTER,
                1398, EducationalStatus.STUDYING);
        Student student5 = new Student(college2, "rain", "1111", "Baran", "Toyii",
                "barantyii@aoulook.com", "9132450232", "4432153255", Grade.BACHELOR,
                1398, EducationalStatus.STUDYING);
        Student student6 = new Student(college2, "s4", "s4", "Sina", "Amooyi",
                "sini@yahoo.com", "9103230223", "4444442323", Grade.MASTER,
                1399, EducationalStatus.GRADUATING);
        Student student7 = new Student(college3, "la79", "7979", "Lachin", "Naqash",
                "lachii@gmail.com", "9130983750", "4432221233", Grade.BACHELOR,
                1399, EducationalStatus.GRADUATING);
        Student student8 = new Student(college3 , "dab" , "dab" , "Diba" , "Hashem" ,
                "dabHash@sharif.edu" , "9134290392" , "002929283" , Grade.BACHELOR
                , 1399 , EducationalStatus.STUDYING);
        Student student9 = new Student(college3 , "shsh" , "shsh0" , "Matin" , "Jaani" ,
                "Matnshash@gmail.com" , "9234328728" , "0039382930" , Grade.BACHELOR ,
                1400 , EducationalStatus.STUDYING);

        Professor professor1 = new Professor(college1, "xyz", "xyz", "Khane", "Dani",
                "dafsdf@gmail.dfo", "9105035060", "2134554632", "12", ProfessorDegree.ASSISTANT_PROFESSOR);
        Professor professor2 = new Professor(college1, "mt", "mt", "Mojtaba", "Tefagh",
                "MTfgh@gmail.dfo", "91943897060", "4432118492", "31B", ProfessorDegree.FULL_PROFESSOR);
        Professor professor3 = new Professor(college2, "ab", "a", "Mani", "Rezayi",
                "dafsdf@gmail.com", "91050523220", "2134254632", "14", ProfessorDegree.ASSOCIATE_PROFESSOR);
        Professor professor4 = new Professor(college2, "abc", "abc", "Sohrab", "Rahvar",
                "alliii@gmail.dfo", "9135240762", "443012579", "16", ProfessorDegree.FULL_PROFESSOR);
        Professor professor5 = new Professor(college3 , "1356" , "1356" , "Movahhed" , "Nejad" ,
                "movahedsdo@dds.co" , "9123032390" , "444322901" , "11C" , ProfessorDegree.FULL_PROFESSOR);
        Professor professor6 = new Professor(college3 , "ch" , "111" , "Mehraneh" , "Chatrchi" ,
                "mhrch@asd.abs", "9219283920" ,"2293382939" , "10A" , ProfessorDegree.ASSOCIATE_PROFESSOR );

        EducationalAssistant assistant1 = new EducationalAssistant(college1, "f19", "1991", "Mohamad", "Khazzayi",
                "dakhazzd@yahoo.dfo", "9190035060", "313015432", "20A", ProfessorDegree.ASSISTANT_PROFESSOR);
        EducationalAssistant assistant2 = new EducationalAssistant(college2, "bah", "2020", "Bahman", "Mahi",
                "bahiMEj@gmail.com", "914729379", "4429985302", "33A", ProfessorDegree.FULL_PROFESSOR);
        EducationalAssistant assistant3 = new EducationalAssistant(college3 , "m1" , "abcd" , "Akbar" , "Mahmudi",
                "maahmod1@sharif.edu" , "9145239893" , "003213421" , "19" , ProfessorDegree.ASSISTANT_PROFESSOR);

        CollegeBoss boss1 = new CollegeBoss(college1, "boss1", "boss1", "Alireza", "Qane",
                "palisbos@gmail.com", "9134899289", "4429923027", "20B", ProfessorDegree.ASSOCIATE_PROFESSOR);
        CollegeBoss boss2 = new CollegeBoss(college2, "boss2", "boss2", "Alireza", "Momen",
                "monnojda@gmail.com", "9909099289", "4223223027", "37", ProfessorDegree.FULL_PROFESSOR);
        CollegeBoss boss3 = new CollegeBoss(college3 , "boss3" , "boss3" , "Rehyaneh" , "Qazi" ,
                "Rey@sharif.edu" , "9092389467" , "332903820" , "18" , ProfessorDegree.ASSOCIATE_PROFESSOR);

        Mohseni mohseni = new Mohseni(null , "mohseni" , "mohseni" , "Ali Akbar" , "Mohseni" ,
                "mohseni@sharif.edu" , "9130995621" , "0022445685");
        AdminEdu adminEdu = new AdminEdu(null , "1" , "1" , "Admin" , "" ,
                "adminedu@sharif.edu" , null , null);

        List<DayOfWeek> dayOfWeeks = new ArrayList<>(List.of(DayOfWeek.SATURDAY , DayOfWeek.MONDAY));
        Lesson lesson1 = new Lesson("Riazi1", college1, 4, Grade.BACHELOR, professor1,
                new LessonTime(dayOfWeeks, 8, 10, 10, 11, 8) ,400 , 14002);
        dayOfWeeks = new ArrayList<>(List.of(DayOfWeek.WEDNESDAY));
        Lesson lesson2 = new Lesson("BP", college1, 4, Grade.BACHELOR, professor2,
                new LessonTime(dayOfWeeks, 11, 13, 30, 10, 10), 100 , 14000);
        dayOfWeeks = new ArrayList<>(List.of(DayOfWeek.SATURDAY , DayOfWeek.MONDAY));
        Lesson lesson3 = new Lesson("AP", college1, 8, Grade.BACHELOR, professor2,
                new LessonTime(dayOfWeeks, 8, 10, 5, 11, 8), 120 , 14001);
        dayOfWeeks = new ArrayList<>(List.of(DayOfWeek.SATURDAY , DayOfWeek.SUNDAY));
        Lesson lesson4 = new Lesson("Gosaste", college1, 3, Grade.DOCTORATE, assistant1,
                new LessonTime(dayOfWeeks, 9, 10, 6, 11, 10), 80 , 14002);
        dayOfWeeks = new ArrayList<>(List.of(DayOfWeek.SUNDAY , DayOfWeek.THURSDAY));
        Lesson lesson5 = new Lesson("Physic1", college2, 3, Grade.BACHELOR, professor3,
                new LessonTime(dayOfWeeks, 9, 11, 10, 11, 12), 400 , 14001);
        dayOfWeeks = new ArrayList<>(List.of(DayOfWeek.SATURDAY , DayOfWeek.MONDAY));
        Lesson lesson6 = new Lesson("Physic2", college2, 4, Grade.MASTER, professor3,
                new LessonTime(dayOfWeeks, 8, 10, 10, 11, 12), 200 , 14002);
        dayOfWeeks = new ArrayList<>(List.of(DayOfWeek.WEDNESDAY , DayOfWeek.TUESDAY));
        Lesson lesson7 = new Lesson("Az Physic", college2, 1, Grade.BACHELOR, professor4,
                new LessonTime(dayOfWeeks, 16, 17, 10, 11, 12), 20 , 14002);
        dayOfWeeks = new ArrayList<>(List.of(DayOfWeek.TUESDAY));
        Lesson lesson8 = new Lesson("Tafsir" , college3 , 2 , Grade.BACHELOR , professor5 ,
                new LessonTime(dayOfWeeks , 13 , 15 , 21 , 11 , 15) , 80 , 14001);
        dayOfWeeks = new ArrayList<>(List.of(DayOfWeek.SATURDAY));
        Lesson lesson9 = new Lesson("Aaein Zendegi" , college3 , 2 , Grade.BACHELOR , professor6 ,
                new LessonTime(dayOfWeeks , 14 , 16 , 20 , 11 , 15) , 80 , 14002);
        dayOfWeeks = new ArrayList<>(List.of(DayOfWeek.WEDNESDAY));
        Lesson lesson10 = new Lesson("Andishe" , college3 , 2 , Grade.MASTER , professor6 ,
                new LessonTime(dayOfWeeks , 15 , 17 , 20 , 11 , 19)  , 40 , 14002);

        lesson3.addPrerequisites(lesson2.getNumber());
        lesson4.addRequirements(lesson1.getNumber());
        lesson6.addPrerequisites(lesson5.getNumber());

        student1.setSupervisor(professor1);
        student2.setSupervisor(professor2);
        student3.setSupervisor(professor2);
        student4.setSupervisor(professor3);
        student5.setSupervisor(professor4);
        student6.setSupervisor(professor4);
        student7.setSupervisor(professor5);
        student8.setSupervisor(professor5);
        student9.setSupervisor(professor6);

        Chat chat1 = new Chat(student1 , student2);
        Chat chat2 = new Chat(student2 , student3);
        Chat chat3 = new Chat(student3 , student1);
        Chat chat4 = new Chat(student4 , student5);
        Chat chat5 = new Chat(student5 , student6);
        Chat chat6 = new Chat(student6 , student4);
        Chat chat7 = new Chat(student7 , student8);
        Chat chat8 = new Chat(student8 , student9);
        Chat chat9 = new Chat(student9 , student7);
        Chat chat10 = new Chat(student1 , student4);
        Chat chat11 = new Chat(student2 , professor2);
        Chat chat12 = new Chat(student3 , professor1);
        Chat chat13 = new Chat(mohseni , student1);
        Chat chat14 = new Chat(mohseni , student2);
        Chat chat15 = new Chat(mohseni , student3);
        Chat chat16 = new Chat(mohseni , student4);
        Chat chat17 = new Chat(mohseni , student6);
        String message = "Hello to Students of MATH college!";
        chat13.addMessage(new ChatMessage(mohseni , message , new Time()));
        chat14.addMessage(new ChatMessage(mohseni , message , new Time()));
        chat15.addMessage(new ChatMessage(mohseni , message , new Time()));
        message = "Hello to Master students from Mohseni!";
        chat16.addMessage(new ChatMessage(mohseni , message , new Time()));
        chat17.addMessage(new ChatMessage(mohseni , message , new Time()));

        LocalDateTime now = LocalDateTime.now();
        Courseware courseware1 = new Courseware(lesson3.getNumber());
        courseware1.addTA(student2);
        Exercise exercise1 = new Exercise("Mini Shogi" , "The third series of delivered exercises has been uploaded.\n" +
                "More details of the exercise are included in the file." , new Time() ,
                new Time(now.plusDays(7)) , new Time(now.plusDays(7)) , UploadType.TEXT);
        exercise1.setMedia(new Media("Shogi" , Files.readAllBytes(Path.of("C:/Users/Almas/OneDrive/Desktop/AP/files/mini shogi.pdf")) , MediaType.PDF));
        Exercise exercise2 = new Exercise("Coup" , "The coup game is a board game.\nYou can learn the" +
                " rules and how to play the game by referring to the file." , new Time(now.plusMonths(1)) ,
                new Time(now.plusMonths(1).plusDays(7)) , new Time(now.plusMonths(1).plusDays(7)) , UploadType.MEDIA);
        exercise2.setMedia(new Media("Coup" , Files.readAllBytes(Path.of("C:/Users/Almas/OneDrive/Desktop/AP/files/coup.pdf")) , MediaType.PDF));
        courseware1.addExercise(exercise1);
        courseware1.addExercise(exercise2);

        Courseware courseware2 = new Courseware(lesson5.getNumber());
        courseware2.addTA(student6);
        EducationalMaterial material1 = new EducationalMaterial("Midterm" , "Here we have put the answers to midterm questions.");
        material1.addFile(new Media("Solution" , Files.readAllBytes(Path.of("C:/Users/Almas/OneDrive/Desktop/AP/files/midterm ph.pdf")) , MediaType.PDF));
        String text = "Important, dear students. This file is only for testing the programming project and does not contain" +
                " any useful content. thanks for your choice. Do not be tired and take care of God.";
        material1.addFile(new Media("Warning" , text.getBytes(StandardCharsets.UTF_8) , MediaType.TEXT));
        material1.addFile(new Media("Scores" , Files.readAllBytes(Path.of("C:/Users/Almas/OneDrive/Desktop/AP/files/nomerat.pdf")) , MediaType.PDF));
        EducationalMaterial material2 = new EducationalMaterial("Summery" , "Lesson summaries can be found here");
        material2.addFile(new Media("Professor tips" , Files.readAllBytes(Path.of("C:/Users/Almas/OneDrive/Desktop/AP/files/voice.m4a")) , MediaType.VOICE));
        text = "This is a list of physics book exercises that are good to think about and solve. Note that delivery is not required:\n" +
                "3, 53, 13, 54, 23, 54, 2, 356, and 2";
        material2.addFile(new Media("Suggested exercises" , text.getBytes(StandardCharsets.UTF_8) , MediaType.TEXT));
        material2.addFile(new Media("Board" , Files.readAllBytes(Path.of("C:/Users/Almas/OneDrive/Desktop/AP/files/image for ap.png")) , MediaType.IMAGE));
        courseware2.addEducationalMaterial(material1);
        courseware2.addEducationalMaterial(material2);

        Courseware courseware3 = new Courseware(lesson8.getNumber());
        LocalDateTime time = now.minusDays(7).plusHours(12);
        Exercise exercise3 = new Exercise("Lesson2" , "Please answer the questions from the second lesson appropriately." ,
                new Time(time) , new Time(time.plusDays(14)) , new Time(time.plusDays(12)) , UploadType.MEDIA);
        exercise3.setMedia(new Media("Lesson2" , Files.readAllBytes(Path.of("C:/Users/Almas/OneDrive/Desktop/AP/files/tafsir96.pdf")) , MediaType.PDF));
        time = now.plusDays(2).withHour(12).withMinute(0).withSecond(0);
        Exercise exercise4 = new Exercise("Say Summery" , "Please record a summary of the past meetings and upload it here."  ,
                new Time(time) , new Time(time.plusMonths(1)) , new Time(time.plusMonths(1).minusHours(12)) , UploadType.MEDIA);
        Exercise exercise5 = new Exercise("Interpretation" , "Write your interpretation of verse 204 of Surah Al-A'raf in several paragraphs." ,
               new Time(now.withHour(18)) , new Time(now.plusDays(4).withHour(23)) , new Time(now.plusDays(4).withHour(11)) , UploadType.TEXT );
        EducationalMaterial material3 = new EducationalMaterial("Sample question" , "");
        material3.addFile(new Media("Nemone soal" , Files.readAllBytes(Path.of("C:/Users/Almas/OneDrive/Desktop/AP/files/Nemoone soal tafsir.pdf")) , MediaType.PDF));
        EducationalMaterial material4 = new EducationalMaterial("Educational film" , "This film shows how the sound and beautiful" +
                " texts of the Quran affect people even if they do not know Arabic.");
        material4.addFile(new Media("Movie" , Files.readAllBytes(Path.of("C:/Users/Almas/OneDrive/Desktop/AP/files/nokte tafsir.pdf")) , MediaType.VIDEO));
        courseware3.addExercise(exercise3);
        courseware3.addExercise(exercise4);
        courseware3.addExercise(exercise5);
        courseware3.addEducationalMaterial(material3);
        courseware3.addEducationalMaterial(material4);

        student1.addLesson(lesson3);
        student5.addLesson(lesson5);

        collegeDatabase.addCollege(college1);
        collegeDatabase.addCollege(college2);
        collegeDatabase.addCollege(college3);

        userDatabase.addUser(student1);
        userDatabase.addUser(student2);
        userDatabase.addUser(student3);
        userDatabase.addUser(student4);
        userDatabase.addUser(student5);
        userDatabase.addUser(student6);
        userDatabase.addUser(student7);
        userDatabase.addUser(student8);
        userDatabase.addUser(student9);
        userDatabase.addUser(professor1);
        userDatabase.addUser(professor2);
        userDatabase.addUser(professor3);
        userDatabase.addUser(professor4);
        userDatabase.addUser(professor5);
        userDatabase.addUser(professor6);
        userDatabase.addUser(assistant1);
        userDatabase.addUser(assistant2);
        userDatabase.addUser(assistant3);
        userDatabase.addUser(boss1);
        userDatabase.addUser(boss2);
        userDatabase.addUser(boss3);
        userDatabase.addUser(mohseni);
        userDatabase.addUser(adminEdu);

        lessonDatabase.addLesson(lesson1);
        lessonDatabase.addLesson(lesson2);
        lessonDatabase.addLesson(lesson3);
        lessonDatabase.addLesson(lesson4);
        lessonDatabase.addLesson(lesson5);
        lessonDatabase.addLesson(lesson6);
        lessonDatabase.addLesson(lesson7);
        lessonDatabase.addLesson(lesson8);
        lessonDatabase.addLesson(lesson9);
        lessonDatabase.addLesson(lesson10);

        chatDatabase.addChat(chat1);
        chatDatabase.addChat(chat2);
        chatDatabase.addChat(chat3);
        chatDatabase.addChat(chat4);
        chatDatabase.addChat(chat5);
        chatDatabase.addChat(chat6);
        chatDatabase.addChat(chat7);
        chatDatabase.addChat(chat8);
        chatDatabase.addChat(chat9);
        chatDatabase.addChat(chat10);
        chatDatabase.addChat(chat11);
        chatDatabase.addChat(chat12);
        chatDatabase.addChat(chat13);
        chatDatabase.addChat(chat14);
        chatDatabase.addChat(chat15);
        chatDatabase.addChat(chat16);
        chatDatabase.addChat(chat17);

        lessonDatabase.addCourseware(courseware1);
        lessonDatabase.addCourseware(courseware2);
        lessonDatabase.addCourseware(courseware3);

        new JsonManager().saveDatabase();
    }
}
