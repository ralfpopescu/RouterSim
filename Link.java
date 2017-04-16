import java.util.ArrayList;

/**
 * Created by ralfpopescu on 4/12/17.
 */
public class Link {

    Router A;
    Router B;
    ArrayList<Router> routers = new ArrayList<Router>();
    int cost;

    public Link(Router one, Router two, int cost){
        this.cost = cost;
        A = one;
        B = two;
        routers.add(A);
        routers.add(B);
    }

    public ArrayList<Router> getRouters(){
        return routers;
    }

    public Router getRouter1(){
        return A;
    }

    public Router getRouter2(){
        return B;
    }

    public int getCost(){
        return cost;
    }


}
