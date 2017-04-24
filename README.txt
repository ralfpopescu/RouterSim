Aaron Parry, aparry3
Ralf Popescu, rpopescu3

test.txt: Initial setup of network
events.txt: List of router events

RoutingSim.java: Main loop for network simulation
Router.java: Router class handling individual router logic
Network.java: Network class maintaining the structure of the network
Event.java: Event class used to handle network events

TO RUN:
1. Navigate to RouterSim Directory
2. Compile all java file
 - 'javac *.java'
3. Run RoutingSim.java with desired text files and flag Number
 - 'java RoutingSim <network init file> <events file> <flag>'

Notes: The way we count rounds may cause off-by-one discrepancies with “official” solutions.
