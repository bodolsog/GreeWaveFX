package pl.bodolsog.GreenWaveFX.crossroads;

import java.util.ArrayList;

/**
 * Created by bodolsog on 04.12.15.
 */
public class ThreeWayCrossroad extends Crossroad {
    private ArrayList<ArrayList<ArrayList<Boolean>>> states = new ArrayList<>();

    /*
     false = red light
     true = green light

      2
    0-|
      1

      state0: 1:2, 2:01
      state1: 1:02, 0:1
      state2: 0:21, 2:0
    */

    public ThreeWayCrossroad(){
        createStates();
    }

    private void createStates(){
        // State 0
        states.add(new ArrayList<>());
        createStateRoads(0);
        setStates(0);
        // State 1
        states.add(new ArrayList<>());
        createStateRoads(1);
        setStates(1);
        // State 2
        states.add(new ArrayList<>());
        createStateRoads(2);
        setStates(2);
    }

    private void createStateRoads(int state){
        // Road 0
        states.get(state).add(new ArrayList<>());
        createStateRoadsWays(state, 0);
        // Road 1
        states.get(state).add(new ArrayList<>());
        createStateRoadsWays(state, 1);
        // Road 2
        states.get(state).add(new ArrayList<>());
        createStateRoadsWays(state, 2);
    }

    private void createStateRoadsWays(int state, int road){
        states.get(state).get(road).add(false);
        states.get(state).get(road).add(false);
        states.get(state).get(road).add(false);
    }

    private void setNewState(int state, int from, boolean a, boolean b, boolean c){
        states.get(state).get(from).set(0, a);
        states.get(state).get(from).set(1, b);
        states.get(state).get(from).set(2, c);
    }

    private void setStates(int state){
        switch(state){
            case 0:
                setNewState(state, 0, false, false, false);
                setNewState(state, 1, false, false, true);
                setNewState(state, 2, true, true, false);
                break;
            case 1:
                setNewState(state, 0, false, true, false);
                setNewState(state, 1, true, false, true);
                setNewState(state, 2, false, false, false);
                break;
            case 2:
                setNewState(state, 0, false, true, true);
                setNewState(state, 1, false, false, false);
                setNewState(state, 2, true, false, false);
                break;
        }

    }
}
