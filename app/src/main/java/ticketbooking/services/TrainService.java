package ticketbooking.services;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ticketbooking.models.Train;

public class TrainService {
    private List<Train> trainList;
    private ObjectMapper objectMapper = new ObjectMapper();
    private static final String TRAINDB_PATH = "../db/trains.json";

    public TrainService() throws Exception {
        File trains = new File(TRAINDB_PATH);
        trainList = objectMapper.readValue(trains, new TypeReference<List<Train>>() {});
    }

    public List<Train> searchTrans(String src, String dest) {
        return trainList.stream().filter(t -> validTrain(t, src, dest)).collect(Collectors.toList());
    }

    public void addTrain(Train newTrain) {
        Optional<Train> existingTrain = trainList.stream()
                                                .filter(t -> t.getTrainId().equalsIgnoreCase(newTrain.getTrainId()))
                                                .findFirst();
        if(existingTrain.isPresent()) {
            updateTrain(newTrain);
        } else {
            trainList.add(newTrain);
            saveTrainListToFile();
        }
    }

    public void updateTrain(Train updatedTrain) {
        OptionalInt index = IntStream.range(0, trainList.size())
                                        .filter(i -> trainList.get(i).getTrainId().equalsIgnoreCase(updatedTrain.getTrainId()))
                                        .findFirst();
        if(index.isPresent()) {
            trainList.set(index.getAsInt(), updatedTrain);
            saveTrainListToFile();
        } else {
            addTrain(updatedTrain);
        }
    }

    private void saveTrainListToFile() {
        try {
            objectMapper.writeValue(new File(TRAINDB_PATH), trainList);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validTrain(Train train, String src, String dest) {
        List<String> stationOrder = train.getStations();
        int srcIndex = stationOrder.indexOf(src.toLowerCase());
        int destIndex = stationOrder.indexOf(dest.toLowerCase());
        return srcIndex!=-1 && destIndex!=-1 && srcIndex<destIndex;
    }
}