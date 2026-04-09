package model;

public class Room {
    private String id;
    private RoomType type;
    private double pricePerNight;

    public Room(String id, RoomType type, double pricePerNight) {
        this.id = id;
        this.type = type;
        this.pricePerNight = pricePerNight;
    }

    public String getId() {
        return id;
    }

    public RoomType getType() {
        return type;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    @Override
    public String toString() {
        return id + " (" + type + ") - ₹" + pricePerNight + "/night";
    }

    public String toCSV() {
        return String.join(",", id, type.name(), String.valueOf(pricePerNight));
    }

    public static Room fromCSV(String line) {
        String[] p = line.split(",");
        return new Room(p[0], RoomType.valueOf(p[1]), Double.parseDouble(p[2]));
    }
}
