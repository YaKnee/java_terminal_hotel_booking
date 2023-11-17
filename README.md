[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-24ddc0f5d75046c5622901739e7c5dd533143b0c8e959d652212380cedb1ea36.svg)](https://classroom.github.com/a/yyC4-1nU)
[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-718a45dd9cf7e7f842a935f5ebbe5719a5e09af4491e668f4dbf3b35d5cca122.svg)](https://classroom.github.com/online_ide?assignment_repo_id=12940356&assignment_repo_type=AssignmentRepo)
# Prog_lang1_final


Purpose of the assignment

Purpose is to assemble all the topics in this course together with a well-designed java- program. You can choose your own topic, as long as it fulfills the technical specifications. There are few example topics at the end of this guide that also can be selected as the assignment.

Technical specifications

Program uses basic structures correctly. Such as variables, if- statements, loops, logical operators, arithmetic etc. Also following criteria must be met:

·       Code is separated sensibly into main method and other methods with necessary libraries included 

·       All variables and methods have sensible and understandable names

·       Use of arrays when it comes to storing and retrieving data

·       Well commented code

·       No global variables, global constants can be used

·       Compiling and running goes without fatal errors. Improper inputs do not crash the program.

Assignment is done personally

This is a personal assignment. copy-paste from others and from the internet is prohibited and will lead to a fail from the assignment. Of course you can consult friends, class mates and teacher.

Returning the assignment and timetable

Assignment must be ready and pushed to git by Friday 15.12.2023 at 23.59. Returned files must include a short description of the functionality of the program.

Approximate criteria for grading the assingment

Grade 0 – Assingment is not returned.

Grade 1 – Assingment is returned. However, it has major flaws and doesn´t work as it is supposed to.

Grade 2 – Assingment carries out minimum criteria but has flaws or the assignment itself is too “easy”.

Grade 3 – Working program without any major flaws. Program code well documented and commented.

Grade 4 – Assignment is more challenging than the average. Quality of the code and documentation is better than average. 

Grade 5 - Work is done exceptionally well and clearly stands out from average assignments.

Assignments 

Here are some example topics. If you do your own assignment the challenge level should be in the same level with these assignments. If in doubt, consult the teacher.

**1.Lottery machine.**  Make a command line application that draws lottery numbers. 7 numbers and 3 bonus numbers. The numbers are between 1 and 39. At the end, the application will sort the numbers from biggest to smallest. Program checks that there are no duplicate numbers in the lottery draw. First the regular numbers and then the bonus numbers. You can also include lottery tickets from few users and define prize money for different results. For example, 5 numbers correct, 5+1 correct, 6 correct, 6+1 correct and all seven correct. You can also add a feature that draws random lottery numbers for the user. After the actual lottery draw the application tells how many numbers were correct and how much money the user won. Text based user interface is defined by the student. 

 

**2. Standings table.** Command line application which lets you input ice hockey or football scores and then prints out standings for example 12 teams. For example:

Input function (1= add scores, 2= print standings, 3= exit program): 1

Home team? Liverpool

Visiting team? Manchester United

Goals for home team? 5

Goals for visiting team? 0

Liverpool won 5-0! Score added!

 Input function (1= add scores, 2= print standings, 3= exit program): 2

Team           Win               Draw            Lost              Goals         Points

Liverpool      1                  0                0                5-0            3

Manchester U   0                  0                1                0-5            0                     

Input function (1= add scores, 2= print standings, 3= exit program): 3

Team is added to table either by using add score function or as an individual operation. Win gives you 3 points, draw one point and loss zero points. Table is printed using sorting according to points. As a bonus you can add a function that asks who made the goals so you can add goal scorer table as well.

**3. Hotel room booking program:**

Simple program: The hotel has n rooms, numbers 1-n. The number of rooms is assigned to the program as standard. The user reserves the room by selecting the room number. If the room is already booked, the program asks you to dial another room number. The room rate is EUR 100 per night. The user gives the number of nights, and the program will indicate the total amount of the invoice. The program is ready to book rooms for as long as the user wants, however, no more than n rooms. Here, it is sufficient to check in the input check that it is a number between 1 and n.

Additional features: As before, the program initially draws a total number of rooms between 30-70 and prices per night between 80-100 euros. The program draws a room number from those available rooms. At least one subroutine has been formed in the program. The input check also covers non-numbers.

Additional features part 2: As above, there are an even number of rooms between 40-80, and half of them (first half) are single and a half (half) double rooms. The program menu must be implemented in a versatile and illustrative way. The program needs to perform as much as possible in problem situations. In the program, the rooms to be booked can either be drawn by machine or selected by the user. However, always make sure that the Room Type is available (bookable).

Room rates are

· Single room: 100 euros per night and

· Double room: EUR 150 per night.

The program draws whether to give a 0%, 10% or 20% discount on the final price of the rooms. The user enters the room size and number of nights, and the program reports the total amount of the invoice after a possible discount. The program is ready to book rooms for as long as the user wants, however not more than the above numbers of single and double rooms. The program always checks the room type before the reservation to ensure their availability. At least two substantially different subroutines have been formed in the program. 

Important: All of the above options must be checked for errors by the user! 4-5 grade the job also list in the comment field the additional features that you have implemented. 

Other limitations of the assignment that must be taken into account in the final work:

Reservation number: (int) must be between 10000-99999. The program draws the reservation number randomly.

Reserve name: (string) eg Esa Kunnari

When making a new reservation, first check the reservation of that room with a boolean-type subroutine. You can set the upper limit of the Rooms table to 300 rooms. If there are rooms that maximum number, then there are max single rooms. 150pcs and 150 for two people.

The program must be able to search for bookings either with the booking number or with the name of the booker. You can design the menu structure of the program yourself. However, first, make sure you have taken taking into account which parts of the program are to be implemented.

You can also make your own additions to the program depending on when there is enough time and energy to do the program. There is a final course reserved for this. Don’t try to go from where the fence is Lowest, but challenge yourself and always strive for the best outcome.

Before you start doing the actual coding part, plan the things needed in the program and their implementation carefully. This will save you a lot of time in the process of writing the actual code.
