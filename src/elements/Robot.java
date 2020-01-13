package elements;

import Server.robot;
import utils.Point3D;

public class Robot {
	
	private int speed, src, dest, id;
	private double value;
	private Point3D pos;
	
	public Robot(int speed, int src, int dest, int id, Point3D pos) {
		this.speed = speed;
		this.src = src;
		this.dest = dest;
		this.id = id;
		this.value = 0;
		this.pos = pos;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public int getSrc() {
		return src;
	}
	public void setSrc(int src) {
		this.src = src;
	}
	public int getDest() {
		return dest;
	}
	public void setDest(int dest) {
		this.dest = dest;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public Point3D getPos() {
		return pos;
	}
	public void setPos(Point3D pos) {
		this.pos = pos;
	}


	
	
	

}
