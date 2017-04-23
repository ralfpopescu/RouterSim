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
    int[] hopVector;
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
        hopVector = new int[numOfRouters];

        for(int i = 0; i < numOfRouters; i++){
            distanceVector[i] = INFINITY;
            nextVector[i] = -1;
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
            changed = changed || neighbor.updateDistanceVectorWithAnotherVector(distanceVector, hopVector, num);
        }

        return changed;
    }

    public boolean propogateSplitHorizon(){
      boolean changed = false;
      ArrayList<Router> neighbors = getNeighbors();

      for(Router neighbor: neighbors){
          //System.out.println("Router num: " + num + " Neighbor: " + neighbor.getNum())
          int[] distV = new int[numOfRouters];
          distV = distanceVector.clone();
          for (int i = 0; i < numOfRouters; i++) {
            distV[i] = nextVector[i] == neighbor.getNum() ? -1 : distanceVector[i];
          }
          // System.out.println(printDistanceRouter(distV));

          changed = changed || neighbor.updateDistanceVectorWithAnotherVector(distV, hopVector, num);
      }

      return changed;
    }

    public boolean propogatePoison(){
      boolean changed = false;
      ArrayList<Router> neighbors = getNeighbors();

      for(Router neighbor: neighbors){
          //System.out.println("Router num: " + num + " Neighbor: " + neighbor.getNum())
          int[] distV = new int[numOfRouters];
          distV = distanceVector.clone();
          for (int i = 0; i < numOfRouters; i++) {
            distV[i] = nextVector[i] == neighbor.getNum() ? INFINITY : distanceVector[i];
          }
          // System.out.println(printDistanceRouter(distV));

          changed = changed || neighbor.updateDistanceVectorWithAnotherVector(distV, hopVector, num);
      }

      return changed;
    }

    public boolean updateDistanceVectorWithAnotherVector(int[] otherVector, int[] otherHopVector, int routerID){
        boolean changed = false;
        int[] oldHopVector = hopVector.clone();
        // distanceVector = initDistanceVector();
        for (int i = 0; i < numOfRouters; i++) {

            if(i == num){
                continue;
            }

            int originalCostToI = distanceVector[i];
            int newCostToI = network.getAdjMatrix()[num][routerID] + otherVector[i];

            //System.out.println("Router " + num + " Distance to " + i + ": " + originalCostToI + ", Distance from Router " + routerID + ": " + otherVector[i]);
            if(newCostToI < originalCostToI || (newCostToI > originalCostToI && routerID == nextVector[i])) {
                //System.out.println("updated");
                distanceVector[i] = newCostToI;
                nextVector[i] = routerID;
                hopVector[i] = 1 + otherHopVector[i];
                changed = true;
            }
        }
        return changed;
    }

    public boolean splitHorizonUpdate(int[] otherVector, int[] otherHopVector, int[] nextVector, int routerID){
        boolean changed = false;
        int[] oldHopVector = hopVector.clone();

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
                hopVector[i] = oldHopVector[i] + otherHopVector[i];
                changed = true;
            }
        }
        return changed;
    }

    public boolean poisonUpdate(int[] otherVector, int[] otherHopVector, int[] nextVector, int routerID){
        boolean changed = false;
        int[] oldHopVector = hopVector.clone();

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
                hopVector[i] = oldHopVector[i] + otherHopVector[i];
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
                hopVector[i] = 1;
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
    public int[] getHopVector(){
        return hopVector;
    }
    public boolean eventUpdate(Event e, int r){
      int oldNext = nextVector[r];
      ArrayList<Router> neigh = getNeighbors();
      int[] distVect, nextVect;
      int shortestDist = e.getCost();
      nextVector[r] = r;
      hopVector[r] = 1;
      if (neigh.size() == 0) {
        distanceVector[r] = INFINITY;
        nextVector[r] = -1;
        hopVector[r] = INFINITY;
      }
      for(Router s: neigh) {
        distVect = s.getDistanceVector();
        nextVect = s.getNextVector();
        int newDist = distVect[r] + network.getAdjMatrix()[num][s.getNum()];
        System.out.println("Distance through " + s.getNum() + ": " + newDist);
        if (newDist < shortestDist || shortestDist == -1) {
          distanceVector[r] = newDist;
          nextVector[r] = s.getNum();
          hopVector[r] = 1 + s.getHopVector()[r];
        }
      }
      return nextVector[r] == oldNext;

      // return changed;

    }
    public String printDistanceRouter() {
      String print = "Distance Vector of Router: " + num + "<";
      for (int i = 0; i <numOfRouters; i++ ) {
        print += "(" +i+","+distanceVector[i]+"),";
      }
      print += ">";
      return print;
    }
    public String printDistanceRouter(int[] vector) {
      String print = "Distance Vector of Router: " + num + "<";
      for (int i = 0; i <vector.length; i++ ) {
        print += "(" +i+","+vector[i]+"),";
      }
      print += ">";
      return print;
    }







}
