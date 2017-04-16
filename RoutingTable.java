/**
 * Created by ralfpopescu on 4/16/17.
 */

import java.util.ArrayList;

public class RoutingTable {

    ArrayList<ArrayList<Integer>> table = new ArrayList<ArrayList<Integer>>();


    public RoutingTable(int n){
        for(int i = 0; i < n; i++)  {
            table.add(new ArrayList<Integer>());
        }
    }


}
