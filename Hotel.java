package service;

import model.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Hotel {

    private List<Room> rooms;
    public List<Booking> bookings;

    public Hotel(List<Room> rooms, List<Booking> bookings) {
        this.rooms = rooms;
        this.bookings = bookings;
    }

    public List<Room> searchAvailable(RoomType type, LocalDate from, LocalDate to) {
        return rooms.stream()
                .filter(r -> r.getType() == type)
                .filter(r -> isRoomAvailable(r.getId(), from, to))
                .collect(Collectors.toList());
    }

    public boolean isRoomAvailable(String roomId, LocalDate from, LocalDate to) {
        for (Booking b : bookings) {
            if (!b.isActive()) continue;
            if (!b.getRoomId().equals(roomId)) continue;

            if (b.getCheckIn().isBefore(to) && from.isBefore(b.getCheckOut())) {
                return false;
            }
        }
        return true;
    }

    public String makeBooking(String roomId, String name, LocalDate from, LocalDate to) {
        Optional<Room> ro = rooms.stream().filter(r -> r.getId().equals(roomId)).findFirst();
        if (ro.isEmpty()) return null;

        if (!isRoomAvailable(roomId, from, to)) return null;

        long nights = java.time.temporal.ChronoUnit.DAYS.between(from, to);
        if (nights <= 0) return null;

        double total = nights * ro.get().getPricePerNight();

        String bookingId = "B" + String.format("%04d", bookings.size() + 1);
        Booking b = new Booking(bookingId, roomId, name, from, to, total, false, true);

        bookings.add(b);

        try {
            Storage.saveBookings(bookings);
        } catch (IOException e) {
            System.out.println("Error saving booking data.");
        }

        return bookingId;
    }

    public boolean cancelBooking(String bookingId) {
        for (Booking b : bookings) {
            if (b.getBookingId().equals(bookingId) && b.isActive()) {
                b.setActive(false);
                try { Storage.saveBookings(bookings); } catch (Exception ignored) {}
                return true;
            }
        }
        return false;
    }

    public Booking findBooking(String id) {
        for (Booking b : bookings)
            if (b.getBookingId().equals(id))
                return b;
        return null;
    }
}
