package Main;

import model.*;
import service.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class HotelReservationSystem {

    static Scanner sc = new Scanner(System.in);
    static DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE;

    public static void main(String[] args) throws Exception {

        List<Room> rooms = Storage.loadRooms();
        List<Booking> bookings = Storage.loadBookings();
        Hotel hotel = new Hotel(rooms, bookings);

        System.out.println("Welcome to the Hotel Reservation System");

        boolean quit = false;

        while (!quit) {
            showMenu();
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1" -> searchRooms(hotel);
                case "2" -> makeBooking(hotel);
                case "3" -> cancelBooking(hotel);
                case "4" -> viewBookings(hotel);
                case "5" -> makePayment(hotel);
                case "0" -> quit = true;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    static void showMenu() {
        System.out.println("\nMenu:");
        System.out.println("1) Search Rooms");
        System.out.println("2) Make Booking");
        System.out.println("3) Cancel Booking");
        System.out.println("4) View Bookings");
        System.out.println("5) Make Payment");
        System.out.println("0) Exit");
        System.out.print("Choose: ");
    }

    static void searchRooms(Hotel hotel) {
        System.out.println("Room types: 1) Standard 2) Deluxe 3) Suite");
        String typeChoice = sc.nextLine().trim();

        RoomType type = switch (typeChoice) {
            case "1" -> RoomType.STANDARD;
            case "2" -> RoomType.DELUXE;
            default -> RoomType.SUITE;
        };

        try {
            System.out.print("Check-in (YYYY-MM-DD): ");
            LocalDate from = LocalDate.parse(sc.nextLine(), fmt);

            System.out.print("Check-out (YYYY-MM-DD): ");
            LocalDate to = LocalDate.parse(sc.nextLine(), fmt);

            List<Room> available = hotel.searchAvailable(type, from, to);

            if (available.isEmpty()) {
                System.out.println("No rooms available.");
            } else {
                available.forEach(r -> System.out.println(" - " + r));
            }
        } catch (Exception e) {
            System.out.println("Invalid date.");
        }
    }

    static void makeBooking(Hotel hotel) {
        try {
            System.out.print("Room ID: ");
            String roomId = sc.nextLine();

            System.out.print("Customer name: ");
            String name = sc.nextLine();

            System.out.print("Check-in (YYYY-MM-DD): ");
            LocalDate from = LocalDate.parse(sc.nextLine());

            System.out.print("Check-out (YYYY-MM-DD): ");
            LocalDate to = LocalDate.parse(sc.nextLine());

            String bid = hotel.makeBooking(roomId, name, from, to);

            if (bid != null)
                System.out.println("Booking successful. Booking ID: " + bid);
            else
                System.out.println("Booking failed. Invalid data or room unavailable.");

        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }

    static void cancelBooking(Hotel hotel) {
        System.out.print("Enter booking ID: ");
        String id = sc.nextLine();
        boolean ok = hotel.cancelBooking(id);
        System.out.println(ok ? "Booking canceled." : "Invalid booking ID.");
    }

    static void viewBookings(Hotel hotel) {
        if (hotel.bookings.isEmpty()) {
            System.out.println("No bookings yet.");
            return;
        }
        hotel.bookings.forEach(b -> System.out.println(" - " + b));
    }

    static void makePayment(Hotel hotel) {
        System.out.print("Enter booking ID: ");
        String id = sc.nextLine();

        Booking b = hotel.findBooking(id);

        if (b == null) {
            System.out.println("Booking not found.");
            return;
        }

        if (!b.isActive()) {
            System.out.println("Booking is canceled.");
            return;
        }

        if (b.isPaid()) {
            System.out.println("Already paid.");
            return;
        }

        boolean success = PaymentSimulator.processPayment(b.getTotalPrice());

        if (success) {
            b.setPaid(true);
            try { Storage.saveBookings(hotel.bookings); } catch (Exception ignored) {}
        }
    }
}
