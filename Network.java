/**
 * Created by ralfpopescu on 4/12/17.
 */

import java.util.ArrayList;

public class Network {

    ArrayList<Router> routers = new ArrayList<Router>();
    ArrayList<Link> links = new ArrayList<Link>();
    ArrayList<ArrayList<Integer>> adjMatrix = new ArrayList<ArrayList<Integer>>();

    public Network(){

    }


    public void addRouter(Router r){
        routers.add(r);
    }


    public void initializeNetwork(ArrayList<String> input){

        for(int i = 0; i < Integer.parseInt(input.get(0)); i++){
            addRouter(new Router(i));
        }

        for (int j = 1; j < input.size(); j++){
            addLink(input.get(j));
        }

    }

    public void addLink(String link){
        String[] split = link.split("\\s+");
        int r1 = Integer.parseInt(split[0]);
        int r2 = Integer.parseInt(split[1]);
        int cost = Integer.parseInt(split[2]);

        Link l = new Link(getRouter(r1), getRouter(r2), cost);
        links.add(l);

        Router rtr1 = getRouter(r1);
        Router rtr2 = getRouter(r2);

        rtr1.addLink(l);
        rtr2.addLink(l);
        rtr1.addNeighbor(rtr2);
        rtr2.addNeighbor(rtr1);

    }

    public boolean removeRouter(int rem){
        return false;
    }

    public boolean removeLink(){
        return false;
    }

    public Router getRouter(int x){

        for(Router r: routers){
            if (r.getNum() == x){
                return r;
            }
        }
        return null;
    }

    public void makeAdjMatrix(){
        for(Link l: links){
            int r1 = l.getRouter1().getNum();
            int r2 = l.getRouter2().getNum();
            int cost = l.getCost();
        }
    }

    public ArrayList<Router> getRouters(){
        return routers;
    }

    public ArrayList<Link> getLinks(){
        return links;
    }
}
