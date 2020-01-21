package elements;

import java.util.Comparator;

import elements.Fruit;

public class Fruit_Comperator implements Comparator<Fruit> {

	public Fruit_Comperator() {;}
	
	@Override
	public int compare(Fruit o1, Fruit o2) {
		double dp = o2.getValue() - o1.getValue();
		return (int) dp;
	
	}

}
