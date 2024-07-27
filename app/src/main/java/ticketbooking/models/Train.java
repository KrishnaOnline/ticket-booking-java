package ticketbooking.models;

import java.util.List;

public class Train {
    private String trainId;
    private String trainNo;
    private List<List<Integer>> seats;

    public Train() {};
    
    public Train(String trainId, String trainNo, List<List<Integer>> seats) {
        this.trainId = trainId;
        this.trainNo = trainNo;
        this.seats = seats;
    }

    public String getTrainId() {
        return trainId;
    }
    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public String getTrainNo() {
        return trainNo;
    }
    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }
    
    public List<List<Integer>> getSeats() {
        return seats;
    }
    public void setSeats(List<List<Integer>> seats) {
        this.seats = seats;
    }
}