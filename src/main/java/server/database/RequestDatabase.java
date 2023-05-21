package server.database;

import server.model.educationalrequest.*;
import shared.model.educaionalrequests.Protest;

import java.util.ArrayList;
import java.util.List;

public class RequestDatabase {
    private final List<EducationalRequest> educationalRequestList = new ArrayList<>();
    private final List<Protest> protestList = new ArrayList<>();
    private final List<Recommendation> recommendations = new ArrayList<>();
    private final List<BusyStudying> busyStudyings = new ArrayList<>();
    private final List<Cancel> cancels = new ArrayList<>();
    private final List<Dorm> dorms = new ArrayList<>();
    private final List<Minor> minors = new ArrayList<>();
    private final List<Defence> defences = new ArrayList<>();

    private static final RequestDatabase instance = new RequestDatabase();
    public synchronized static RequestDatabase getInstance(){
        return instance;
    }

    public void addRequest(EducationalRequest educationalRequest){
        educationalRequestList.add(educationalRequest);
        if (educationalRequest instanceof Recommendation)
            recommendations.add((Recommendation) educationalRequest);
        if (educationalRequest instanceof BusyStudying)
            busyStudyings.add((BusyStudying) educationalRequest);
        if (educationalRequest instanceof Dorm)
            dorms.add((Dorm) educationalRequest);
        if (educationalRequest instanceof Cancel)
            cancels.add((Cancel) educationalRequest);
        if (educationalRequest instanceof Minor)
            minors.add((Minor) educationalRequest);
        if (educationalRequest instanceof Defence)
            defences.add((Defence) educationalRequest);
    }

    public void addProtest(Protest protest){
        protestList.add(protest);
    }

    public EducationalRequest getRequest(String requestNumber){
        for (EducationalRequest r : educationalRequestList)
            if (r.getRequestNumber().equals(requestNumber))
                return r;
        return null;
    }

    public Protest getProtest(long userCode , int lesson){
        for (Protest p : protestList)
            if (p.getStudentCode() == userCode && p.getLessonCode() == lesson)
                return p;
        return null;
    }

    public List<EducationalRequest> getRequestList() {
        return educationalRequestList;
    }

    public List<Recommendation> getRecommendations() {
        return recommendations;
    }

    public List<BusyStudying> getBusyStudyings() {
        return busyStudyings;
    }

    public List<Cancel> getCancels() {
        return cancels;
    }

    public List<Dorm> getDorms() {
        return dorms;
    }

    public List<Minor> getMinors() {
        return minors;
    }

    public List<Defence> getDefences() {
        return defences;
    }

    public List<Protest> getProtestList() {
        return protestList;
    }
}
