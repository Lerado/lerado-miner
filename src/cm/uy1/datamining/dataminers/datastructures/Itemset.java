package cm.uy1.datamining.dataminers.datastructures;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import java.io.Serializable;

import cm.uy1.arrays.ArraySorting;

public class Itemset implements Comparable<Itemset>, Serializable{
	
	private static final long serialVersionUID = -5411523906962900520L;
	protected ArrayList<Item> items;
	protected int size;
	protected int support;
	
	public Itemset() {
		
		items = new ArrayList<Item>();
		size  = 0;
		support = 0;
	}
	
	public Itemset(Item... aListOfItems) {
		
		items = new ArrayList<Item>();
		ArraySorting.quickSort(aListOfItems);
		
		for(int i = 0; i<aListOfItems.length; i++)
			items.add(aListOfItems[i]);
		
		size  = items.size();
		support = 0;
	}
	
	public Itemset(Collection<? extends Item> set) {
		
		items = new ArrayList<Item>(set);
		items.sort(null);
		size  = items.size();
		support = 0;
	}
	
	public Itemset(Itemset anotherItemset) {
		
		items = new ArrayList<Item>(anotherItemset.items);
		items.sort(null);
		size  = items.size();
		support = anotherItemset.getSupport();
	}
	
	protected void resize() {
		
		size = items.size();
	}
	
	public Item get(int index) {
		
		return items.get(index);
	}
	
	public ArrayList<Item> items() {
		
		return items;
	}
	
	public ArrayList<String> itemsToString() {
		
		ArrayList<String> temp = new ArrayList<String>();
		for(Item i : items)
			temp.add(i.toString());
		
		return temp;
	}
	
	public int support() {
		
		return getSupport();
	}
	
	public int size() {
		
		return size;
	}
	
	public void set(int index, Item newItem) {
		
		items.set(index, newItem);
	}
	
	public int getSupport() {
		return support;
	}

	public void setSupport(int newSupport) {
		
		support = newSupport;
	}
	
	public void add(Item anItem) {
		
		items.add(anItem);
		items.sort(null);
		resize();
	}
	
	public void addAll(Collection<? extends Item> set) {
		
		items.addAll(set);
		items.sort(null);
		resize();
	}
	
	public void addAll(Itemset set) {
		
		items.addAll(set.items);
		items.sort(null);
		resize();
	}
	
	public void remove(Item anItem) {
		
		items.remove(anItem);
		resize();
		
	}
	
	public void removeAll(Collection<? extends Item> set) {
		
		items.removeAll(set);
		resize();
		
	}
	
	public void removeAll(Itemset set) {
		
		items.removeAll(set.items);
		resize();
		
	}
	
	public void increment() {
		
		support = getSupport() + 1;
	}
	
	public void reset() {
		
		support = 0;
	}
	
	public String toString() {
		
		return items.toString()+": "+String.valueOf(getSupport());
	}
	
	public String toStringIgnoringSupport() {
		
		return items.toString();
	}
	
	public boolean[] toBooleanArray(ArrayList<Item> items_) {
		
		boolean[] booleanArray = new boolean[items_.size()];
		Arrays.fill(booleanArray, false);
		
		for(Item i : items)
			booleanArray[items_.indexOf(i)] = true;
		
		return booleanArray;
	}
	
	public boolean equals(Itemset anotherItemset) {
		
		return anotherItemset.items.containsAll(this.items) 
				&& this.size == anotherItemset.size
				&& this.getSupport() == anotherItemset.size;
	}
	
	public boolean equalsIgnoringSupport(Itemset anotherItemset) {
		
		return anotherItemset.items.containsAll(this.items) 
				&& this.size == anotherItemset.size;
	}
	
	public static boolean isIn(Itemset a, ArrayList<Itemset> set) {
		
		boolean check = false;
		
		for(Itemset i : set) {
			check = a.equals(i);
			if(check)
				break;
		}
		
		return check;
	}
	
	public ArrayList<Itemset> listItemsetsOfLowerSize() {
		
		ArrayList<Itemset> result = new ArrayList<Itemset>();
		int skipper = this.size() - 1;
		
		while(skipper >= 0) {
			Itemset subItemset = new Itemset();
			for(int i = 0; i<this.size(); i++)
				if(i != skipper)
					subItemset.add(this.get(i));
			result.add(subItemset);
			skipper--;
		}
		
		return result;
	}
	
	public boolean contains(Item item) {
		
		return items.contains(item);
	}
	
	public boolean contains(Itemset itemset) {

		return items.containsAll(itemset.items);
	}

	@Override
	public int compareTo(Itemset o) {
		
		return ((Integer)this.getSupport()).compareTo(o.getSupport());
	}

}
