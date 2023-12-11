class Room{
    private String type, name;
    private int cost, room, nights, number;
    private double bill;
    private boolean isReserved = false;

    public Room() {

    }
    //Used for updating room file
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
    //Used for generating room file
    public Room(int roomNum, String roomType, int costPerNight, String name) {
        this.room = roomNum;
        this.type = roomType;
        this.cost = costPerNight;
        this.name = name;
        this.isReserved = false;
    }
    //only used for Option 5
    public Room(int roomNum, String roomType, int costPerNight){
        this.room = roomNum;
        this.type = roomType;
        this.cost = costPerNight;
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

    public void setReserved(boolean reserved) {
        this.isReserved = reserved;
    }

    public boolean isReserved() {
        return isReserved;
    }

    public double getTotalPrice() {
        return bill;
    }
    public void setTotalPrice(double bill){
        this.bill = bill;
    }

    public int getReservationNumber() {
        return number;
    }

    public void displayReservedRoomsInfo() {
        System.out.println("\nRoom Number:\t\t\t#" + getNumber());
        System.out.println("Room type:\t\t\t" + getType());
        System.out.println("Price per night:\t\t" + getPricePerNight());
        System.out.println("Number of nights:\t\t" + getNightsStay());
        System.out.printf("Room price for %d nights:\t%.2f%n", getNightsStay(), getTotalPrice());
        System.out.println("Reservation number: \t\t" + getReservationNumber());
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
        return String.format("Num: %d, Type: %s, Nights: %d, Cost: %d, Total: %.2f, Name: %s, Reservation: %d, Reserved: %b%n",
                getNumber(), getType(), getNightsStay(), getPricePerNight(), getTotalPrice(), getName(),
                getReservationNumber(), isReserved());
    }
}

