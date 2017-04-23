/**
 * Created by ralfpopescu on 4/12/17.
 */

import java.util.ArrayList;

public class Network {

    ArrayList<Router> routers;
    ArrayList<Link> links;
    int[][] adjMatrix;
    int numOfRouters;


    public Network(){
        routers = new ArrayList<Router>();
        links = new ArrayList<Link>();
    }



    public void initializeNetwork(ArrayList<String> input){

        numOfRouters = Integer.parseInt(input.get(0));
        adjMatrix = new int[numOfRouters][numOfRouters];

        for(int i =0; i < numOfRouters; i++){
            for(int j =0; j < numOfRouters; j++){
                adjMatrix[i][j] = -1;
            }
        }

        for(int i = 0; i < numOfRouters; i++){
            routers.add(new Router(i, numOfRouters, this));
        }

        for (int j = 1; j < input.size(); j++){
            addLink(input.get(j));
        }

        for(Router r: routers){
            r.initDistanceVector();
        }


    }

    public void addLink(String link){
        String[] split = link.split("\\s+");
        int r1 = Integer.parseInt(split[0]);
        int r2 = Integer.parseInt(split[1]);
        int cost = Integer.parseInt(split[2]);

        adjMatrix[r1][r2] = cost;
        adjMatrix[r2][r1] = cost;

    }


    public ArrayList<Integer> getNeighbors(int i){

        ArrayList<Integer> neighbors = new ArrayList<Integer>();

        for (int j =0; j < numOfRouters; j++){
            if(adjMatrix[i][j] != -1){
                neighbors.add(j);
            }
        }
        return neighbors;
    }

    public Router getRouter(int x){
        return routers.get(x);
    }

    public void executeEvent(Event e){
        int r1 = e.getRouter1();
        int r2 = e.getRouter2();
        int cost = e.getCost();
        if (cost == -1) {
          System.out.println("Remove link: " + r1 +"-" +r2);

        }
        adjMatrix[r1][r2] = cost;
        adjMatrix[r2][r1] = cost;

        Router router1 = getRouter(r1);
        Router router2 = getRouter(r2);

        router1.eventUpdate(e, r2);
        router2.eventUpdate(e, r1);
        // System.out.println(router2.printDistanceRouter());



    }

    public int[][] getAdjMatrix(){
        return adjMatrix;
    }

    public boolean propogate(){
        boolean changed = false;
        for(Router r: routers){
            changed = changed || r.propogateVector();
        }
        return changed;
    }

    public boolean splitHorizonPropogate(){
        boolean changed = false;
        for(Router r: routers){
            changed = changed || r.propogateSplitHorizon();
        }
        return changed;
    }

    public boolean poisonPropogate(){
        boolean changed = false;
        for(Router r: routers){
            changed = changed || r.propogatePoison();
        }
        return changed;
    }

    public String stats(){
        String stats = "";
        for(int i=0;i<numOfRouters;i++){
            int[] dv = routers.get(i).getDistanceVector();
            int[] nv = routers.get(i).getNextVector();
            int[] hv = routers.get(i).getHopVector();

            String dvs = "";
            String nvs = "";
            String hvs = "";
            stats += "Router " + i + " Hop Vector, NextVector: ";
            for(int j =0; j < dv.length; j++){
                stats += Integer.toString(hv[j]) + "," + Integer.toString(nv[j]) + "\t";
                dvs += (Integer.toString(dv[j])) + " ";
                hvs += (Integer.toString(hv[j])) + " ";
                nvs += (Integer.toString(nv[j])) + " ";
            }
            stats += "\n";

            // stats += "Router " + i + " Distance Vector: " + dvs + " Hop Vector: " + hvs + ", Next Vector: " + nvs + "\n";
        }
        return stats;
    }


    public ArrayList<Router> getRouters(){
        return routers;
    }


}
