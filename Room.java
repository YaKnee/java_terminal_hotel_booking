class Room{
    private String type, name;
    private int cost, room, nights, number;
    private double bill;
    private boolean isReserved = false;
    static final int MAX_SINGLE_COST = 100;
    static final int MIN_SINGLE_COST = 90;
    static final int MAX_DOUBLE_COST = 150;
    static final int MIN_DOUBLE_COST = 140;

    public Room() {

    }

    public Room(int roomNum, String roomType, int nights, int costPerNight, double totalPrice, String reservationName,
            int reservationNumber) {
        this.room = roomNum;
        this.cost = costPerNight;
        this.type = roomType;
        this.nights = nights;
        this.bill = totalPrice;
        this.name = reservationName;
        this.number = reservationNumber;
        this.isReserved = false;
    }

        public Room(int roomNum, String roomType, int costPerNight, String name) {
        this.room = roomNum;
        this.type = roomType;
        this.cost = costPerNight;
        this.name = name;
        this.isReserved = false;
    }

    public Room(int roomNum, String roomType) {
        this.room = roomNum;
        this.type = roomType;
        if (roomNum >= 1 && roomNum <= 50) {
            this.cost = MAX_SINGLE_COST;
        } else if (roomNum > 50 && roomNum <= 60) {
            this.cost = MIN_SINGLE_COST;
        } else if (roomNum > 60 && roomNum <= 90) {
            this.cost = MAX_DOUBLE_COST;
        } else {
            this.cost = MIN_DOUBLE_COST;
        }
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getNumber() {
        return room;
    }

    public int getNightsStay() {
        return nights;
    }

    public int getPricePerNight() {
        return cost;
    }

    public boolean setReserved() {
        this.isReserved = true;
        return isReserved;
    }

    public boolean getReserved() {
        return isReserved;
    }

    public double getTotalPrice() {
        return bill;
    }

    public int getReservationNumber() {
        return number;
    }

    public void displayRoomInfo() {
        System.out.println("\nRoom Number:\t\t\t#" + getNumber());
        System.out.println("Room type:\t\t\t" + getType());
        System.out.println("Price per night:\t\t" + getPricePerNight());
        System.out.println("Number of nights:\t\t" + getNightsStay());
        System.out.printf("Room price for %d nights:\t%.2f%n", getNightsStay(), getTotalPrice());
        System.out.println("Reservation number: \t\t" + getReservationNumber());
    }

    public String totalRoomsToTXT() {
        return String.format("Num: %d, Type: %s, Nights: %d, Cost: %d, Total: %.2f, Name: %s, Reservation: %d%n",
                getNumber(), getType(), getNightsStay(), getPricePerNight(), getTotalPrice(), getName(),
                getReservationNumber());
    }
}

