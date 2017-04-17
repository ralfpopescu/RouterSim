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
    int[][] routingTable;
    Network network;


    public Router(int num, int numOfRouters, Network network){
        this.num = num;
        this.numOfRouters = numOfRouters;
        this.network = network;

        routingTable = new int[numOfRouters][numOfRouters];
        distanceVector = new int[numOfRouters];

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
            changed = changed || neighbor.updateDistanceVectorWithAnotherVector(distanceVector);
        }

        return changed;
    }

    public boolean updateDistanceVectorWithAnotherVector(int[] otherVector){
        boolean changed = false;
        for (int i = 0; i < numOfRouters; i++){
            int originalCostToI = distanceVector[i];
            int newCostToI = distanceVector[i] + otherVector[i];
            if(newCostToI < originalCostToI){
                distanceVector[i] = newCostToI;
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
            }
        }
    }

    public int[] getDistanceVector(){
        return distanceVector;
    }





}
