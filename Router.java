import java.util.ArrayList;

/**
 * Created by ralfpopescu on 4/12/17.
 */
public class Router {

    ArrayList<Router> neighbors = new ArrayList<Router>();
    ArrayList<Link> links = new ArrayList<Link>();
    int num;
    ArrayList<Integer> distanceVector = new ArrayList<Integer>();


    public Router(int num){
        this.num = num;
    }

    public void addNeighbor(Router routerToAdd){
        neighbors.add(routerToAdd);

    }

    public boolean removeNeighbor(int routerToRemove){
        for (int i = 0; i < neighbors.size(); i++) {
            if (neighbors.get(i).getNum() == i){
               neighbors.remove(i);
                return true;
            }
        }
        return false;
    }

    public int getNum(){
        return num;
    }

    public ArrayList<Router> getNeighbors(){
        return neighbors;

    }

    public void addLink(Link l){
        links.add(l);
    }

    public void invalidate(){
        num = -1;
    }


}
