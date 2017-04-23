/**
 * Created by ralfpopescu on 4/16/17.
 */
public class Event {

    int round;
    int router1;
    int router2;
    int cost;

    public Event(int round, int r1, int r2, int cost){
        this.round = round;
        this.router1 = r1;
        this.router2 = r2;
        this.cost = cost;
    }

    public Event(String s){
        String[] split = s.split("\\s+");
        int round = Integer.parseInt(split[0]);
        int r1 = Integer.parseInt(split[1]);
        int r2 = Integer.parseInt(split[2]);
        int cost = Integer.parseInt(split[3]);

        r1 -= 1; //compensate for 1 indexing
        r2 -= 1;

        this.round = round;
        this.router1 = r1;
        this.router2 = r2;
        this.cost = cost;

    }

    public int getRound(){
        return round;
    }

    public int getCost(){
        return cost;
    }

    public int getRouter1(){
        return router1;
    }

    public int getRouter2(){
        return router2;
    }

}
