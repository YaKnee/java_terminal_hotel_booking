import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Hotel{  
    // Scanner to get user input
    private static Scanner scanner = new Scanner(System.in);
    // Constants for maximum rooms, nights, and costs
    private static final byte MAX_ROOMS = 3;
    private static final byte MAX_NIGHTS = 28;
    private static final int MAX_SINGLE_COST = 100;
    private static final int MIN_SINGLE_COST = 90;
    private static final int MAX_DOUBLE_COST = 150;
    private static final int MIN_DOUBLE_COST = 140;

    // TO DO
    // make an if statement in room selection for max roomtype reached add in main
    // as ifelse
    // change all reservation checks to use attribute of room class
    // change availrooms limiters to constants so can change room type numbers
    // change menu names to be more descriptive-
    // make a readme, how it works, comment how you did and what was difficult
    // add discount draw to after all rooms are booked, after they say no more rooms
    // bookings, as can currently exploit draw scheme
    // add nights to after booking room
    // if choice is 0 and room details havent been filled ask to redo "are you sure?"

    public static void main(String[] args) {
        System.setProperty("file.encoding", "UTF-8"); // y no work
        // Load available and booked rooms from files
        List<Room> allRooms = loadAllRooms();
        greeting();

        // Check if there are available rooms
        if (allRooms.size() < 1) {
            System.out.println("Unfortunately we are fully booked.");
        } // add checks for single/double rooms exceeded?
        else {
            boolean repeat = true;
            String name = "null";
            int choice;
            do {// change to two sout's, single and double?
                int totalRoomsLeft = roomCount(allRooms, name);
                System.out.println("\nWe currently have " + totalRoomsLeft + " rooms available.");
                // Set/reset variables
                Room room = new Room();
                int roomNum = 0;
                int nights = 0;
                String roomType = "";

                // Menu loop
                do {
                    displayMenu();
                    choice = getSwitchChoice();
                    switch (choice) {
                        case 1:
                            currentPrices();
                            break;
                        case 2:
                            name = askName(allRooms);
                            System.out.println("Booking under name: " + name);
                            break;
                        case 3:
                            roomNum = askRoom(allRooms); //////////////////////change this logic.
                            for (Room rooms : allRooms) {
                                if (roomNum == rooms.getNumber()) {
                                    roomType = rooms.getType();
                                }
                            }
                            room = new Room(roomNum, roomType);
                            break;
                        case 4:
                            nights = askNights(MAX_NIGHTS);
                            break;
                        case 5:
                            roomInfo(name, roomNum, roomType, nights);
                            break;
                        case 6:
                            // checks to see if a room number, number of nights and name has been entered
                            if (roomNum != 0 && nights != 0 && !name.equals("null")) {
                                allRooms = confirmAndBookRoom(nights, name, allRooms, room);
                                repeat = bookAnotherRoom(allRooms, name);
                            } else {
                                System.out.println(
                                        "\nInvalid room information. Please choose a room, length of stay, and enter name first.");
                            }
                            break;
                        case 7: 
                            String fullName = validateName("full");
                            fullName = toProperCase(fullName);
                            displayNameBooking(allRooms, fullName);
                            break;
                        case 8:
                            displayReservationBooking(allRooms);
                            break;
                        case 0:
                            repeat = false;
                            break;

                        default:
                            System.out.println("Invalid choice. Please choose a valid option.");
                            break;
                    }

                } while (repeat);

                break;
            } while (isNameExceedingMaxRooms(name, allRooms));
            // Display final bill and save data to file
            finalBill(allRooms, name);
            saveAllRooms(allRooms);
            thankYou();
            scanner.close();
        }
    }

    // Method to load booked rooms from the file
    private static List<Room> loadAllRooms() {
        List<Room> allRooms = new ArrayList<>();
        // Read lines from file as strings
        try (BufferedReader reader = new BufferedReader(new FileReader("all_rooms.txt"))) {
            String line;
            // while the file hasn't reached end
            while ((line = reader.readLine()) != null) {
                // Split the line into parts using ", " as the separator
                String[] parts = line.split(", ");
                // Extract information from each part and create a Room object
                // Order: Num, Type, Nights, Cost, Total, Name, Reservation
                int number = Integer.parseInt(parts[0].split(": ")[1]);
                String type = parts[1].split(": ")[1];
                int nights = Integer.parseInt(parts[2].split(": ")[1]);
                int cost = Integer.parseInt(parts[3].split(": ")[1]);
                double total = Double.parseDouble(parts[4].split(": ")[1]);
                String name = parts[5].split(": ")[1];
                int reservationNumber = Integer.parseInt(parts[6].split(": ")[1]);

                Room room = new Room(number, type, nights, cost, total, name, reservationNumber);
                allRooms.add(room);
            }
        } catch (FileNotFoundException e) {
            allRooms = generateAllRooms();
        } catch (IOException e) { // auto generated.. what do?
            e.printStackTrace();
        }
        return allRooms;
    }

    // Method to generate a list of available room
    private static List<Room> generateAllRooms() { 
        List<Room> allRooms = new ArrayList<Room>();
        for (int i = 0; i < 50; i++) {
            allRooms.add(new Room((i + 1), "single", MAX_SINGLE_COST, "null")); // rooms 1-50 = single @ 100
        }
        for (int i = 50; i < 60; i++) {
            allRooms.add(new Room((i + 1), "single", MIN_SINGLE_COST, "null")); // rooms 51-60 = single @ 90
        }
        for (int i = 60; i < 90; i++) {
            allRooms.add(new Room((i + 1), "double", MAX_DOUBLE_COST, "null")); // rooms 61-90 = double @ 150
        }
        for (int i = 90; i < 100; i++) {
            allRooms.add(new Room((i + 1), "double", MIN_DOUBLE_COST, "null")); // rooms 91-100 = double @ 140
        }
        return allRooms;
    }

    private static void greeting() {
        System.out.println("\nHello and welcome to Hotel Java!");
        System.out.println(
                "As it is the holiday season, you are currently only able to book " + MAX_ROOMS + " rooms in total.");
    }

    private static void displayMenu() {
        System.out.println("\nMenu:");
        System.out.println("0. Quit\t\t\t\t5. Display current room details");
        System.out.println("1. See current prices\t\t6. Book current room");
        System.out.println("2. Enter name for current room\t7. Display booking(s) by name");
        System.out.println("3. Choose Room\t\t\t8. Display booking(s) by reservation number");
        System.out.println("4. Choose length of stay");
    }

    // Method to get user's choice for switch case
    private static int getSwitchChoice() { 
        while (true) {
            System.out.print("Enter your choice: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice < 0 || choice > 8) {
                    System.out.println("Incorrect input. Please enter a number between 0 and 8.");
                } else {
                    return choice;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    // Method for current room pricing
    private static void currentPrices() {
        System.out.println("\nOur current prices are: ");
        System.out.println("Single rooms #1-50:\t" + MAX_SINGLE_COST + " euro per night.");
        System.out.println("Single rooms #51-60:\t " + MIN_SINGLE_COST + " euro per night.");
        System.out.println("Double rooms #61-90:\t" + MAX_DOUBLE_COST + " euro per night.");
        System.out.println("Double rooms #91-100:\t" + MIN_DOUBLE_COST + " euro per night.");
    }

    private static String askName(List<Room> allRooms) { // validate full name not just first then last
        String fullName;
        do {
            String firstName = validateName("first");
            String lastName = validateName("last");
            fullName = toProperCase(firstName) + " " + toProperCase(lastName);

            if (isNameExceedingMaxRooms(fullName, allRooms)) {
                System.out.print(
                        "\nMaximum number of rooms [" + MAX_ROOMS + "] have been booked under name: " + fullName);
                System.out.print("\nWould you like to book under a different name? [y/n]: ");
                String response = scanner.nextLine().trim().toLowerCase();

                if (response.equals("n")) {
                    // User does not want to book under a different name, exit the system
                    noBooking();
                }
                // If 'y', will ask for a new name
            }

        } while (isNameExceedingMaxRooms(fullName, allRooms));
        return fullName;
    }

    // Method to validate user's name
    private static String validateName(String part) {
        String name;
        boolean isValidName = false;
        boolean isValidChoice = false;
        do {
            System.out.print("\nPlease enter " + part + " name: ");
            name = scanner.nextLine().trim().toLowerCase();
            // \\p{L} to allow any foreign character and list of allowed special characters
            if (!name.matches("^[\\p{L} .'`\"_-]+")) {
                System.out.println("Invalid input. \nNumbers are not allowed.");
                System.out.println("List of allowed special characters: . ' ` \" _ -");
            } else {
                do {
                    System.out.print(
                            "You entered the " + part + " name: " + toProperCase(name) + ". Is this correct? [y/n]: ");
                    String confirm = scanner.nextLine().trim().toLowerCase();

                    isValidName = confirm.equals("y");
                    isValidChoice = isValidName || confirm.equals("n");

                    if (!isValidChoice) {
                        System.out.println("Invalid input. Please enter either 'y' or 'n'.");
                    }
                } while (!isValidChoice);
            }
        } while (!isValidName);
        return name;
    }

    private static boolean isNameExceedingMaxRooms(String name, List<Room> allRooms) {
        int nameCount = 0;
        for (Room room : allRooms) { // checks all booked rooms' names
            // Checks if the current room's name is not null and equals the name
            if (room.getName() != null && room.getName().equals(name)) {
                nameCount++;
                if (nameCount >= MAX_ROOMS) {
                    return true;
                }

            } else if (name == null && room.getName() != null && room.getName().isEmpty()) {
                // If the specified name is null, and the current room's name is not null and
                // empty,
                // return false as there can't be any more rooms with the specified name
                return false;
            }
        }
        return false;
    }

    private static String askRoomType() {
        while (true) {
            System.out.print("\nWhat type of room would you like to book? [single/double]: ");
            String roomType = scanner.nextLine().trim().toLowerCase();
            // checking if input is valid
            if (roomType.equals("single") || roomType.equals("double")) {
                return roomType;
            } else {
                System.out.println("Invalid room type. Please enter either single or double.");
            }
        }
    }

    // ask user for manual or auto selection of rooms, and return selected room
    // number
    private static int askRoom(List<Room> allRooms) {
        String roomType = askRoomType();
        int roomNum = 0;
        do {
            System.out.println("\nAutomatically generate an available " + roomType + " room: [auto]");
            System.out.println("Or would you like to choose your own " + roomType + " room?: [manual]");
            String inputChoice = scanner.nextLine().trim().toLowerCase();

            if (inputChoice.equals("auto")) {
                roomNum = autoRoom(roomType, allRooms);
                break;
            } else if (inputChoice.equals("manual")) {
                roomNum = displayallRooms(roomType, allRooms);
                break;
            } else {
                System.out.print("Sorry, '" + inputChoice + "' was not an option.");
                System.out.print(" Please input either auto or manual.\n");
            }
        } while (true);
        return roomNum;
    }

    // checks if input/generated room number has already been booked
    private static boolean isRoomBooked(List<Room> allRooms, int roomNum) {
        for (Room room : allRooms) {
            if (room.getNumber() == roomNum && !room.getName().equals("null")) {
                return true; // room is already booked
            }
        }
        return false; // room is not yet booked
    }

    // Method to auto-generate an available room number based on the room type
    private static int autoRoom(String roomType, List<Room> allRooms) {
        int roomNum = 0;
        while (true) {
            if (roomType.equals("single")) {
                // Generate a random number between 1 and 60 for a single room
                roomNum = 1 + (int) (Math.random() * ((60 - 1) + 1)); // random number between 1 and 60
            } else {
                // Generate a random number between 61 and 100 for a double room
                roomNum = 61 + (int) (Math.random() * ((100 - 61) + 1)); // random number between 61 and 100
            }
            // Check if the auto-generated room number is not reserved
            if (!isRoomBooked(allRooms, roomNum)) {
                System.out.printf("%nYou have been assigned %s room #%d.%n", roomType, roomNum);
                return roomNum;
            }
        }
    }

    // Method to manually select an available room number based on the room type
    private static int displayallRooms(String roomType, List<Room> allRooms) {
        int numColumns = 10;

        System.out.println("\nOur available " + roomType + " rooms: ");
        int startIndex = roomType.equals("single") ? 0 : 60;
        int endIndex = roomType.equals("single") ? 60 : 100;

        for (int i = startIndex; i < endIndex; i++) {
            Room room = allRooms.get(i);

            if (!isRoomBooked(allRooms, room.getNumber())) {
                System.out.print("#" + room.getNumber() + "\t");
            } else {
                System.out.print("#_\t");
            }
            // creates grid for available rooms
            if ((i + 1) % numColumns == 0) {
                System.out.println();
            }
        }
        int roomNum = manualRoomSelection(roomType, allRooms);
        System.out.printf("%nYou have chosen %s room #%d.%n", roomType, roomNum);
        return roomNum;
    }

    // Method to prompt user for specific room number
    private static int manualRoomSelection(String roomType, List<Room> allRooms) {
        while (true) {
            System.out.print("\nWhat " + roomType + " room would you like to book?: #");
            try {
                int roomNum = Integer.parseInt(scanner.nextLine().trim());
                // Check if the entered room number is valid for the selected room type
                if ((roomType.equals("single") && roomNum >= 1 && roomNum <= 60) ||
                        (roomType.equals("double") && roomNum >= 61 && roomNum <= 100)) {
                    // Check if the selected room number is not already booked
                    if (!isRoomBooked(allRooms, roomNum)) {
                        Room room = new Room(roomNum, roomType);
                        // Remove the reserved room from the available rooms list
                        allRooms = reserveRoom(allRooms, room);
                        return roomNum;
                    } else {
                        // Display an error message if the selected room is already booked
                        System.out.println("Room #" + roomNum + " is already booked. Please choose a different room.");
                    }
                } else {
                    // Handle the case where the input is not a valid integer
                    System.out.println(
                            "Invalid room number. Please enter a valid room number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number value.");
            }
        }
    }

    // Ask user for length of stay [1-MAX]
    private static int askNights(byte MAX_NIGHTS) {
        while (true) {
            System.out.print("\nHow many nights do you plan on spending with us? [max: " + MAX_NIGHTS + "]: ");
            try {
                int nights = Integer.parseInt(scanner.nextLine().trim());
                // checking if input is valid
                if (nights < 1) {
                    System.out.println("Please only enter positive numbers.");
                } else if (nights > MAX_NIGHTS) {
                    System.out.println("The maximum number of nights is " + MAX_NIGHTS + ".");
                } else { // valid input
                    return nights;
                }
            } catch (NumberFormatException e) {
                // handles case where input is not valid integer
                System.out.println("Invalid input. Please enter a number value.");
            }
        }
    }
    ///////////////////////////////////////////////////////////////change and add cost
    // Method to display current room info as you build it
    private static void roomInfo(String name, int roomNum, String roomType, int nights) {
        System.out.println("\nName: \t\t\t\t" + (name.equals("null")||name.isEmpty() ? "NaN" : name));
        System.out.println("Room Number: \t\t\t" + (roomNum == 0 ? "NaN" : "#" + roomNum));
        System.out.println("Room type: \t\t\t" + (roomType.isEmpty() ? "NaN" : roomType));
        System.out.println("Number of nights: \t\t" + (nights == 0 ? "NaN" : nights));
    }

    // Method to ask the user if they want to enter a draw for a discount
    private static double enterDiscountDraw(double bill) {
        while (true) {
            System.out.print("\nWould you like to enter a draw for a discount? [y/n]: ");
            String discount = scanner.nextLine().trim().toLowerCase();

            if (discount.equals("y")) {
                return discountDraw(bill);
            } else if (discount.equals("n")) {
                return bill;
            }
            System.out.println("'" + discount + "' was an invalid option. Please try again.");
        }
    }

    // Method to perform a discount draw and return the updated bill amount
    private static double discountDraw(double bill) {
        // Display information about the discount draw
        System.out.println("\nRoll #71-100 for a 10% discount.\nRoll #20-30 for a 20% discount.");
        hitEnter();
        // Generate a random number between 1 and 100 for the draw
        int random = 1 + (int) (Math.random() * ((100 - 1) + 1));
        System.out.println("\nYou rolled #" + random + ".");
        if (random <= 100 && random > 70) { // 71-100 = 10%
            System.out.println("Congratulations! You have received a 10% discount!");
            hitEnter();
            return bill - (bill * 0.1);
        } else if (random >= 20 && random <= 30) { // 20-30 = 20%
            System.out.println("Congratulations! You have received a 20% discount!");
            hitEnter();
            return bill - (bill * 0.2);
        } else {
            System.out.println("Unfortunately you did not hit a winning number.");
            hitEnter();
            return bill;
        }
    }

    private static List<Room> confirmAndBookRoom(int nights, String name, List<Room> allRooms, Room room) {

        double bill = room.getPricePerNight() * nights;

        // Display room and bill details (own method?)
        System.out.printf("%nThe price of this %s room is: %d euro per night.%n",
                room.getType(),
                room.getPricePerNight());
        System.out.printf("Your current bill for %d nights is: %.2f euro.%n", nights,
                bill);
        bill = enterDiscountDraw(bill);
        int reservationNumber = reservationNumber(allRooms);
        Room roomWithBill = new Room(room.getNumber(), room.getType(), nights,
                room.getPricePerNight(), bill,
                name, reservationNumber);
        System.out.println("\nPlease confirm room details:");
        System.out.println("\nName: " + toProperCase(name));
        roomWithBill.displayRoomInfo();

        // Confirm reservation and update room lists
        allRooms = confirmReservation(allRooms, room, roomWithBill);
        return allRooms;
    }

    // Method to confirm or abort the current reservation
    private static List<Room> confirmReservation(List<Room> allRooms, Room room, Room roomWithBill) { // add isReserved
                                                                                                      // to this instead
                                                                                                      // of own method
        do {
            System.out.print("\nPress 'C' to Confirm or 'A' to Abort current reservation: ");
            String confirm = scanner.nextLine().trim().toLowerCase();

            if (confirm.equals("c")) {
                System.out.println("Room reserved!");
                // add booked room to totalRoomsBooked list
                if (room.getNumber() == roomWithBill.getNumber()) {
                    allRooms.set(room.getNumber() - 1, roomWithBill);
                }
                // update available rooms list minus booked room
                return reserveRoom(allRooms, room);
            } else if (confirm.equals("a")) {
                // If aborted, return the original available rooms
                return allRooms;
            } else {
                System.out.println("'" + confirm + "' was not an option. Please only enter C or A.");
            }
        } while (true);
    }

    // Method to remove a reserved room from the available rooms list
    // try to use attribute of Room instead of this method///////////////////////
    private static List<Room> reserveRoom(List<Room> allRooms, Room reservedRoom) {
        // Find the corresponding room in allRooms
        for (int i = 0; i < allRooms.size(); i++) {
            Room currentRoom = allRooms.get(i);
            if (currentRoom.getNumber() == reservedRoom.getNumber()) {
                // Mark the room as reserved
                currentRoom.setReserved();
                // Update the room in the allRooms list
                allRooms.set(i, currentRoom);
                break;
            }
        }
        return allRooms;
    }

    // Method to ask the user if they want to book another room
    private static boolean bookAnotherRoom(List<Room> allRooms, String name) { // change to boolean
        boolean bookAnother = false;
        while (!bookAnother) {
            System.out.print("Would you like to book a different room?: [y/n] ");
            String repeat = scanner.nextLine().trim().toLowerCase();
            if (repeat.equals("n")) {
                // If no, and at least one room is booked, break out of the loop
                if (roomCount(allRooms, name) >= 1) {
                    break;
                } else {
                    // If no and no room is booked, display a message and exit the program
                    noBooking();
                }
            } else if (repeat.equals("y")) {
                // If yes, set to true and break out of the loop
                if (isNameExceedingMaxRooms(name, allRooms)) {
                    System.out.print("\nUnfortunately, you have already booked the max amount of rooms.");
                    break;
                } else {
                    bookAnother = true;
                }
                break;
            } else {
                System.out.println("Invalid input. Please enter either 'y' or 'n'.");
            }
        }
        return bookAnother;
    }

    // Method to display the final bill
    private static void finalBill(List<Room> allRooms, String name) { // maybe add enter draw here
        // Display the total bill amount
        int count = roomCount(allRooms, name);
        if (count == 0) {
            noBooking();
        }
        double totalPrice = displayNameBooking(allRooms, name);
        System.out.println("\nTotal bill for the above room(s):\n");
        System.out.printf("%.2f euro.%n", totalPrice);

    }

    // Method to display total bookings based on guest's name
    private static double displayNameBooking(List<Room> allRooms, String name) {
        int count = 0;
        double totalPrice = 0;
        // Count rooms in total booked rooms list based on name of guest
        count = roomCount(allRooms, name);
        // Display the count of booked rooms and the reservation details
        System.out.println("\nYou have booked " + count + " rooms.");
        System.out.println("Reservation(s) under name: " + toProperCase(name));
        // Loop through booked rooms to display room info and accumulate total price
        for (Room room : allRooms) {
            // Check if room's name is not null before calling equals
            if (name != null && name.equals(room.getName())) {
                room.displayRoomInfo();
                totalPrice += room.getTotalPrice();
            }
        }
        return totalPrice;
    }

    private static int roomCount(List<Room> allRooms, String name) {
        int roomCount = 0;
        for (Room room : allRooms) { // checks all booked rooms' names
            // Check if the room's name is not null before calling equals
            if (name != null && name.equals(room.getName())) {
                roomCount++;
            }
        }
        return roomCount;
    }

    private static void displayReservationBooking(List<Room> allRooms) {
        int reservationNumber;
        boolean roomFound = false;
        while (true) {
            System.out.print("\nPlease enter reservation number [10000-99999]: ");
            try {
                reservationNumber = Integer.parseInt(scanner.nextLine());

                if (reservationNumber >= 10000 && reservationNumber <= 99999) {
                    break; // Break the loop if the input is valid
                } else {
                    System.out.println("Invalid input. Please enter a number between 10000 and 99999.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        for (Room room : allRooms) { ////////////////// use reservationValidation as if then display
            if (room.getReservationNumber() == reservationNumber) {
                roomFound = true;
                room.displayRoomInfo();
                // break;
            }
        }
        if (!roomFound) {
            System.out.println("No rooms found under reservation number: #" + reservationNumber);
        }
    }

    private static int reservationNumber(List<Room> allRooms) {
        int reservationNumber;
        do {
            // Generate a random reservation number between 10000 and 99999.
            reservationNumber = 10000 + (int) (Math.random() * ((99999 - 10000) + 1));
        } while (reservationNumberValidation(allRooms, reservationNumber));
        return reservationNumber;
    }

    private static boolean reservationNumberValidation(List<Room> allRooms, int reservationNumber) {
        // Check if the generated reservation number already exists in the booked rooms.
        for (Room room : allRooms) {
            if (room.getReservationNumber() == reservationNumber) {
                return true; // The reservation number is already in use.
            }
        }
        return false; // The reservation number is valid and not in use.
    }

    private static void hitEnter() {
        System.out.print("Hit enter to continue: ");
        scanner.nextLine();
    }

    private static void thankYou() {
        System.out.println("\nThank you for choosing Hotel Java.");
        System.out.println("We hope you enjoy your stay.\n");
    }

    private static void noBooking() {
        // Display a message when no booking is found, and exit the program.
        System.out.println("\nWe're sorry you couldn't find what you were looking for with us today.");
        System.out.println("We hope you will use our services in the future.");
        System.exit(0);
    }

    private static void saveAllRooms(List<Room> allRooms) {
        // Save the details of all rooms to txt file
        try (FileWriter writer = new FileWriter("all_rooms.txt")) {
            for (Room room : allRooms) {
                writer.write(room.totalRoomsToTXT());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

// Convert the input string to proper case/title case
public static String toProperCase(String input) { //why do I do this to myself
    // StringBuilder to store the result
    StringBuilder properCase = new StringBuilder();
    boolean makeUpperCase = true;
    // Loop through each character in input string
    for (char c : input.toCharArray()) {
        if (Character.isWhitespace(c)) {
            // Handle whitespace characters
            properCase.append(c);
            makeUpperCase = true;
        } else if (Character.isLetter(c)) {
            // Handle letter characters
            if (makeUpperCase) {
                // Capitalize the first letter of a word
                properCase.append(Character.toUpperCase(c));
                makeUpperCase = false;
            } else {
                // Convert the rest of the letters to lowercase
                properCase.append(Character.toLowerCase(c));
            }
        } else {
            // Handle special characters
            properCase.append(c);
            makeUpperCase = true;
        }
    }

    // StringBuilder to String and return result
    return properCase.toString();
}
}
