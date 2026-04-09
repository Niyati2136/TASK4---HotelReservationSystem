package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Booking {

    private String bookingId;
    private String roomId;
    private String customerName;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private double totalPrice;
    private boolean paid;
    private boolean active;

    DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE;

    public Booking(String bookingId, String roomId, String customerName,
                   LocalDate checkIn, LocalDate checkOut, double totalPrice,
                   boolean paid, boolean active) {

        this.bookingId = bookingId;
        this.roomId = roomId;
        this.customerName = customerName;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.totalPrice = totalPrice;
        this.paid = paid;
        this.active = active;
    }

    public String getBookingId() { return bookingId; }
    public String getRoomId() { return roomId; }
    public boolean isPaid() { return paid; }
    public boolean isActive() { return active; }
    public void setPaid(boolean paid) { this.paid = paid; }
    public void setActive(boolean active) { this.active = active; }
    public LocalDate getCheckIn(){ return checkIn; }
    public LocalDate getCheckOut(){ return checkOut; }
    public double getTotalPrice(){ return totalPrice; }

    @Override
    public String toString() {
        return String.format(
                "%s | Room: %s | %s -> %s | %s | ₹%.2f | Paid:%s | Active:%s",
                bookingId, roomId, checkIn, checkOut, customerName,
                totalPrice, paid, active
        );
    }

    public String toCSV() {
        return String.join(",",
                bookingId, roomId, customerName,
                checkIn.format(fmt), checkOut.format(fmt),
                String.valueOf(totalPrice),
                String.valueOf(paid),
                String.valueOf(active)
        );
    }

    public static Booking fromCSV(String line) {
        String[] p = line.split(",");
        DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE;

        return new Booking(
                p[0], p[1], p[2],
                LocalDate.parse(p[3], fmt),
                LocalDate.parse(p[4], fmt),
                Double.parseDouble(p[5]),
                Boolean.parseBoolean(p[6]),
                Boolean.parseBoolean(p[7])
        );
    }
}
