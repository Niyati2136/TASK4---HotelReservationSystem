package service;

import model.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class Storage {

    public static final String ROOMS_FILE = "data/rooms.csv";
    public static final String BOOKINGS_FILE = "data/bookings.csv";

    public static void ensureFilesExist() throws IOException {
        if (!Files.exists(Path.of("data"))) {
            Files.createDirectory(Path.of("data"));
        }

        if (!Files.exists(Path.of(ROOMS_FILE))) {
            List<String> seed = Arrays.asList(
                    "R101,STANDARD,2000",
                    "R102,STANDARD,2000",
                    "R201,DELUXE,3500",
                    "R202,DELUXE,3500",
                    "R301,SUITE,6000"
            );
            Files.write(Path.of(ROOMS_FILE), seed);
        }

        if (!Files.exists(Path.of(BOOKINGS_FILE))) {
            Files.createFile(Path.of(BOOKINGS_FILE));
        }
    }

    public static List<Room> loadRooms() throws IOException {
        ensureFilesExist();
        return Files.readAllLines(Path.of(ROOMS_FILE))
                .stream()
                .filter(s -> !s.isBlank())
                .map(Room::fromCSV)
                .collect(Collectors.toList());
    }

    public static List<Booking> loadBookings() throws IOException {
        ensureFilesExist();
        return Files.readAllLines(Path.of(BOOKINGS_FILE))
                .stream()
                .filter(s -> !s.isBlank())
                .map(Booking::fromCSV)
                .collect(Collectors.toList());
    }

    public static void saveBookings(List<Booking> bookings) throws IOException {
        List<String> lines = bookings.stream()
                .map(Booking::toCSV)
                .toList();
        Files.write(Path.of(BOOKINGS_FILE), lines);
    }
}
