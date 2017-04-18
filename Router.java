import java.util.ArrayList;

/**
 * Created by ralfpopescu on 4/12/17.
 */
public class Router {

    ArrayList<Router> neighbors = new ArrayList<Router>();
    //ArrayList<Link> links = new ArrayList<Link>();
    int num;
    int numOfRouters;
    int[] distanceVector;
    int[] nextVector;
    int[][] routingTable;
    Network network;


    public Router(int num, int numOfRouters, Network network){
        this.num = num;
        this.numOfRouters = numOfRouters;
        this.network = network;

        routingTable = new int[numOfRouters][numOfRouters];
        distanceVector = new int[numOfRouters];
        nextVector = new int[numOfRouters];

        for(int i = 0; i < numOfRouters; i++){
            distanceVector[i] = 999999;
        }
    }

    public void addNeighbor(Router routerToAdd){
        neighbors.add(routerToAdd);

    }


    public ArrayList<Router> getNeighbors(){
        ArrayList<Router> routers = network.getRouters();
        ArrayList<Integer> neighborNums = network.getNeighbors(num);
        ArrayList<Router> neighbors = new ArrayList<Router>();

        for(int x: neighborNums) {
            neighbors.add(routers.get(x));
        }

        return neighbors;

    }

    public int getNum(){
        return num;
    }

    public boolean propogateVector(){
        boolean changed = false;
        ArrayList<Router> neighbors = getNeighbors();

        for(Router neighbor: neighbors){
            //System.out.println("Router num: " + num + " Neighbor: " + neighbor.getNum());
            changed = changed || neighbor.updateDistanceVectorWithAnotherVector(distanceVector, num);
        }

        return changed;
    }

    public boolean propogateSplitHorizon(){
        boolean changed = false;
        ArrayList<Router> neighbors = getNeighbors();

        for(Router neighbor: neighbors){
            //System.out.println("Router num: " + num + " Neighbor: " + neighbor.getNum());
            changed = changed || neighbor.splitHorizonUpdate(distanceVector, nextVector, num);
        }

        return changed;
    }

    public boolean propogatePoison(){
        boolean changed = false;
        ArrayList<Router> neighbors = getNeighbors();

        for(Router neighbor: neighbors){
            //System.out.println("Router num: " + num + " Neighbor: " + neighbor.getNum());
            changed = changed || neighbor.poisonUpdate(distanceVector, nextVector, num);
        }

        return changed;
    }

    public boolean updateDistanceVectorWithAnotherVector(int[] otherVector, int routerID){
        boolean changed = false;
        for (int i = 0; i < numOfRouters; i++) {

            if(i == num){
                continue;
            }

            int originalCostToI = distanceVector[i];
            int newCostToI = distanceVector[routerID] + otherVector[i];

            System.out.println("Router " + num + " Distance to " + i + ": " + originalCostToI + ", Distance from Router " + routerID + ": " + otherVector[i]);
            if(newCostToI < originalCostToI) {
                System.out.println("updated");
                distanceVector[i] = newCostToI;
                nextVector[i] = routerID;
                changed = true;
            }
        }
        return changed;
    }

    public boolean splitHorizonUpdate(int[] otherVector, int[] nextVector, int routerID){
        boolean changed = false;
        for (int i = 0; i < numOfRouters; i++) {

            if(i == num){
                continue;
            }

            int originalCostToI = distanceVector[i];
            int newCostToI = distanceVector[routerID] + otherVector[i];

            System.out.println("Router " + num + " Distance to " + i + ": " + originalCostToI + ", Distance from Router " + routerID + ": " + otherVector[i]);
            if(newCostToI < originalCostToI) {
                System.out.println("updated");
                distanceVector[i] = newCostToI;
                nextVector[i] = routerID;
                changed = true;
            }
        }
        return changed;
    }

    public boolean poisonUpdate(int[] otherVector, int[] nextVector, int routerID){
        boolean changed = false;
        for (int i = 0; i < numOfRouters; i++) {

            if(i == num){
                continue;
            }

            int originalCostToI = distanceVector[i];
            int newCostToI = distanceVector[routerID] + otherVector[i];

            System.out.println("Router " + num + " Distance to " + i + ": " + originalCostToI + ", Distance from Router " + routerID + ": " + otherVector[i]);
            if(newCostToI < originalCostToI) {
                System.out.println("updated");
                distanceVector[i] = newCostToI;
                nextVector[i] = routerID;
                changed = true;
            }
        }
        return changed;
    }

    public void initDistanceVector(){
        int[][] adjMatrix = network.getAdjMatrix();

        for (int i=0;i<numOfRouters;i++){
            if (adjMatrix[num][i] != -1){
                distanceVector[i] = adjMatrix[num][i];
                nextVector[i] = adjMatrix[num][i];
            }
        }
        distanceVector[num] = 0;
    }

    public int[] getDistanceVector(){
        return distanceVector;
    }





}
