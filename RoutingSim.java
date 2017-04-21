/**
 * Created by ralfpopescu on 4/11/17.
 */

import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class RoutingSim {

    public static void main(String args[]) {

        Network network = new Network();
        Network splitHorizonNetwork = new Network();
        Network poisonNetwork = new Network();
        int round = 0;
        boolean converged = false;
        int flag = Integer.parseInt(args[2]);
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
        splitHorizonNetwork.initializeNetwork(lines);
        poisonNetwork.initializeNetwork(lines);

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

        while(!converged){ //basic routing
            for(Event e:events){
                if (e.getRound() == round){
                    network.executeEvent(e);
                }
            }
            converged = !(network.propogate());
            round++;
            if (flag == 1) {
              System.out.println("Number of Rounds: " + round);
              System.out.println(network.stats());
            }


            //System.out.println("Round: " + round);
            //System.out.println(network.stats());
        }
        System.out.println("BASIC ROUTING");
        System.out.println(network.stats());
        System.out.println("Number of Rounds: " + round);

        converged = false;
        round = 0;

        while(!converged){ //split horizon routing
            for(Event e:events){
                if (e.getRound() == round){
                    splitHorizonNetwork.executeEvent(e);
                }
            }
            converged = !(splitHorizonNetwork.splitHorizonPropogate());
            round++;
            if (flag == 1) {
              System.out.println("Number of Rounds: " + round);
              System.out.println(network.stats());
            }
            //System.out.println("Round: " + round);
            //System.out.println(network.stats());
        }
        System.out.println("SPLIT HORIZON");
        System.out.println("Number of Rounds: " + round);
        System.out.println(network.stats());


        converged = false;
        round = 0;

        while(!converged){ //poison routing
            for(Event e:events){
                if (e.getRound() == round){
                    poisonNetwork.executeEvent(e);
                }
            }
            converged = !(poisonNetwork.poisonPropogate());
            round++;
            if (flag == 1) {
              System.out.println("Number of Rounds: " + round);
              System.out.println(network.stats());
            }
            //System.out.println("Round: " + round);
            //System.out.println(network.stats());
        }
        System.out.println("POISON");
        System.out.println("Number of Rounds: " + round);
        System.out.println(network.stats());

    }



}
