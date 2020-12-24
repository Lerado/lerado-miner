package cm.uy1.datamining;
import java.io.BufferedInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import cm.uy1.datamining.dataminers.Utils;
import cm.uy1.datamining.dataminers.datastructures.DataReader;
import cm.uy1.datamining.dataminers.datastructures.Item;
import cm.uy1.datamining.dataminers.datastructures.Itemset;
import cm.uy1.datamining.dataminers.datastructures.Rule;

@SuppressWarnings("rawtypes")
public class AppServices {
	
	protected ArrayList<String> items;
	protected ArrayList<ArrayList<ArrayList<String>>> frequentItemsets;
	protected ArrayList<ArrayList<ArrayList<String>>> rareItemsets;
	protected ArrayList<ArrayList<Itemset>> frequentItemsetsOptimized;
	protected ArrayList<ArrayList<Itemset>> rareItemsetsOptimized;
	protected ArrayList<ArrayList<ArrayList<String>>> associationRules;
	protected ArrayList<Rule> associationRulesOptimized;
	protected ArrayList statistics;
	
	public AppServices() {
		
		try {
			readItems();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		frequentItemsets = new ArrayList<ArrayList<ArrayList<String>>>();
		rareItemsets = new ArrayList<ArrayList<ArrayList<String>>>();
		frequentItemsetsOptimized = new ArrayList<ArrayList<Itemset>>();
		rareItemsetsOptimized = new ArrayList<ArrayList<Itemset>>();
	}
	
	public String listItems() {
		
		String itemsString = new String();
		
		for(String s : items)
			if(s.compareTo("a")<0)
				itemsString += s+"\n";
		
		return itemsString;
	}
	
	public int getLastAlgoNumber() throws FileNotFoundException, ClassNotFoundException, IOException {
		
		readStatistics();
		return (int)statistics.get(statistics.size()-1);
	}
	
	public ArrayList statistics() {
		
		return statistics;
	}
	
	public void readStatistics() throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream ois = null;
		ois = new ObjectInputStream(
				new BufferedInputStream(
						new FileInputStream(
								new File(System.getProperty("user.home")
									+File.separatorChar
										+"Data Mining App"+File.separatorChar
										+"archives"+File.separatorChar+"statistics.save"))));

		statistics = (ArrayList) ois.readObject();
		ois.close();
	}
	
	
	@SuppressWarnings("unchecked")
	public void readItems() throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream ois = null;
		ois = new ObjectInputStream(
				new BufferedInputStream(
						new FileInputStream(
								new File(System.getProperty("user.home")
										+File.separatorChar
										+"Data Mining App"+File.separatorChar
										+"archives"+File.separatorChar+"items.save"))));

		items = (ArrayList<String>) ois.readObject();
		ois.close();
	}
	
	@SuppressWarnings("unchecked")
	public void readOptimizedFrequentItemsets() throws FileNotFoundException, IOException, ClassNotFoundException {
		
		ObjectInputStream ois = null;
		ois = new ObjectInputStream(
				new BufferedInputStream(
						new FileInputStream(
								new File(System.getProperty("user.home")
										+File.separatorChar
										+"Data Mining App"+File.separatorChar
										+"archives"+File.separatorChar+"frequentItemsetsOptimized.save"))));
		
		frequentItemsetsOptimized = (ArrayList<ArrayList<Itemset>>) ois.readObject();
		ois.close();
	}
	
	@SuppressWarnings("unchecked")
	public void readFrequentItemsets() throws FileNotFoundException, IOException, ClassNotFoundException {
		
		ObjectInputStream ois = null;
		ois = new ObjectInputStream(
				new BufferedInputStream(
						new FileInputStream(
								new File(System.getProperty("user.home")
										+File.separatorChar
										+"Data Mining App"+File.separatorChar
										+"archives"+File.separatorChar+"frequentItemsets.save"))));
	
		frequentItemsets = (ArrayList<ArrayList<ArrayList<String>>>) ois.readObject();
		ois.close();
	}

	@SuppressWarnings("unchecked")
	public void readOptimizedRareItemsets() throws FileNotFoundException, IOException, ClassNotFoundException {
		
		ObjectInputStream ois = null;
		ois = new ObjectInputStream(
				new BufferedInputStream(
						new FileInputStream(
								new File(System.getProperty("user.home")
										+File.separatorChar
										+"Data Mining App"+File.separatorChar
										+"archives"+File.separatorChar+"rareItemsetsOptimized.save"))));

		rareItemsetsOptimized = (ArrayList<ArrayList<Itemset>>) ois.readObject();
		ois.close();
	}
	
	@SuppressWarnings("unchecked")
	public void readRareItemsets() throws FileNotFoundException, IOException, ClassNotFoundException {
		
		ObjectInputStream ois = null;
		ois = new ObjectInputStream(
				new BufferedInputStream(
						new FileInputStream(
								new File(System.getProperty("user.home")
										+File.separatorChar
										+"Data Mining App"+File.separatorChar
										+"archives"+File.separatorChar+"rareItemsets.save"))));

		rareItemsets = (ArrayList<ArrayList<ArrayList<String>>>) ois.readObject();
		ois.close();
	}
	
	@SuppressWarnings("unchecked")
	public void readAssociationRules() throws FileNotFoundException, IOException, ClassNotFoundException {
		
		ObjectInputStream ois = null;
		ois = new ObjectInputStream(
				new BufferedInputStream(
						new FileInputStream(
								new File(System.getProperty("user.home")
										+File.separatorChar
										+"Data Mining App"+File.separatorChar
										+"archives"+File.separatorChar+"associationRules.save"))));

		associationRules = (ArrayList<ArrayList<ArrayList<String>>>) ois.readObject();
		ois.close();
	}
	
	@SuppressWarnings("unchecked")
	public void readOptimizedAssociationRules() throws ClassNotFoundException, IOException {
		
		ObjectInputStream ois = null;
		ois = new ObjectInputStream(
				new BufferedInputStream(
						new FileInputStream(
								new File(System.getProperty("user.home")
										+File.separatorChar
										+"Data Mining App"+File.separatorChar
										+"archives"+File.separatorChar+"associationRulesOptimized.save"))));

		associationRulesOptimized = (ArrayList<Rule>) ois.readObject();
		ois.close();
	}
	
	public void printResults1(FileWriter fw) throws IOException, ClassNotFoundException {
		
		readStatistics();
		
	  fw.write("\n+----------------------------------------------------+\n");
		fw.write("|                FREQUENT ITEMSETS                   |\n");
		fw.write("+----------------------------------------------------+\n");
		
		fw.write("\nFound "+ statistics.get(3)
		+ " during last mining on the "
				+ statistics.get(0)+" with support "
					+ statistics.get(6)+ " and confidence " + statistics.get(7) +".\n");
		fw.write("Execution duration : " + String.valueOf((long)statistics.get(1)) + "ms\n");
		
		
		int i = 1;
		for(ArrayList<ArrayList<String>> pack : frequentItemsets) {
			fw.write("\n\n-- Found "+ pack.size() + " of size " + i++ + "\n");
			for(ArrayList<String> itemset : pack)
				fw.write(itemset.toString()+"\n");
		}
		
	  fw.write("\n+----------------------------------------------------+\n");
		fw.write("|                    RARE ITEMSETS                   |\n");
		fw.write("+----------------------------------------------------+\n");
		
		fw.write("\nFound "+ statistics.get(4)
		+ " during last mining on the "
		+ statistics.get(0)+" with support "
		+ statistics.get(6)+ " and confidence " + statistics.get(7) +".\n");
		fw.write("Execution duration : " + String.valueOf((long)statistics.get(1)) + "ms\n");
		
		i = 1;
		for(ArrayList<ArrayList<String>> pack : rareItemsets) {
			fw.write("\n\nFound "+ pack.size() + " of size" + i++ + "\n");
			for(ArrayList<String> itemset : pack)
				fw.write(itemset.toString()+"\n");
		}
		
	  fw.write("\n+----------------------------------------------------+\n");
		fw.write("|                ASSOCIATION RULES                   |\n");
		fw.write("+----------------------------------------------------+\n");
		
		fw.write("\nFound "+ statistics.get(5)
		+ " during last mining on the "
		+ statistics.get(0)+" with support "
		+ statistics.get(6)+ " and confidence " + statistics.get(7) +".\n");
		fw.write("Execution duration : " + String.valueOf((long)statistics.get(2)) + "ms\n");
		
		for(ArrayList<ArrayList<String>> rule : associationRules) {
			fw.write(rule.get(0).toString()+ " --->  " 
						+ rule.get(1).toString() + " " +rule.get(2).get(0) + "\n");
		}
		
		fw.close();
	}
	
	public void printResults2(FileWriter fw) throws IOException, ClassNotFoundException {
		
		readStatistics();
		
	  fw.write("\n+----------------------------------------------------+\n");
		fw.write("|                FREQUENT ITEMSETS                   |\n");
		fw.write("+----------------------------------------------------+\n");
		
		fw.write("\nFound "+ statistics.get(3)
		+ " during last mining on the "
				+ statistics.get(0)+" with support "
					+ statistics.get(6)+ " and confidence " + statistics.get(7) +".\n");
		fw.write("Execution duration : " + String.valueOf((long)statistics.get(1)) + "ms\n");
		
		
		int i = 1;
		for(ArrayList<Itemset> pack : frequentItemsetsOptimized) {
			fw.write("\n\n-- Found "+ pack.size() + " of size " + i++ + "\n");
			for(Itemset itemset : pack)
				fw.write(itemset.toString()+"\n");
		}
		
	  fw.write("\n+----------------------------------------------------+\n");
		fw.write("|                    RARE ITEMSETS                   |\n");
		fw.write("+----------------------------------------------------+\n");
		
		fw.write("\nFound "+ statistics.get(4)
		+ " during last mining on the "
		+ statistics.get(0)+" with support "
		+ statistics.get(6)+ " and confidence " + statistics.get(7) +".\n");
		fw.write("Execution duration : " + String.valueOf((long)statistics.get(1)) + "ms\n");
		
		i = 1;
		for(ArrayList<Itemset> pack : rareItemsetsOptimized) {
			fw.write("\n\nFound "+ pack.size() + " of size " + i++ + "\n");
			for(Itemset itemset : pack)
				fw.write(itemset.toString()+"\n");
		}
		
	  fw.write("\n+----------------------------------------------------+\n");
		fw.write("|                ASSOCIATION RULES                   |\n");
		fw.write("+----------------------------------------------------+\n");
		
		fw.write("\nFound "+ statistics.get(5)
		+ " during last mining on the "
		+ statistics.get(0)+" with support "
		+ statistics.get(6)+ " and confidence " + statistics.get(7) +".\n");
		fw.write("Execution duration : " + String.valueOf((long)statistics.get(2)) + "ms\n");
		
		for(Rule rule : associationRulesOptimized) {
			fw.write(rule.toString() + "\n");
		}
		
		fw.close();
	}
	
	public void printResults(File f, int algoNumber) throws IOException, ClassNotFoundException {
		
		FileWriter fw;
		
		fw = new FileWriter(f);
		switch(algoNumber) {
		case 4:
			readOptimizedFrequentItemsets();
			readOptimizedAssociationRules();
			printResults2(fw);
			break;
		default:
			readFrequentItemsets();
			readAssociationRules();
			printResults1(fw);
		}
	}
	
	public String results() throws IOException {
		
		FileReader fr = new FileReader(new File(new File(System.getProperty("user.home")
							+File.separatorChar
							+"Data Mining App"+File.separatorChar+"archives"+File.separatorChar),"output.save"));
		String results = "";
		
		int i = 0;
		while((i = fr.read()) != -1)
			results += (char)i;
		fr.close();
		
		return results;
	}
	
	public boolean searchInItems(String s) {
		
		return items.contains(s);
	}
	
	public String nearerItem(String item) {
		String nearestItem = "";
		
		int i = 0;
		int j = 0;
		int k = 0;
		double max = 0;
		int maxIndexI = 0;
		int maxIndexJ = 0;
		for(i = 0; i<associationRules.size(); i++)
			for(j = 0; j<associationRules.get(i).size(); j++)
				for(k = 0; k<associationRules.get(i).get(j).size(); k++)
					if(associationRules.get(i).get(j).contains(item)) {
						double localMax = Double.valueOf(associationRules.get(i)
								.get(associationRules.get(i).size()-1).get(0)).doubleValue();
						if(localMax > max){
							max = localMax;
							maxIndexI = i;
							maxIndexJ = j;
						}	
					}
		nearestItem = associationRules.get(maxIndexI).get( Math.abs(maxIndexJ - 1) ).get(0);
		
		return nearestItem;
	}
	
	public String nearerItem(Item item) {
		String nearestItem = "";
		
		int i = 0;
		double max = 0;
		int maxIndexI = 0;
		int maxIndexJ = 0;
		for(Rule rule : associationRulesOptimized) {
			if(rule.getConfidence()>max && rule.contains(item)) {
				max = rule.getConfidence();
				maxIndexI = i;
				maxIndexJ = (rule.getLeftItemset().contains(item))?1:2;
			}
			i++;
		}
		
		nearestItem = (maxIndexJ == 1)?associationRulesOptimized.get(maxIndexI).getRightItemset().get(0).toString():
			(maxIndexJ == 2)?associationRulesOptimized.get(maxIndexI).getLeftItemset().get(0).toString():"";
		return nearestItem;
	}
	
	private void purify(ArrayList<String> itemList) {
		
		for(int i = itemList.size()-1; i>=0; i--)
			if(itemList.get(i).compareTo("a")>=0)
				itemList.remove(i);
	}
	
	public void printCatalog1(File f) throws IOException {
		
		ArrayList<String> catalogItems = new ArrayList<String>(items);
		
		if(!frequentItemsets.isEmpty()) {
			
			ArrayList<ArrayList<String>> itemsets = new ArrayList<ArrayList<String>>(frequentItemsets.get(0));
			Utils.sort(itemsets);
			Utils.removeSupports(itemsets);
			
			for(int i = 1; i<frequentItemsets.size(); i++)
				for(int j = 0; j<frequentItemsets.get(i).size(); j++)
					for(int k = 0; k<frequentItemsets.get(i).get(j).size(); k++)
						for(int l = 0; l<itemsets.size(); l++)
							if(Utils.isIncludedInArrayList(itemsets.get(l), frequentItemsets.get(i).get(j))) {
								ArrayList<String> temp = new ArrayList<String>(frequentItemsets.get(i).get(j));
								temp.remove(temp.size()-1);
								temp.removeAll(itemsets.get(l));
								itemsets.get(l).addAll(temp);
								catalogItems.removeAll(itemsets.get(l));
							}
			for(int l = 0; l<itemsets.size(); l++) {
				catalogItems.addAll(0, itemsets.get(l));
			}
			
			purify(catalogItems);
		}
		
		FileWriter fw = new FileWriter(f);
		fw.write("\n+----------------------------------------------+\n");
		fw.write("+------------------  CATALOG  -----------------+\n\n");
		for(String s : catalogItems)
			fw.write("---> "+s+"\n");
		
		fw.close();
		
	}
	
public void printCatalog2(File f) throws IOException {
		
		ArrayList<String> catalogItems = new ArrayList<String>(items);
		
		if(!frequentItemsetsOptimized.isEmpty()) {
			
			ArrayList<Itemset> itemsets = new ArrayList<Itemset>(frequentItemsetsOptimized.get(0));
			itemsets.sort(null);;
			
			for(int i = 1; i<frequentItemsetsOptimized.size(); i++)
				for(int j = 0; j<frequentItemsetsOptimized.get(i).size(); j++)
					for(int k = 0; k<frequentItemsetsOptimized.get(i).get(j).size(); k++)
						for(int l = 0; l<itemsets.size(); l++)
							if( frequentItemsetsOptimized.get(i).get(j).contains(itemsets.get(l)) ) {
								Itemset temp = new Itemset(frequentItemsetsOptimized.get(i).get(j));
								temp.removeAll(itemsets.get(l));
								itemsets.get(l).addAll(temp);
								catalogItems.removeAll(itemsets.get(l).itemsToString());
							}
			for(int l = 0; l<itemsets.size(); l++) {
				catalogItems.addAll(0, itemsets.get(l).itemsToString());
			}
			
			purify(catalogItems);
		}
		
		FileWriter fw = new FileWriter(f);
		fw.write("\n+----------------------------------------------+\n");
		fw.write("+------------------  CATALOG  -----------------+\n\n");
		for(String s : catalogItems)
			fw.write("---> "+s+"\n");
		
		fw.close();
		
	}

	public void printSimpleCatalog(File f) throws IOException {
		
		FileWriter fw = new FileWriter(f);
		fw.write("\n+----------------------------------------------+\n");
		fw.write("+------------------  CATALOG  -----------------+\n\n");
		for(String s : items)
			fw.write("---> "+s+"\n");
		
		fw.close();
	}
	
	public void printCatalog(File f, int number) throws IOException {
		
		
		try {
			readFrequentItemsets();
		} catch (ClassNotFoundException | IOException e) {
			
		}
			
		try {
			readOptimizedFrequentItemsets();
		} catch (ClassNotFoundException | IOException e1) {
			
		}
		
		switch(number) {
		case 3:
			printCatalog2(f);
			break;
		default:
			printCatalog1(f);
		}
	}
	
	public String listRareItemsetsGroup1() throws FileNotFoundException, ClassNotFoundException, IOException {
		
		String result = "";
		readOptimizedRareItemsets();
		
		for(int i = 1; i<rareItemsetsOptimized.size(); i++)
			for(Itemset itemset : rareItemsetsOptimized.get(i))
				result += itemset.toString()+"\n";
		return result;
	}
	
	public String listFrequentItemsetsGroup1() throws FileNotFoundException, ClassNotFoundException, IOException {
		
		String result = "";
		readOptimizedFrequentItemsets();
		
		for(int i = 1; i<frequentItemsetsOptimized.size(); i++)
			for(Itemset itemset : frequentItemsetsOptimized.get(i))
				result += itemset.toString()+"\n";
		return result;
	}
	
	public String listRareItemsetsGroup2() throws FileNotFoundException, ClassNotFoundException, IOException {
		
		String result = "";
		readRareItemsets();
		
		for(int i = 1; i<rareItemsets.size(); i++)
			for(ArrayList<String> itemset : rareItemsets.get(i))
				result += itemset.toString()+"\n";
		return result;
	}
	
	public String listFrequentItemsetsGroup2() throws FileNotFoundException, ClassNotFoundException, IOException {
		
		String result = "";
		readFrequentItemsets();
		
		for(int i = 1; i<frequentItemsets.size(); i++)
			for(ArrayList<String> itemset : frequentItemsets.get(i))
				result += itemset.toString()+"\n";
		return result;
	}
	
	public String listAssociationRules1() throws FileNotFoundException, ClassNotFoundException, IOException {
		
		String result = "";
		readOptimizedAssociationRules();
		
		for(Rule rule : associationRulesOptimized)
			result += rule.toString()+"\n";
		
		return result;
	}
	
	public String listAssociationRules2() throws FileNotFoundException, ClassNotFoundException, IOException {
		
		String result = "";
		readAssociationRules();
		
		for(ArrayList<ArrayList<String>> rule : associationRules) {
			result += rule.get(0).toString()+ " --->  " 
						+ rule.get(1).toString() + " " +rule.get(2).get(0) + "\n";
		}
		return result;
	}
	
	public String listRareItemsetsGroups(int number) throws FileNotFoundException, ClassNotFoundException, IOException {
		
		String result = "";
		
		switch(number) {
		case 3:
			result = listRareItemsetsGroup1();
			break;
		default:
			result = listRareItemsetsGroup2();
		}
		
		return result;
	}
	
	public String listFrequentItemsetsGroups(int number) throws FileNotFoundException, ClassNotFoundException, IOException {
		
		String result = "";
		
		switch(number) {
		case 3:
			result = listFrequentItemsetsGroup1();
			break;
		default:
			result = listFrequentItemsetsGroup2();
		}
		
		return result;
	}
	
	public String listAssociationRules(int number) throws FileNotFoundException, ClassNotFoundException, IOException {
		
		String result = "";
		
		switch(number) {
		case 3:
			result = listAssociationRules1();
			break;
		default:
			result = listAssociationRules2();
		}
		
		return result;
	}
	
	public static boolean checkFiles() {
		
		File f1 = new File(System.getProperty("user.home")
				+File.separatorChar
				+"Data Mining App"+File.separatorChar+"archives"+File.separatorChar+"items.save");
		
		File f2 = new File(System.getProperty("user.home")
				+File.separatorChar
				+"Data Mining App"+File.separatorChar+"archives"+File.separatorChar+"transactions.save");
		
		return f1.exists() && f2.exists();
	}
	
	public static boolean save(File file) {
		DataReader dr = new DataReader(file);
		dr.saveItems();
		dr.saveTransactions();
		try {
			dr.close();
			return true;
		} catch (IOException e) {
			return false;
		}
		
	}
	
}
