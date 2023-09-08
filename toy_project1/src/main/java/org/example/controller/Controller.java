package org.example.controller;

import org.example.model.Itinerary;
import org.example.model.Trip;
import org.example.repository.TripDao;
import org.example.service.TripExplorerService;
import org.example.view.Viewer;
import org.example.service.TripRecordService;

import java.util.List;

import static java.lang.System.exit;

public class Controller {
    private static Controller instance;
    private final Viewer viewer = new Viewer();
    private final TripDao tripDao = new TripDao();
    private final int NOT_APPROPRIATE_NUMBER = -999999999;

    private final TripExplorerService searchTripService = new TripExplorerService(tripDao);
    private final TripRecordService saveTripService = new TripRecordService();

    private Controller() {
    }

    public static synchronized Controller getInstance() {
        if (instance == null)
            instance = new Controller();
        return instance;
    }

    public void launch() {
        viewer.printMenuOption();
        int menuOptionNumber;
        while (true) {
            menuOptionNumber = ParseStringToInteger(viewer.receiveMenuSelection());
            if (menuOptionNumber != -NOT_APPROPRIATE_NUMBER && checkValidOptionNumberRange(menuOptionNumber, 3)) { // TODO: magic number
                break;
            }
        }
        switch (menuOptionNumber) {
            case 1: // TODO: magic number
                recordTrip();
                break;

            case 2: // TODO: magic number
                printTrip();
                break;

            case 3: // TODO: magic number
                terminate();
        }
        launch();
    }

    private void recordTrip() {
        Trip trip = new Trip();
        trip = viewer.returnTripInformationFromConsole(trip);
        recordItineraries(trip);
        saveTripService.setIdToTrip(trip);
        saveTripService.saveJsonToFile(trip);
        saveTripService.saveCsvToFile(trip);

        int numberOfTripRecordSelection;
        while (true) {
            numberOfTripRecordSelection = ParseStringToInteger(viewer.receiveTripSelection());
            if (numberOfTripRecordSelection != NOT_APPROPRIATE_NUMBER &&
                    checkValidOptionNumberRange(numberOfTripRecordSelection, 2)) { // TODO: magic number
                break;
            }
        }
        if (numberOfTripRecordSelection == 2) { // TODO: magic number
            return;
        }
        recordTrip();
    }

    private void printTrip() {
        int numberOfFileType;
        while (true) {
            numberOfFileType = ParseStringToInteger(viewer.receiveFileTypeSelection());
            if (numberOfFileType != NOT_APPROPRIATE_NUMBER && checkValidOptionNumberRange(numberOfFileType, 2)) { // TODO: magic number
                break;
            }
        }

        List<Trip> trips;
        if (numberOfFileType == 1) { // TODO: magic number
            trips = searchTripService.getTripsAs(".json"); // TODO: magic number
        } else {
            trips = searchTripService.getTripsAs(".csv"); // TODO: magic number
        }

        if(trips.isEmpty()){
            return;
        }

        viewer.printTripsNameAndId(trips);


        int selectedTripId;
        while (true) {
            selectedTripId = ParseStringToInteger(viewer.receiveTripId());
            if (selectedTripId != NOT_APPROPRIATE_NUMBER) {
                break;
            }
        }
        Trip foundTrip = searchTripService.getTripById(selectedTripId);
        viewer.printTripDetailInformation(foundTrip);
    }

    private Trip recordItineraries(Trip trip) {
        int maximumOptionNumber = 2;
        List<Itinerary> itineraries = trip.getItineraries();

        while (true) {
            Itinerary itinerary = viewer.returnItineraryInformationFromConsole(new Itinerary());
            itineraries.add(itinerary); // TODO; Role 1

            int ItineraryOptionNumber;
            while (true) { // TODO: need to make method for clarification e.g., checkValidItineraryOptionNumber
                ItineraryOptionNumber = ParseStringToInteger(viewer.receiveItinerarySelection());
                if (ItineraryOptionNumber != NOT_APPROPRIATE_NUMBER && checkValidOptionNumberRange(ItineraryOptionNumber, maximumOptionNumber)) { // TODO: magic number
                    break;
                }
            }
            if (ItineraryOptionNumber == maximumOptionNumber) { // TODO: magic number
                break;
            }
        }
        trip.setItineraries(itineraries);
        return trip;
    }

    private void terminate() {
        viewer.printExitMessage();
        exit(0); // TODO: magic number
    }

    private int ParseStringToInteger(String num) {
        try { // TODO: Exception, if-else
            int integerNum = Integer.parseInt(num);
            return integerNum;
        } catch (NumberFormatException e) {
            System.out.println("정수가 아닙니다. 다시 입력해주세요."); // TODO: magic number
            return NOT_APPROPRIATE_NUMBER;
        }
    }

    private boolean checkValidOptionNumberRange(int optionNumber, int maximumOptionsNumber) {
        int minimumOptionValue = 1;
        if (optionNumber < minimumOptionValue || optionNumber > maximumOptionsNumber) { // TODO: magic number & Exception
            System.out.println("리스트에 있는 번호를 입력해주세요."); // TODO: magic number
            return false;
        }
        return true;
    }
}
