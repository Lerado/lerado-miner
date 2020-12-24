package cm.uy1.datamining.dataminers.datastructures;
import java.util.ArrayList;

import java.io.Serializable;

public class Item implements Comparable<Item>,Serializable{
	
	private static final long serialVersionUID = 2750302191166255552L;
	protected String label;
	protected int position;
	
	public Item(String itemLabel) {
		
		label = itemLabel;
	}
	
	public Item(String itemLabel, int itsPosition) {
		
		label = itemLabel;
		position = itsPosition;
	}
	
	public String getName() {
		
		return this.label;
	}
	
	public int getPosition() {
		return position;
	}

	public void setName(String newItemLabel) {
		
		label = newItemLabel;
	}
	
	public String toString() {
		
		return label;
	}
	
	public int compareTo(Item anotherItem) {
		
		return label.compareTo(anotherItem.label);
	}
	
	@Override
	public boolean equals(Object anotherItem) {
		
		return this.compareTo((Item)anotherItem) == 0;
	}
	
	public int positionIn(ArrayList<Item> list) {
		
		int position = -1;
		
		for(int i = 0; i<list.size(); i++)
			if(list.get(i) == this)
				position = i;
		
		return position;
	}

}
