import java.util.ArrayList;

/**
 * Created by ralfpopescu on 4/12/17.
 */
public class Router {
    final int INFINITY = 999999;
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
            distanceVector[i] = INFINITY;
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
        int[] oldDistVector = distanceVector;
        // distanceVector = initDistanceVector();
        for (int i = 0; i < numOfRouters; i++) {

            if(i == num){
                continue;
            }

            int originalCostToI = distanceVector[i];
            int newCostToI = distanceVector[routerID] + otherVector[i];

            //System.out.println("Router " + num + " Distance to " + i + ": " + originalCostToI + ", Distance from Router " + routerID + ": " + otherVector[i]);
            if(newCostToI < originalCostToI) {
                //System.out.println("updated");
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
            int nextHop = nextVector[i];

            //System.out.println("Router " + num + " Distance to " + i + ": " + originalCostToI + ", Distance from Router " + routerID + ": " + otherVector[i]);
            if(newCostToI < originalCostToI) {
                if(nextHop == num){ //skip if next hop
                    break;
                }
                //System.out.println("updated");
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
            int nextHop = nextVector[i];
            int newCostToI = distanceVector[routerID] + otherVector[i];
            if(nextHop == num){
                originalCostToI = Integer.MAX_VALUE;
            }


            //System.out.println("Router " + num + " Distance to " + i + ": " + originalCostToI + ", Distance from Router " + routerID + ": " + otherVector[i]);
            if(newCostToI < originalCostToI) {
                //System.out.println("updated");
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
                nextVector[i] = i;
            }
        }
        distanceVector[num] = 0;
    }

    public int[] getDistanceVector(){
        return distanceVector;
    }

    public int[] getNextVector(){
        return nextVector;
    }
    public boolean eventUpdate(Event e, int r){
      int oldNext = nextVector[r];
      ArrayList<Router> neigh = getNeighbors();
      int[] distVect, nextVect;
      int shortestDist = e.getCost();
      nextVector[r] = r;
      for(Router s: neigh) {
        distVect = s.getDistanceVector();
        nextVect = s.getNextVector();
        int newDist = distVect[r] + network.getAdjMatrix()[num][s.getNum()];
        if (newDist < shortestDist || shortestDist == -1) {
          distanceVector[r] = newDist;
          nextVector[r] = s.getNum();
        }
      }
      return nextVector[r] == oldNext;

      // return changed;

    }







}
