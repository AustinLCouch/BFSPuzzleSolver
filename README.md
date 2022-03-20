# BFSPuzzleSolver
## UI
This program offers usability in both a PTUI and/or a JavaFX GUI

## MVC
This project uses an MVC design pattern that allows the central model unit to perform the Breadth-First-Search functionality for both a "Traffic Jam" game and a "Hoppers" game. This was designed and coded collaboratively with my classmate Hritik Gupta.

## "Traffic Jam"
This was the portion of the program that I personally wrote the view and controller code for. This small game embues the user with Load, Hint, and Reset capabilities. This game involves the user moving rectangular "cars" around that can only be moved forward or backward one unit at a time. The objective is to move the red car all the way across to the rightmost wall of the gameboard, signifying the car making it out of the Traffic Jam.

## Gameplay
The user is able to click anywhere within a "car" then click the closest unit either forward or backward and the car will move to that position. Any alteration from this play will be noted in the help bar at the top of the GUI.

### Load
Allows the user to load in a text file that the GUI or PTUI will display and run through the model.

### Hint
This is the Breadth-First-Search component that at the click of a button, moves a "car" one block in the correct direction for the current best possible solution.

### Reset
This button resets the current board back to its initial load state with all of the cars in those positions. The Hint mechanic works with this newly set board.

## "Hoppers"
This was my classmate's code working for the "Hoppers" game with very similar functionality to mine as they use the same model.
