package pl.bodolsog.GreenWaveFX.crossroads;

import java.util.ArrayList;

/**
 * Created by bodolsog on 04.12.15.
 */
public class FourWayCrossroad {
    private ArrayList<ArrayList<ArrayList<Boolean>>> states = new ArrayList<>();

    /*
     false = red light
     true = green light

      0
    1-|-3
      2

      state0: 0:12, 2:03
      state1: 0:3, 2:1
      state2: 1:23, 3:01
      state3: 1:0, 3:2
    */

    public FourWayCrossroad(){
        createStates();
    }

    private void createStates(){
        // State 0
        states.add(new ArrayList<>());
        createStateRoads(0);
        setStates(1);
        // State 1
        states.add(new ArrayList<>());
        createStateRoads(1);
        setStates(2);
        // State 2
        states.add(new ArrayList<>());
        createStateRoads(2);
        setStates(3);
        // State 3
        states.add(new ArrayList<>());
        createStateRoads(3);
        setStates(3);
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
        // Road 3
        states.get(state).add(new ArrayList<>());
        createStateRoadsWays(state, 3);
    }

    private void createStateRoadsWays(int state, int road){
        states.get(state).get(road).add(false);
        states.get(state).get(road).add(false);
        states.get(state).get(road).add(false);
    }

    private void setNewState(int state, int from, boolean a, boolean b, boolean c, boolean d){
        states.get(state).get(from).set(0, a);
        states.get(state).get(from).set(1, b);
        states.get(state).get(from).set(2, c);
        states.get(state).get(from).set(3, d);
    }

    private void setStates(int state){
        switch(state){
            case 0:
                setNewState(state, 0, false, true, true, false);
                setNewState(state, 1, false, false, false, false);
                setNewState(state, 2, true, false, false, true);
                setNewState(state, 3, false, false, false, false);
                break;
            case 1:
                setNewState(state, 0, false, false, true, false);
                setNewState(state, 1, false, false, false, false);
                setNewState(state, 2, false, true, false, false);
                setNewState(state, 3, false, false, false, false);
                break;
            case 2:
                setNewState(state, 0, false, false, false, false);
                setNewState(state, 1, false, false, true, true);
                setNewState(state, 2, true, true, false, false);
                setNewState(state, 3, false, false, false, false);
                break;
            case 3:
                setNewState(state, 0, false, false, false, false);
                setNewState(state, 1, true, false, false, false);
                setNewState(state, 2, false, false, false, false);
                setNewState(state, 3, false, false, true, false);
                break;
        }

    }
}

