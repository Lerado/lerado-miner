package cm.uy1.datamining.dataminers;
import java.util.ArrayList;
import java.util.List;

import cm.uy1.datamining.dataminers.datastructures.Item;
import cm.uy1.datamining.dataminers.datastructures.Itemset;

import java.util.Arrays;

public class Utils {
	
	public static<T extends List<String>> boolean isIncludedInArrayList(T v1, T v2) {
		
		boolean tester = true;
		
		for(String s : v1)
			tester = tester && v2.contains(s);
		
		return tester && v2.size() >= v1.size();
	}
	
	public static boolean itemsetEquals(ArrayList<String> itemset,ArrayList<String> anotherItemset) {
		
		boolean checker = true;
		
		for(String s : itemset)
			checker = checker && anotherItemset.contains(s);
		
		return checker && itemset.size() == anotherItemset.size();
	}
	
	public static boolean isInSetOfItemsets(ArrayList<String> itemset, ArrayList<ArrayList<String>> set) {
		
		boolean checker = false;
		
		for(ArrayList<String> anItemset : set) {
			checker = itemsetEquals(itemset,anItemset);
			if(checker)
				break;
		}
		
		return checker;
	}
	
	public static ArrayList<ArrayList<String>> listSubsetsOfLowerSize(ArrayList<String> anItemset) {
		
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		int skipper = anItemset.size() - 1;
		
		while(skipper >= 0) {
			ArrayList<String> subItemset = new ArrayList<String>();
			for(int i = 0; i<anItemset.size(); i++)
				if(i != skipper)
					subItemset.add(anItemset.get(i));
			result.add(subItemset);
			skipper--;
		}
		
		return result;
	}
	
	public static ArrayList<ArrayList<String>> listSingletonsOfItemset(ArrayList<String> anItemset) {
		
		ArrayList<ArrayList<String>> singletons = new ArrayList<ArrayList<String>>();
		
		for(int i = 0; i<anItemset.size()-1; i++) {
			ArrayList<String> aSingleton = new ArrayList<String>();
			aSingleton.add(anItemset.get(i));
			singletons.add(aSingleton);
		}
		
		return singletons;
	}
	
	public static int readSupport(ArrayList<String> anItemset) {
		
		int support = Integer.valueOf(anItemset.get(anItemset.size()-1)).intValue();
		return support;
	}
	
	public static ArrayList<String> sub(ArrayList<String> itemset, ArrayList<String> anotherItemset) {

		ArrayList<String> result = new ArrayList<String>(itemset);
		result.remove(result.get(result.size()-1));
		
		for(int i = 0; i<anotherItemset.size()-1; i++)
			result.remove(anotherItemset.get(i));
		
		return result;
		
	}
	
	public static ArrayList<Item> subItemsets(ArrayList<Item> itemset, ArrayList<Item> anotherItemset) {

		ArrayList<Item> result = new ArrayList<Item>(itemset);
		
		for(int i = 0; i<anotherItemset.size(); i++)
			result.remove(anotherItemset.get(i));
				
		return result;
		
	}
	
	public static boolean[] lineToBooleanArray(ArrayList<String> line, ArrayList<Item> items) {

		boolean[] booleanArray = new boolean[items.size()];
		Arrays.fill(booleanArray, false);
		
		for(String s : line) {
			Item item = new Item(s);
			booleanArray[items.indexOf(item)] = true;
		}
		
		return booleanArray;
	}
	
	public static boolean[][] toBooleanArray(ArrayList<ArrayList<String>> matrix, ArrayList<Item> items) {
		
		boolean[][] booleanArray = new boolean[matrix.size()][items.size()];
		int i = 0;
		
		for(ArrayList<String> line : matrix)
			booleanArray[i++] = lineToBooleanArray(line,items);
		
		return booleanArray;
	}
	
	public static boolean searchItemsetIn(Itemset itemset, ArrayList<Item> items, boolean[] transaction) {
		
		boolean match = true;
		
		for(int i = 0; i<itemset.size(); i++) {
			match = transaction[itemset.get(i).getPosition()];
			if(!match)
				break;
		}
		
		return match;
	}
	
	public static boolean searchIn(boolean[] itemset, boolean[] transaction) {
		
		int i = 0;
		boolean match = true;
		
		while(i<itemset.length && match) {
			if(itemset[i])
				match = transaction[i];
			i++;
			
		}
		
		return match;
	}
	
	public static boolean searchIn(boolean[] itemset, boolean[][] transactions) {
		
		boolean match =  false;
		
		for(boolean[] transaction : transactions) {
			match = searchIn(itemset, transaction);
			if(match)
				break;
		}
		
		return match;
	}
	
	public static void removeSupports(ArrayList<ArrayList<String>> itemsets) {
		
		for(int i = 0; i<itemsets.size(); i++)
			itemsets.get(i).remove(itemsets.get(i).size()-1);
	}
	
	private static void selectMaxAndPlace(ArrayList<ArrayList<String>> pVecteur, int pIndex) {
		
		// Naively look for maximum and its position.
		int max = readSupport(pVecteur.get(0));
		int pos = 0;
		int i = 0;
		for(i=0; i<=pIndex; i++) {
			if(readSupport(pVecteur.get(i))>max) {
				pos = i;
				max = readSupport(pVecteur.get(i));
			}
		}
		// Place it at pIndex.
		ArrayList<String> copy = pVecteur.get(pos);
		pVecteur.set(pos, pVecteur.get(pIndex));
		pVecteur.set(pIndex, copy);
	}
	
	public static void sort(ArrayList<ArrayList<String>> set) {
		
		for(int i = set.size()-1; i>0; i--)
			selectMaxAndPlace(set,i);
	}

}
