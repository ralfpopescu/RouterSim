/**
 * Created by ralfpopescu on 4/11/17.
 */

import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class RoutingSim {

    public static void main(String args[]) {

        Network network = new Network();
        int round = 0;
        boolean converged = false;

        ArrayList<String> lines = new ArrayList<String>();

        Scanner scanner = null; //get information from text file
        try {
            scanner = new Scanner(new File(args[0]));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (scanner.hasNextLine()) { //get all words from text message
            String s = scanner.nextLine();
            lines.add(s);
        }

        network.initializeNetwork(lines);

        ArrayList<Event> events = new ArrayList<Event>();

        scanner = null; //get information from text file
        try {
            scanner = new Scanner(new File(args[1]));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (scanner.hasNextLine()) { //get all words from text message
            String s = scanner.nextLine();
            Event e = new Event(s);
            events.add(e);
        }

        while(!converged){

            for(Event e:events){
                if (e.getRound() == round){
                    network.executeEvent(e);
                }
            }

            converged = !(network.propogate());

            round++;
            System.out.println("Round: " + round);
            System.out.println(network.stats());
        }

        System.out.println(network.stats());

    }

}
