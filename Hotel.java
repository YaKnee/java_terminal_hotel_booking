import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Hotel {
    // Scanner to get user input
    private static Scanner scanner = new Scanner(System.in);
    // Constants for maximum rooms, nights, and costs
    private static final byte MAX_ROOMS_PER_PERSON = 3;
    private static final byte MAX_NIGHTS = 28;
    private static final int MAX_SINGLE_ROOM_COST = 100;
    private static final int MIN_SINLGE_ROOM_COST = 90;
    private static final int MAX_DOUBLE_ROOM_COST = 150;
    private static final int MIN_DOUBLE_ROOM_COST = 140;

    public static void main(String[] args) {
        System.setProperty("file.encoding", "UTF-8"); // y no work
        // Load available and booked rooms from files
        List<Room> allRooms = loadAllRooms();
        boolean repeat = true;
        String name = "null";
        
        do {
            int totalRoomsLeft = checkAvailableRooms(allRooms);
            if (totalRoomsLeft == 0) {
                System.out.println("\nUnfortunately we are fully booked at the moment.");
                System.out.println("Please try again in the future.\n");
                System.exit(0);
            }
            greeting();
            System.out.println("\nWe currently have " + totalRoomsLeft + " room(s) available.");
            // Set/reset variables
            Room room = new Room();
            int roomNum = 0;
            int nights = 0;
            String roomType = "";
            int cost = 0;

            // Menu loop
            do {
                //allRooms = loadAllRooms();
                displayMenu();
                int choice = getSwitchChoice();
                switch (choice) {
                    case 1:
                        currentPrices();
                        break;
                    case 2:
                        name = askName(allRooms);
                        System.out.println("Booking under name: " + name);
                        break;
                    case 3:
                        roomType = askRoomType(allRooms, name);
                        roomNum = askRoom(allRooms, roomType); 
                        for (Room rooms : allRooms) {
                            if (roomNum == rooms.getNumber()) {
                                cost = rooms.getPricePerNight();
                            }
                        }
                        room = new Room(roomNum, roomType, cost);
                        break;
                    case 4:
                        nights = askNights(MAX_NIGHTS);
                        break;
                    case 5:
                        roomInfo(name, room, nights);
                        break;
                    case 6:
                        // checks to see if a room number, number of nights and name has been entered
                        if (roomNum != 0 && nights != 0 && !name.equals("null")) {
                            if (roomIsBooked(allRooms, roomNum)) {
                                System.out.println("\nYou have already booked room #" + roomNum + ".");
                                break;
                            }
                            allRooms = reserveRoom(nights, name, allRooms, room);
                            repeat = reserveAnotherRoom(allRooms, name);
                        } else {
                            System.out.println(
                                    "\nRoom information missing. Please enter a name, choose a room and length of stay first.");
                            System.out.println("If you are unsure what you're missing, select option 5.");
                        }
                        break;
                    case 7:
                        displayAllRooms(allRooms);
                        break;
                    case 8:
                        String fullName = validateName("full");
                        fullName = toProperCase(fullName);
                        displayNameBooking(allRooms, fullName);
                        break;
                    case 9:
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
    // END OF MAIN

    // ============================================================================================================================================================
    // -------------------------------------------------------------------ROOM FILES & GENERATOR-------------------------------------------------------------------
    // ============================================================================================================================================================

    // Read rooms from the file
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
                // Order: Num, Type, Nights, Cost, Total, Name, Reservation Num, Reserved
                int number = Integer.parseInt(parts[0].split(": ")[1]);
                String type = parts[1].split(": ")[1];
                int nights = Integer.parseInt(parts[2].split(": ")[1]);
                int cost = Integer.parseInt(parts[3].split(": ")[1]);
                double total = Double.parseDouble(parts[4].split(": ")[1]);
                String name = parts[5].split(": ")[1];
                int reservationNumber = Integer.parseInt(parts[6].split(": ")[1]);
                boolean isReserved = Boolean.parseBoolean(parts[7].split(": ")[1]);

                Room room = new Room(number, type, nights, cost, total, name, reservationNumber);
                room.setReserved(isReserved);
                allRooms.add(room);
            }
        } catch (FileNotFoundException e) {
            // if no file found create total rooms list
            allRooms = generateAllRooms();
            //save all the rooms to file so we can instantly use it for logic
            saveAllRooms(allRooms);
        } catch (IOException e) {
            // if errors reading/writing file, log exception
            e.printStackTrace();
        }
        return allRooms;
    }

    // Save room info to file
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

    // Method to generate list of rooms
    private static List<Room> generateAllRooms() {
        List<Room> allRooms = new ArrayList<Room>();
        for (int i = 0; i < 50; i++) {
            allRooms.add(new Room((i + 1), "single", MAX_SINGLE_ROOM_COST, "null")); // rooms 1-50 = single @ 100
        }
        for (int i = 50; i < 60; i++) {
            allRooms.add(new Room((i + 1), "single", MIN_SINLGE_ROOM_COST, "null")); // rooms 51-60 = single @ 90
        }
        for (int i = 60; i < 90; i++) {
            allRooms.add(new Room((i + 1), "double", MAX_DOUBLE_ROOM_COST, "null")); // rooms 61-90 = double @ 150
        }
        for (int i = 90; i < 100; i++) {
            allRooms.add(new Room((i + 1), "double", MIN_DOUBLE_ROOM_COST, "null")); // rooms 91-100 = double @ 140
        }
        return allRooms;
    }

    // ============================================================================================================================================================
    // -------------------------------------------------------------------GENERIC TEXT-----------------------------------------------------------------------------
    // ============================================================================================================================================================

    private static void greeting() {
        System.out.println("\nHello and welcome to Hotel Java!");
        System.out.print("As it is the holiday season, you are currently only able to book ");
        System.out.println(MAX_ROOMS_PER_PERSON + " rooms per person.");
    }

    private static void invalidInput() {
        System.out.println("Invalid input. Please only input either 'y' or 'n'.");
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
        System.out.println("\nWe're sorry you couldn't find what you were looking for with us today.");
        System.out.println("We hope you will use our services in the future.\n");
        System.exit(0);
    }

    
    // ============================================================================================================================================================
    // -------------------------------------------------------------------MISC. METHODS----------------------------------------------------------------------------
    // ============================================================================================================================================================

    private static int roomCountForName(List<Room> allRooms, String name) {
        int roomCount = 0;
        for (Room room : allRooms) { 
            // Check if input name matches name on reservation
            if (name.equals(room.getName())) {
                roomCount++;
            }
        }
        return roomCount;
    }

    private static int checkAvailableRooms(List<Room> allRooms){
        int count=0;
        for (Room room : allRooms) {
            if (!room.isReserved()) {
                count++;
            }
        }
        return count;
    }

    // Method to ask the user if they want to enter a draw for a discount
    private static boolean enterDiscountDraw() {
        while (true) {
            System.out.print("\nWould you like to enter for a chance at a discount on the room you just booked? [y/n]: ");
            String discount = scanner.nextLine().trim().toLowerCase();

            if (discount.equals("y")) {
                return true;
            } else if (discount.equals("n")) {
                return false;
            }
            invalidInput();
        }
    }

    // Method to perform a discount
    private static double discountDraw(double bill) {
        // Display information about the discount draw
        System.out.println("\nAre you feeling lucky?");
        System.out.println("Roll #1-25 for a 5% discount.\nRoll #40-50 for a 10% discount.\nRoll #65-70 for a 20% discount.");
        hitEnter();
        // Generate a random number between 1 and 100 for the draw
        int random = 1 + (int) (Math.random() * ((100 - 1) + 1));
        System.out.println("\nYou rolled #" + random + ".");
        if (random >= 1 && random <= 25) { 
            System.out.println("Congratulations! You have received a 5% discount!");
            bill -= (bill * 0.05);
        }else if (random >= 40 && random <= 50) { 
            System.out.println("Congratulations! You have received a 10% discount!");
            bill -= (bill * 0.1);
        }else if (random >= 65 && random <= 70) { 
            System.out.println("Congratulations! You have received a 20% discount!");
            bill -= (bill * 0.2);
        }else {
            System.out.println("Unfortunately you did not hit a winning number.");
        }
        hitEnter();
        return bill;
    }

    
    // Convert string to proper case/title case
    public static String toProperCase(String input) { // why do I do this to myself
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

    // ============================================================================================================================================================
    // -------------------------------------------------------------------MENU-------------------------------------------------------------------------------------
    // ============================================================================================================================================================

    private static void displayMenu() {
        System.out.println("\n+-----------------------------------------------------------------------+");
        System.out.println("|                            Main Menu                                  |");
        System.out.println("+-----------------------------------------------------------------------+");
        System.out.println("| 0. Quit\t\t\t5. Display current room details\t\t|");
        System.out.println("| 1. See current prices\t\t6. Book current room\t\t\t|");
        System.out.println("| 2. Enter name for booking\t7. Display all available rooms\t\t|");
        System.out.println("| 3. Choose Room\t\t8. Display booking(s) by name\t\t|");
        System.out.println("| 4. Choose length of stay\t9. Display booking by reservation number|");
        System.out.println("+-----------------------------------------------------------------------+");
    }

    // Method to get user's choice for switch case
    private static int getSwitchChoice() {
        while (true) {
            System.out.print("Enter your choice: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice < 0 || choice > 9) {
                    System.out.println("Incorrect input. Please enter a number between 0 and 9.");
                } else {
                    return choice;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    // ============================================================================================================================================================
    // -------------------------------------------------------------------CASE 1: PRICES---------------------------------------------------------------------------
    // ============================================================================================================================================================

        private static void currentPrices() {
        System.out.println("\nOur current prices are: ");
        System.out.println("Single rooms #1-50:\t\t" + MAX_SINGLE_ROOM_COST + " euro per night.");
        System.out.println("Single rooms #51-60:\t\t " + MIN_SINLGE_ROOM_COST + " euro per night.");
        System.out.println("Double rooms #61-90:\t\t" + MAX_DOUBLE_ROOM_COST + " euro per night.");
        System.out.println("Double rooms #91-100:\t\t" + MIN_DOUBLE_ROOM_COST + " euro per night.");
    }

    // ============================================================================================================================================================
    // -------------------------------------------------------------------CASE 2: NAME-----------------------------------------------------------------------------
    // ============================================================================================================================================================

    private static String askName(List<Room> allRooms) {
        String fullName;
        boolean repeat = true;
        do {
            String firstName = validateName("first");
            String lastName = validateName("last");
            fullName = toProperCase(firstName) + " " + toProperCase(lastName);

            if (isNameExceedingMaxRooms(fullName, allRooms)) {
                System.out.print("\nMaximum number of rooms [" + MAX_ROOMS_PER_PERSON + "]");
                System.out.println(" have been booked under name: " + fullName);

                while (true){
                    System.out.print("Book under a different name? [y/n]: ");
                    String response = scanner.nextLine().trim().toLowerCase();
                    if (response.equals("n")) {
                        noBooking();
                    } else if (response.equals("y")) {
                        //break out of loop and ask for new name
                        break;
                    } else {
                        invalidInput();
                    }
                } 
            }else{
                repeat = false;
            }
        } while (repeat);
        return fullName;
    }

    // Method to validate user's name
    private static String validateName(String part) {
        String name;
        while(true) {
            System.out.print("\nPlease enter " + part + " name: ");
            name = scanner.nextLine().trim().toLowerCase();
            // Validate the name using a regex pattern
            if (!name.matches("^[\\p{L} .'`\"_-]+")) {
                System.out.println("Invalid input. Numbers are not allowed.");
                System.out.println("List of allowed special characters: . ' ` \" _ -");
            } else {
                System.out.println("\nYou entered the " + part + " name: " + toProperCase(name) + ".");
                // Confirm if the entered name is correct
                while (true) {
                    System.out.print("Is this correct? [y/n]: ");
                    String confirm = scanner.nextLine().trim().toLowerCase();
                    if (confirm.equals("y")) {
                        return name; // Return the validated name if confirmed
                    } else if (confirm.equals("n")) {
                        break; // Repeat the outer loop if not confirmed
                    } else {
                        invalidInput();
                    }
                }
            }
        } 
    }

    private static boolean isNameExceedingMaxRooms(String name, List<Room> allRooms) {
        int nameCount = 0;
        for (Room room : allRooms) { 
            // Checks if the current room's name is not null and equals the name
            if (!room.getName().equals("null") && room.getName().equals(name)) {
                nameCount++;
            }
        }
        return nameCount >= MAX_ROOMS_PER_PERSON;
    }

    // ============================================================================================================================================================
    // -------------------------------------------------------------------CASE 3: ROOM-----------------------------------------------------------------------------
    // ============================================================================================================================================================

    private static boolean isRoomTypeMaxedOut(String roomType, List<Room> allRooms) {
        //number of single rooms = 60, number of double rooms = 40
        int totalRoomsOfType = (roomType.equals("single")) ? 60 : 40;
        int roomCount = 0;

        for (Room room : allRooms) {
            if (room.isReserved() && room.getType().equals(roomType)) {
                roomCount++;
            }
        }
        return roomCount >= totalRoomsOfType;
    }

    private static String askRoomType(List<Room> allRooms, String name) {
        while (true) {
            System.out.print("\nWhat type of room would you like to book? [single/double]: ");
            String roomType = scanner.nextLine().trim().toLowerCase();

            if (roomType.equals("single") || roomType.equals("double")) {
                // Check if all rooms of a specific type are fully booked
                if (isRoomTypeMaxedOut(roomType, allRooms)) {
                    System.out.println("\nUnfortunately, all of our " + roomType + " rooms are fully booked.");
                    // switch room's type
                    roomType = (roomType.equals("single")) ? "double" : "single";

                    while (true){
                        System.out.print("Would you like to proceed with a " + roomType + " room instead? [y/n]: ");
                        String confirm = scanner.nextLine().trim().toLowerCase();
                        if (confirm.equals("y")) {
                            System.out.println("\nYour room type has been switched to " + roomType + ".");
                            break;
                        } else if (confirm.equals("n")) {
                            finalBill(allRooms, name);
                        } else {
                            invalidInput();
                        }
                    } 
                }
                return roomType;
            } else {
                System.out.println("Invalid room type. Please enter either 'single' or 'double'.");
            }
        }
    }

    // Method for manual or auto selection of room num
    private static int askRoom(List<Room> allRooms, String roomType) {
        int roomNum = 0;
        System.out.print("Would you prefer an automatically generated room or manual room selection? [auto = A, manual = M]: ");
        while (true) {
            String inputChoice = scanner.nextLine().trim().toLowerCase();
            if (inputChoice.equals("a")) {
                roomNum = autoRoom(roomType, allRooms);
                System.out.printf("%nYou have been assigned %s room #%d.%n", roomType, roomNum);
                break;
            } else if (inputChoice.equals("m")) {
                displayRoomsOfType(roomType, allRooms);
                roomNum = manualRoomSelection(roomType, allRooms);
                System.out.printf("%nYou have chosen %s room #%d.%n", roomType, roomNum);
                break;
            } else {
                System.out.print("Sorry, '" + inputChoice + "' was not an option.");
                System.out.print(" Please input either 'A' or 'M': ");
            }
        }
        return roomNum;
    }

    // Method to check if selected room number has already been booked
    private static boolean roomIsBooked(List<Room> allRooms, int roomNum) {
        for (Room room : allRooms) {
            if (room.getNumber() == roomNum && room.isReserved()) {
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
                roomNum = 1 + (int) (Math.random() * ((60 - 1) + 1)); // random number between 1 and 60: single
            } else {
                roomNum = 61 + (int) (Math.random() * ((100 - 61) + 1)); // random number between 61 and 100: double
            }
            // Check if the auto-generated room number is not reserved
            if (!roomIsBooked(allRooms, roomNum)) {
                return roomNum;
            }
        }
    }

    // Method to display available rooms based on the room type
    private static void displayRoomsOfType(String roomType, List<Room> allRooms) {
        int numColumns = 10;

        System.out.println("\nOur available " + roomType + " rooms: ");
        int startIndex = roomType.equals("single") ? 0 : 60;
        int endIndex = roomType.equals("single") ? 60 : 100;

        for (int i = startIndex; i < endIndex; i++) {
            Room room = allRooms.get(i);
            // room not reserved = num
            if (!room.isReserved()) {
                System.out.print("#" + room.getNumber() + "\t");
            } else {
                //room reserved = _
                System.out.print("#_\t");
            }
            // creates grid for available rooms
            if ((i + 1) % numColumns == 0) {
                System.out.println();
            }
        }
    }

    // Method to manually select a room
    private static int manualRoomSelection(String roomType, List<Room> allRooms) {
        while (true) {
            System.out.print("\nWhat " + roomType + " room would you like to book?: #");
            try {
                int roomNum = Integer.parseInt(scanner.nextLine().trim());

                // Check if the entered room number is valid for the selected room type
                if ((roomType.equals("single") && roomNum >= 1 && roomNum <= 60) || (roomType.equals("double") && roomNum >= 61 && roomNum <= 100)) {
                    // Check if the selected room number is not already booked
                    if (roomIsBooked(allRooms, roomNum)) {
                         // Display an error message if the selected room is already booked
                        System.out.println("Room #" + roomNum + " is already booked. Please choose a different room.");
                    } else {
                        return roomNum;
                    }
                } else {
                    System.out.println("Please choose a valid " + roomType + " room number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please only enter a number value.");
            }
        }
    }

    // ============================================================================================================================================================
    // -------------------------------------------------------------------CASE 4: NIGHTS---------------------------------------------------------------------------
    // ============================================================================================================================================================

    // Method to ask user for length of stay 
    private static int askNights(byte MAX_NIGHTS) {
        while (true) {
            System.out.print("\nHow many nights do you plan on spending with us? [max: " + MAX_NIGHTS + "]: ");
            try {
                int nights = Integer.parseInt(scanner.nextLine().trim());
                if (nights < 1) {
                    System.out.println("Please only enter positive numbers.");
                } else if (nights > MAX_NIGHTS) {
                    System.out.println("The maximum number of nights you can select is " + MAX_NIGHTS + ".");
                } else {
                    return nights;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a positive number value.");
            }
        }
    }
    
    // ============================================================================================================================================================
    // -------------------------------------------------------------------CASE 5: ROOM DETAILS---------------------------------------------------------------------
    // ============================================================================================================================================================

    // Method to display current room info as you build it
    private static void roomInfo(String name, Room room, int nights) {
        System.out.println("\nName: \t\t\t\t" + (name.equals("null") ? "NaN" : name));
        if (room.getNumber() == 0) {
            System.out.println("Room Number: \t\t\tNaN");
            System.out.println("Room type: \t\t\tNaN");
            System.out.println("Cost of this room per night: \tNaN");
        } else {
            System.out.println("Room Number: \t\t\t" + room.getNumber());
            System.out.println("Room type: \t\t\t" + room.getType());
            System.out.println("Cost of this room per night: \t" + room.getPricePerNight());
        }
        System.out.println("Number of nights: \t\t" + (nights == 0 ? "NaN" : nights));
    }

    // ============================================================================================================================================================
    // -------------------------------------------------------------------CASE 6: ROOM RESERVATION-----------------------------------------------------------------
    // ============================================================================================================================================================

    private static List<Room> reserveRoom(int nights, String name, List<Room> allRooms, Room room) {
        double bill = room.getPricePerNight() * nights;
        // Display room and bill details 
        System.out.printf("%nThe price of this %s room is: %d euro per night.%n", room.getType(), room.getPricePerNight());
        System.out.printf("Your current bill for %d nights is: %.2f euro.%n", nights, bill);
        int reservationNumber = reservationNumber(allRooms);
        Room roomWithBill = new Room(room.getNumber(), room.getType(), nights, room.getPricePerNight(), bill, name, reservationNumber);
        System.out.println("\nPlease confirm room details:");
        System.out.println("\nName: " + toProperCase(name));
        roomWithBill.displayRoomInfo();
        
        allRooms = confirmBooking(allRooms, roomWithBill);
        return allRooms;
    }


    // Method to confirm or abort the current reservation
    private static List<Room> confirmBooking(List<Room> allRooms, Room roomWithBill) {
        while(true) {
            System.out.print("\nConfirm current reservation? [y/n]: ");
            String confirm = scanner.nextLine().trim().toLowerCase();

            if (confirm.equals("y")) {
                // Set the room as reserved
                roomWithBill.setReserved(true);
                allRooms.set(roomWithBill.getNumber() - 1, roomWithBill);
                System.out.println("Room reserved!");
                saveAllRooms(allRooms);

                // Check if the user wants to enter a discount draw
                if (enterDiscountDraw()) {
                    // Calculate the new bill based on the discount draw
                    double newBill = discountDraw(roomWithBill.getTotalPrice());

                    // Check if the bill has been updated and display the appropriate message
                    if (roomWithBill.getTotalPrice() != newBill) {
                        System.out.println("Your bill has been updated!\n");
                        // Update the room information in the list and save the changes
                        roomWithBill.setTotalPrice(newBill);
                        allRooms.set(roomWithBill.getNumber() - 1, roomWithBill);
                        saveAllRooms(allRooms);
                    } else {
                        System.out.println("Your bill remains the same.\n");
                    }
                }
                return allRooms;
            } else if (confirm.equals("n")) {
                // If aborted, return the original available rooms
                return allRooms;
            } else {
                invalidInput();
            }
        }
    }

    // Method to ask the user if they want to book another room
    private static boolean reserveAnotherRoom(List<Room> allRooms, String name) { 
        int totalRoomsLeft = checkAvailableRooms(allRooms);
        while (true) {
            System.out.print("Would you like to book a different room? [y/n]: ");
            String repeat = scanner.nextLine().trim().toLowerCase();
            if (repeat.equals("n")) {
                break;
            } else if (repeat.equals("y")) {
                if (isNameExceedingMaxRooms(name, allRooms)) {
                    System.out.print("\nUnfortunately, you have already booked the max amount of rooms.");
                    break;
                }else if (totalRoomsLeft == 0) {
                    System.out.println("\nUnfortunately we are fully booked at the moment.");
                    break;
                }else {
                    System.out.println("\nWe currently have " + totalRoomsLeft + " room(s) available.");
                    return true;
                }
            } else {
                invalidInput();
            }
        }
        return false;
    }

    // Method to display the final bill
    private static void finalBill(List<Room> allRooms, String name) { 
        // Display the total bill amount
        int count = roomCountForName(allRooms, name);
        if (name.equals("null") || count == 0) {
            noBooking();
        } else {
            double totalPrice = displayNameBooking(allRooms, name);
            System.out.println("\nTotal bill for the above room(s):\n");
            System.out.printf("%.2f euro.%n", totalPrice);
        }
    }
    // ============================================================================================================================================================
    // -------------------------------------------------------------------CASE 7: DISPLAY AVAILABLE ROOMS----------------------------------------------------------
    // ============================================================================================================================================================

    private static void displayAllRooms(List<Room> allRooms){
        int numColumns = 10;
        int count = 1;
        System.out.println("\nHere are all of our available rooms:\n");
        for (Room room : allRooms) {
            if (!room.isReserved()) {
                System.out.print("#" + room.getNumber() + "\t");
            } else {
                System.out.print("#_\t");
            }
            // create grid for available rooms
            if (count++ % numColumns == 0) {
                System.out.println();
            }
        }

    }
    
    // ============================================================================================================================================================
    // -------------------------------------------------------------------CASE 8: RESERVATION BY NAME--------------------------------------------------------------
    // ============================================================================================================================================================

    // Method to display total bookings based on guest's name
    private static double displayNameBooking(List<Room> allRooms, String name) {
        int count = 0;
        double totalPrice = 0;
        // Count rooms in total booked rooms list based on name of guest
        count = roomCountForName(allRooms, name);
        // Display the count of booked rooms
        System.out.println("\n"+count + " reservation(s) under name: " + toProperCase(name));
        // Loop through booked rooms to display room info and accumulate total price
        for (Room room : allRooms) {
            if (name.equals(room.getName())) {
                room.displayRoomInfo();
                totalPrice += room.getTotalPrice();
            }
        }
        return totalPrice;
    }

    // ============================================================================================================================================================
    // -------------------------------------------------------------------(CASE 9) RESERVATION NUMBER--------------------------------------------------------------
    // ============================================================================================================================================================

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
        for (Room room : allRooms) {
            if (room.getReservationNumber() == reservationNumber) {
                roomFound = true;
                room.displayRoomInfo();
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
}
