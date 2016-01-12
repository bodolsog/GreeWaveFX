package pl.bodolsog.GreenWaveFX.model;

import netscape.javascript.JSObject;

import java.util.ArrayList;

/**
 * Created by bodolsog on 11.01.16.
 */
public class WayTest {
    private Ways ways;
    private ArrayList<Way> wayArr;
    private ArrayList<Marker> markerArr;
    private JSObject jsO;

//    /**
//     * JSObject adapter for test.
//     */
//    public class JSObjectTest extends JSObject {
//        @Override public Object call(String s, Object... objects) throws JSException { return null; }
//        @Override public Object eval(String s) throws JSException { return null; }
//        @Override public Object getMember(String s) throws JSException { return null; }
//        @Override public void setMember(String s, Object o) throws JSException {}
//        @Override public void removeMember(String s) throws JSException {}
//        @Override public Object getSlot(int i) throws JSException { return null; }
//        @Override public void setSlot(int i, Object o) throws JSException {}
//    }
//
//    @Before
//    public void setUp(){
//
//        jsO = new JSObjectTest();
//        int i;
//        ways = new Ways();
//
//        for(i = 0; i < 2; i++){
//            markerArr.add(new Marker(i, jsO));
//        }
//    }
}