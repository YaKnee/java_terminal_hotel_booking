Loads in available rooms file, creates one if there isn't one. 
If no available rooms left, code will stop

The program welcomes you, and displays number of rooms left
Menu appears and you can choose any option.
#0: Exit program. 
	-If room(s) booked, displays final price of booked room(s)
	-If no rooms booked, display noBooking message
#1: Displays rooms prices.
#2: Name for booking. If the name has surpassed max_rooms, it will ask you for another name.
#3: Asks for room type, and if you want auto/manual room selection.
	-If all rooms of one type are not available, it will switch your room type if you wish, program ends if not.
	-Only allow you to select rooms that are available.
	-Automatically will only choose available rooms.
#4: Asks for length of stay. Only numbers between 1 and MAX_NIGHTS valid.
#5: Displays info of current booking.
#6: Confirm or abort current reservation (Cannot choose until you have filled in the details)
	-If confirmed, asks if you would like discount on room just booked.
	-Then asks you if you would like to book another room.
	-If no repeat, or max rooms reached-> display bookings under name and quit.
#7: Display all currently available rooms.
#8: Display all bookings found under a name.
	-Will not update current booking name
#9: Display booking by reservation number.


-All choices have been tested for incorrect input and handles them.
-Two copies were made for the available rooms file for testing at different capacities. Either copy and paste or rename to all_rooms.txt


Hardest parts:
-Reading in that damn file with the correct spacing between words. If I had created it without a toString method, it would've been so much easier, but I wanted it to be readable by people and computers.
-Making a To Proper Case method

Didn't work:
if(!name.matches("^[\\p{L} .'`\"_-]+"))
-was supposed to allow for any Unicode letter but didn't. List of allowed special characters works though.
