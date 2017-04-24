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
        int round = 1;
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

        int finalEvent = events.get(events.size() - 1).getRound() + 1;
        int convergenceRound = 0;

        System.out.println("BASIC ROUTING" + "\n");
        if (flag == 1) {
            System.out.println("Initial Round");
            System.out.println(network.stats());
        }

        while(!converged && round < 100 || (round < finalEvent)){ //basic routing
            for(Event e:events){
                if (e.getRound() == round){
                    network.executeEvent(e);
                    if(flag == 1) {
                        System.out.println("Event happened!");
                    }
                }
            }
            boolean oldConverged = converged;
            converged = !(network.propogate());

            if(converged && !oldConverged){
                if(flag == 1) {
                    System.out.println("Convergence detected at round " + round);
                    System.out.println("Convergence delay was " + (round - convergenceRound) + "\n");
                }
                //convergenceRound = round;
            }

            if(oldConverged && !converged){
                if(flag == 1) {
                    System.out.println("Convergence disrupted at round " + round + "\n");
                }
                convergenceRound = round;
            }

            if (flag == 1) {
              System.out.println("Round: " + round);
              System.out.println(network.stats());
            }
            round++;

        }
        if (round >= 100) {
          System.out.println("COUNT TO INFINITY" + "\n");
        }
        if (flag != 1) {
            System.out.println("Number of Rounds: " + (round - 1));
            System.out.println(network.stats());
            System.out.println("Last convergence delay was " + (round - convergenceRound - 1) + "\n");
        }

        converged = false;
        round = 1;
        convergenceRound = 0;

        System.out.println("SPLIT HORIZON" + "\n");
        if (flag == 1) {
            System.out.println("Initial Round");
            System.out.println(splitHorizonNetwork.stats());
        }
        while(!converged && round < 100 || (round < finalEvent)){ //split horizon routing
            for(Event e:events){
                if (e.getRound() == round){
                    splitHorizonNetwork.executeEvent(e);
                    if(flag == 1) {
                        System.out.println("Event happened!");
                    }
                }
            }
            boolean oldConverged = converged;
            converged = !(splitHorizonNetwork.splitHorizonPropogate());

            if(converged && !oldConverged){
                if(flag == 1) {
                    System.out.println("Convergence detected at round " + round);
                    System.out.println("Convergence delay was " + (round - convergenceRound) + "\n");
                }
                //convergenceRound = round;
            }

            if(oldConverged && !converged){
                if(flag == 1) {
                    System.out.println("Convergence disrupted at round " + round + "\n");
                }
                convergenceRound = round;
            }


            if (flag == 1) {
              System.out.println("Round: " + round);
              System.out.println(splitHorizonNetwork.stats());
            }
            round++;
            //System.out.println("Round: " + round);
            //System.out.println(network.stats());
        }
        if (round >= 100) {
          System.out.println("COUNT TO INFINITY" + "\n");
        }
        if (flag != 1) {
            System.out.println("Number of Rounds: " + (round - 1));
            System.out.println(splitHorizonNetwork.stats());
            System.out.println("Last convergence delay was " + (round - convergenceRound  - 1) + "\n");
        }


        converged = false;
        round = 1;
        convergenceRound = 0;

        System.out.println("POISON REVERSE" + "\n");
        if (flag == 1) {
            System.out.println("Initial Round");
            System.out.println(poisonNetwork.stats());
        }
        while(!converged && round < 100 || (round < finalEvent)){ //poison routing
            for(Event e:events){
                if (e.getRound() == round){
                    poisonNetwork.executeEvent(e);
                    if(flag == 1) {
                        System.out.println("Event happened!");
                    }
                }
            }
            boolean oldConverged = converged;
            converged = !(poisonNetwork.poisonPropogate());

            if(converged && !oldConverged){
                if(flag == 1) {
                    System.out.println("Convergence detected at round " + round);
                    System.out.println("Convergence delay was " + (round - convergenceRound) + "\n");
                }
                //convergenceRound = round;
            }

            if(oldConverged && !converged){
                if(flag == 1) {
                    System.out.println("Convergence disrupted at round " + round + "\n");
                }
                convergenceRound = round;
            }


            if (flag == 1) {
              System.out.println("Round: " + round);
              System.out.println(poisonNetwork.stats());

            }
            round++;
            //System.out.println("Round: " + round);
            //System.out.println(network.stats());
        }
        if (round >= 100) {
          System.out.println("COUNT TO INFINITY" + "\n");
        }
        if (flag != 1) {
            System.out.println("Number of Rounds: " + (round - 1));
            System.out.println(poisonNetwork.stats());
            System.out.println("Last convergence delay was " + (round - convergenceRound - 1) + "\n");
        }

    }



}
