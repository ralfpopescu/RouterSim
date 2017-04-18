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

        adjMatrix[r1][r2] = cost;
        adjMatrix[r2][r1] = cost;
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

    public String stats(){
        String stats = "";
        for(int i=0;i<numOfRouters;i++){
            int[] dv = routers.get(i).getDistanceVector();
            String dvs = "";
            for(int j =0; j < dv.length; j++){
                dvs += (Integer.toString(dv[j])) + " ";
            }
            stats += "Router " + i + " Distance Vector: " + dvs + "\n";
        }
        return stats;
    }


    public ArrayList<Router> getRouters(){
        return routers;
    }


}
