Code Description:

##Introduction:
The game is written in java through the use of (“javax.lang”) library, in which the main function in the Main.java class calls the class Gamepanel.java, where the main game loop is located, and passing it to a JFrame object. The GamePanel.java class extends  the Canvas class and implements the Runnable, KeyListener interfaces Because of the use threads in the GamePanel.java, by which we initialize a thread by passing a runnable object after overriding the Run() function through the constructor of the Thread class to create a thread for the GamePanel.java class, also I implemented threads in the player class in the aimbot() function , the function that is responsible to make the player seeks and shoots automatically when the target ball is in range, in which this function runs on its own thread , which I noticed after many trials that , when this function runs on its own thread it improves the accuracy of the player


##The Used Classes:
The Main.java Class:
this class is responsible for the declaration of the window, where the game would run, and setting its needed properties, by creating a new object from the class GamePanel.java which is responsible of the drawing the running the game because it contains the game loop

##The GamePanel.java:
This class is responsible of the drawing of the game on the panel through the implementation of the class Canavas, which is displayed to the player, also responsible for the controls of the player in game through the implementation of the class KeyListener, which listens for every key press on the keyboard and makes the appropriate action for each key, the class contains an arraylist of objects from the Class Enemy.java, which will be spawned and generated for the player to kill, in the run function() this arrays list is initialized and after that, these enemies are drawn on the panel through the draw function in the Enemy.java class, The GamePanel.java also contains a player object from the class Player.java , which is responsible for the player spawning and all of his actions, The GamePanel.java also contains an arraylist from the class Bullet.java , because its also responsible for the management and firing of each bullet that the player fires using the keys on his keyboard 
The class starts after the its thread is created and runned through the function (Thread.start()),
Which calls the function run() which initializes all of the other attributes of the class and sets the max frame rate ,which is set to be 30 FPS (frames per seconds) and starts the second thread of (t1) which is responsible of the automatic shooting functionality for the player, after that the run() function calls the gameUpdate() function which Is responsible of updating the status of the bullets and hit detection between each one of the balls and the bullets and the updating of the score of the player when the player hits an enemy successfully, then the run() function calls the gameRender() function which is responsible of the drawing of each one of the enemy, the player and the bullets which is fired by the player , also responsible for displaying the text that generates the random math equation that is displayed for the player of the game and the random color that is picked for the player to kill which gives the player more score , then the run() function calls the gameDraw() function which completes job the gameRender() function do display the content for the player of the game.

##The Player.java:
This class contains all of the attributes of the player and is responsible for initialized those attributes ,also is responsible for drawing the player on the game panel based on those attributes , including the player’s position and managing the cooldown that is set on the player’s ability to fire and also includes the function aimbot() which is responsible for the auto aiming and firing of the player  which human control through the active prediction of the enemy’s position , this function runs on its own thread which improves the accuracy of the targeting operation 

T##he Enemy.java:
This class contains the attributes of each ball regarding the initial random position, color, angle which each ball spawns with, this class is also responsible for the initializing of each one of these attributes, it’s also responsible for the bouncing animation of each ball by doing the appropriate calculations for the position attribute (dx, dy) and rendering the ball accordingly 

##The Bullet.java:
This class contains the attributes of each bullet that is fired by the player though constantly incrementing the position coordinates of the bullet that is fired and checking it through the Boolean function update() function and drawing the bullet if it passes the check on the game panel through the function draw()



##How to play the game:

The game is played through the keyboard using the arrow keys to control the player position, and to fire a bullet press the key “Z”, and to make the player enter the auto aim mode press the key “X”, in this state the player’s position control and firing control is transferred to the computer and cannot be controlled anymore, to disable this press the letter “C” which will give the human player control again.
The game will display a text for example “Kill the Blue also what is 10 + 60”, so for the player to gain point he has to kill the blue ball which will give the player 1 point or he can kill the ball which have the solution of the previous given random generated mathematical equation, (which is 70 for example) by which he gains 3 points.
