package tests;

import elements.Robot;
import org.junit.Test;
import utils.Point3D;

import static org.junit.Assert.*;

public class RobotTest {
    static String JSONSTRING = "{\"Robot\":{\"id\":0,\"value\":5,\"src\":7,\"dest\":6,\"speed\":1,\"pos\":\"35.4,32.3,0.0\"}}";
    @Test
    public void setId() {
        Robot r1 = new Robot(0, 0, 0, 0, null);
        Robot r2 = new Robot(0, 0, 0, 0, null);
        Robot r3 = new Robot(0, 0, 0, 0, null);
        r1.setId(1);
        r2.setId(2);
        r3.setId(3);
        assertEquals(3,r3.getId());
        assertEquals(2,r2.getId());
        assertEquals(1,r1.getId());
    }

    @Test
    public void getId() {
        Robot r1 = new Robot(0, 0, 0, 0, null);
        Robot r2 = new Robot(0, 0, 0, 0, null);
        Robot r3 = new Robot(0, 0, 0, 0, null);
        r1.setId(1);
        r2.setId(2);
        r3.setId(3);
        assertEquals(3,r3.getId());
        assertEquals(2,r2.getId());
        assertEquals(1,r1.getId());
    }

    @Test
    public void setPos() {
        Robot r1 = new Robot(0, 0, 0, 0, null);
        Robot r2 = new Robot(0, 0, 0, 0, null);
        Robot r3 = new Robot(0, 0, 0, 0, null);
        Point3D p1 = new Point3D(1,2,0);
        Point3D p2 = new Point3D(2,4,0);
        Point3D p3 = new Point3D(5,6,0);
        r1.setPos(p1);
        r2.setPos(p2);
        r3.setPos(p3);
        assertEquals(p3,r3.getPos());
        assertEquals(p2,r2.getPos());
        assertEquals(p1,r1.getPos());
    }


    @Test
    public void getPos() {
        Robot r1 = new Robot(0, 0, 0, 0, null);
        Robot r2 = new Robot(0, 0, 0, 0, null);
        Robot r3 = new Robot(0, 0, 0, 0, null);
        Point3D p1 = new Point3D(1,2,0);
        Point3D p2 = new Point3D(2,4,0);
        Point3D p3 = new Point3D(5,6,0);
        r1.setPos(p1);
        r2.setPos(p2);
        r3.setPos(p3);
        assertEquals(p3,r3.getPos());
        assertEquals(p2,r2.getPos());
        assertEquals(p1,r1.getPos());
    }


    @Test
    public void setSrc() {
        Robot r1 = new Robot(0, 0, 0, 0, null);
        Robot r2 = new Robot(0, 0, 0, 0, null);
        Robot r3 = new Robot(0, 0, 0, 0, null);
        r1.setSrc(1);
        r2.setSrc(2);
        r3.setSrc(3);
        assertEquals(3,r3.getSrc());
        assertEquals(2,r2.getSrc());
        assertEquals(1,r1.getSrc());
    }

    @Test
    public void getSrc() {
        Robot r1 = new Robot(0, 0, 0, 0, null);
        Robot r2 = new Robot(0, 0, 0, 0, null);
        Robot r3 = new Robot(0, 0, 0, 0, null);
        r1.setSrc(1);
        r2.setSrc(2);
        r3.setSrc(3);
        assertEquals(3,r3.getSrc());
        assertEquals(2,r2.getSrc());
        assertEquals(1,r1.getSrc());
    }

    @Test
    public void setValue() {
        Robot r1 = new Robot(0, 0, 0, 0, null);
        Robot r2 = new Robot(0, 0, 0, 0, null);
        Robot r3 = new Robot(0, 0, 0, 0, null);
        r1.setValue(1.0);
        r2.setValue(2.0);
        r3.setValue(3.0);
        assertEquals(3.0,r3.getValue(),0.0001);
        assertEquals(2.0,r2.getValue(), 0.0001);
        assertEquals(1.0,r1.getValue(), 0.0001);
    }

    @Test
    public void getValue() {
        Robot r1 = new Robot(0, 0, 0, 0, null);
        Robot r2 = new Robot(0, 0, 0, 0, null);
        Robot r3 = new Robot(0, 0, 0, 0, null);
        r1.setValue(1.0);
        r2.setValue(2.0);
        r3.setValue(3.0);
        assertEquals(3.0,r3.getValue(),0.0001);
        assertEquals(2.0,r2.getValue(), 0.0001);
        assertEquals(1.0,r1.getValue(), 0.0001);
    }


}