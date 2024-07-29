package ticketbooking.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ticketbooking.models.Train;
import ticketbooking.models.User;
import ticketbooking.utils.UserServiceUtil;

public class UserService {
    private User user;

    private List<User> userList;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String USERSDB_PATH = "app/src/main/java/ticketbooking/db/users.json";

    public UserService(User usr) throws IOException {
        this.user = usr;
        File users = new File(USERSDB_PATH);
        userList = objectMapper.readValue(users, new TypeReference<List<User>>() {});
    }

    public UserService() throws Exception {
        loadUserListFromFile();
    }

    private void loadUserListFromFile() throws Exception {
        userList = objectMapper.readValue(new File(USERSDB_PATH), new TypeReference<List<User>>() {});
    }

    private void saveUserToDB() throws Exception {
        File usersFile = new File(USERSDB_PATH);
        objectMapper.writeValue(usersFile, userList);
    }

    public Boolean loginUser() {
        Optional<User> userExists = userList.stream().filter(usr -> {
            return usr.getName().equalsIgnoreCase(user.getName()) && UserServiceUtil.checkPassword(usr.getHashedPassword(), user.getHashedPassword());
        }).findFirst();
        return userExists.isPresent();
    }

    public Boolean signupUser(User usr) {
        try {
            userList.add(usr);
            saveUserToDB();
            return Boolean.TRUE;
        } catch(Exception e) {
            return Boolean.FALSE;
        }
    }

    public void fetchBookings() {
        Optional<User> userFetched = userList.stream().filter(usr -> {
            return usr.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), usr.getHashedPassword());
        }).findFirst();
        if(userFetched.isPresent()) {
            userFetched.get().printTickets();
        } else {
            System.out.println("No Bookings Found...");
        }
    }

    public Boolean cancelTicket(String ticketId) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Ticket to Cancel: ");
        ticketId = sc.next();

        if(ticketId==null || ticketId.isEmpty()) {
            System.out.println("Ticket ID cannot be null or empty");
            return Boolean.FALSE;
        }
        String finalTicketId1 = ticketId; // as String are immutable
        boolean removed = user.getTicketsBooked().removeIf(t -> t.getTicketId().equals(finalTicketId1));
        String finalTicketId = ticketId;
        user.getTicketsBooked().removeIf(t -> t.getTicketId().equals(finalTicketId));
        if(removed) {
            System.out.println("Ticket with ID "+ticketId+" has been removed");
            return Boolean.TRUE;
        } else {
            System.out.println("No Ticket found with ID "+ticketId);
            return Boolean.FALSE;
        }
    }

    public List<Train> getTrains(String src, String dest) {
        try {
            TrainService trainService = new TrainService();
            return trainService.searchTrans(src, dest);
        } catch(Exception e) {
            return new ArrayList<>();
        }
    }

    public List<List<Integer>> fetchSeats(Train train) {
        return train.getSeats();
    }

    public Boolean bookTrainSeat(Train train, int row, int seat) {
        try {
            TrainService trainService = new TrainService();
            List<List<Integer>> seats = train.getSeats();
            if(row>=0 && row<seats.size() && seat>=0 && seat<seats.get(row).size()) {
                if(seats.get(row).get(seat)==0) {
                    seats.get(row).set(seat, 1);
                    train.setSeats(seats);
                    trainService.addTrain(train);
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch(Exception e) {
            return Boolean.FALSE;
        }
    }
}