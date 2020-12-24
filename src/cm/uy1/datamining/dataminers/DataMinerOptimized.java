package cm.uy1.datamining.dataminers;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;

import cm.uy1.datamining.dataminers.datastructures.Item;
import cm.uy1.datamining.dataminers.datastructures.Itemset;
import cm.uy1.datamining.dataminers.datastructures.Rule;

public class DataMinerOptimized {

	protected int minSupport;
	protected double minConfidence;
	
	protected ArrayList<Item> items;
	protected boolean[][] transactions;
	
	protected ArrayList<Itemset> candidates;
	protected ArrayList<Itemset> currentFrequentItemsets;
	protected ArrayList<ArrayList<Itemset>> frequentItemsets;
	protected ArrayList<Itemset> currentRareItemsets;
	protected ArrayList<ArrayList<Itemset>> rareItemsets;
	protected int numberOfFrequentItemsets;
	protected int numberOfRareItemsets;
	protected int numberOfAssociationRules;
	protected int level;
	protected long execTime1;
	protected long execTime2;
	protected String startDate;
	
	protected ArrayList<Rule> associationRules;
	
	public DataMinerOptimized() {

		level = 0;
		execTime1 = 0;
		startDate = "";
		frequentItemsets = new ArrayList<ArrayList<Itemset>>();
		rareItemsets = new ArrayList<ArrayList<Itemset>>();
		associationRules = new ArrayList<Rule>();
		numberOfAssociationRules = 0;
		numberOfFrequentItemsets = 0;
		numberOfRareItemsets = 0;
		readItems();
		readTransactions();
	}
	
	public void configure(double support_min, double confidence_min) {
		
		minSupport = (int)(support_min*transactions.length/100);
		minConfidence = confidence_min;
	}

	@SuppressWarnings("unchecked")
	public void readItems() {
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(
					new BufferedInputStream(
							new FileInputStream(
									new File(System.getProperty("user.home")
											+File.separatorChar
											+"Data Mining App"+File.separatorChar
											+"archives"+File.separatorChar+"items.save"))));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			ArrayList<String> items_ = (ArrayList<String>) ois.readObject();
			ois.close();
			items = new ArrayList<Item>();
			int i = 0;
			for(String s : items_)
				items.add(new Item(s,i++));
			
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public void readTransactions() {
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(
					new BufferedInputStream(
							new FileInputStream(
									new File(System.getProperty("user.home")
											+File.separatorChar
											+"Data Mining App"+File.separatorChar
											+"archives"+File.separatorChar+"transactions.save"))));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			transactions = Utils.toBooleanArray((ArrayList<ArrayList<String>>) ois.readObject(), items);
			ois.close();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void levelOneCandidates() {
		
		candidates = new ArrayList<Itemset>();
		for(Item s: items) {
			Itemset candidate = new Itemset(s);
			candidates.add(candidate);
		}
		level = 1;
	}
	
	private void countSupports() {
		
		for(boolean[] transaction: transactions) {
				for(Itemset candidate : candidates)
					if(Utils.searchItemsetIn(candidate, items, transaction)) {
						candidate.increment();
					}
		}
	}
	
	private void eliminateCandidates() {
		currentFrequentItemsets = new ArrayList<Itemset>();
		currentRareItemsets = new ArrayList<Itemset>();
		
		for(Itemset v : candidates) {
			if(v.support() >= minSupport) {
				currentFrequentItemsets.add(v);
				numberOfFrequentItemsets++;
			}
			else if(v.support()  < minSupport/10) {
				currentRareItemsets.add(v);
				numberOfRareItemsets++;
			}
		}
		
		if(!currentFrequentItemsets.isEmpty())
			frequentItemsets.add(currentFrequentItemsets);
		if(!currentRareItemsets.isEmpty())
			rareItemsets.add(currentRareItemsets);
	}
	
	private void aPrioriGen(ArrayList<Itemset> previousFrequentItemsets) {
		
		candidates = new ArrayList<Itemset>();
		
		if(!previousFrequentItemsets.isEmpty()) {
			
			for(int i = 0; i<previousFrequentItemsets.size(); i++) {

				Itemset cand1 = previousFrequentItemsets.get(i);
			
				for(int j = i+1; j<previousFrequentItemsets.size(); j++) {
					
					Itemset cand2 = previousFrequentItemsets.get(j);
					
					assert(cand1.size() == cand2.size());
					
					Itemset candidate = new Itemset(cand1);
					candidate.reset();
					int nDifferent = 0;
					
					for(int k = 0; k<cand2.size(); k++) {
						boolean checker = false;
						Item s2 = cand2.get(k);
						for(int l = 0; l<cand1.size(); l++) {
							Item s1 = cand1.get(l);
							if(s1 == s2) {
								checker = true;
								break;
							}
						}
						if(!checker) {
							nDifferent++;
							candidate.add(s2);
						}
					}
					assert(nDifferent > 0);
					if(nDifferent == 1) {
						if(!Itemset.isIn(candidate,candidates) 
								&& filterCandidate(candidate))
							candidates.add(candidate);
					}
				}
			}
			
		}
		
	}
	
	public boolean filterCandidate(Itemset candidate) {
			
		boolean checker = false;
		ArrayList<Itemset> subsets = candidate.listItemsetsOfLowerSize();
		
		for(Itemset subset : subsets) {
			for(Itemset frequentItemset : currentFrequentItemsets) {
				if(subset.equalsIgnoringSupport(frequentItemset)) {
					checker = true;
					break;
				}
			}
		}
		
		return checker;
	}
	
	public void aPriori() {
		
		startDate = LocalDateTime.now().toString();
		execTime1 = System.currentTimeMillis();
		levelOneCandidates();
		
		while(!candidates.isEmpty()) {
			
			countSupports();
			eliminateCandidates();
			aPrioriGen(currentFrequentItemsets);
			
			if(!candidates.isEmpty()) {
				level++;
			}
		}
		execTime1 = System.currentTimeMillis() - execTime1;
		
	}
	
	public void fetchSupports(ArrayList<Itemset> set) {
		for(Itemset v : set)
			fetchSupport(v);
	}
	
	public void fetchSupport(Itemset v) {
		
		ArrayList<Itemset> bucket = frequentItemsets.get(v.size()-1);
		for(Itemset v_ : bucket)
			if(v.equalsIgnoringSupport(v_)) {
				v.setSupport(v_.getSupport());;
				break;
			}
	}
	
	public void calculateAssociationRules() {
		
		execTime2 = System.currentTimeMillis();
		if(frequentItemsets.size() > 1) {
			for(int i = 1; i<frequentItemsets.size(); i++)
				for(int j = 0; j<frequentItemsets.get(i).size(); j++) {
					currentFrequentItemsets = frequentItemsets.get(i);
					Itemset v = new Itemset(currentFrequentItemsets.get(j));
					int m = 1;
					ArrayList<Itemset> H = v.listItemsetsOfLowerSize();
					fetchSupports(H);
					
					while(m < v.size()) {
						for(int k = 0; k<H.size(); k++) {
							Itemset h = H.get(k);
							double confidence;
							Itemset subing = new Itemset(Utils.subItemsets(v.items(), h.items()));
							fetchSupport(subing);
							confidence = (double)v.support()/(double)subing.support();
							
							if(confidence >= minConfidence) {
								Rule rule = new Rule(subing,h,confidence);
								associationRules.add(rule);
								numberOfAssociationRules++;
							}
							else
								H.remove(h);
						}
						
						aPrioriGen(H);
						H = new ArrayList<Itemset>(candidates);
						fetchSupports(H);
						m++;
					}
				}
		}
		execTime2 = System.currentTimeMillis() - execTime2;
		try {
			saveStatistics();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void saveStatistics() throws FileNotFoundException, IOException {
		
		ArrayList statistics = new ArrayList();
		statistics.add(startDate);
		statistics.add(execTime1);
		statistics.add(execTime2);
		statistics.add(numberOfFrequentItemsets);
		statistics.add(numberOfRareItemsets);
		statistics.add(numberOfAssociationRules);
		statistics.add(minSupport);
		statistics.add(minConfidence);
		statistics.add(3);
		
		ObjectOutputStream oos;
		File dir = new File(System.getProperty("user.home")
				+File.separatorChar
				+"Data Mining App"+File.separatorChar+"archives"+File.separatorChar);
		if(!dir.exists())
			dir.mkdirs();
		
		oos = new ObjectOutputStream(
				new BufferedOutputStream(
						new FileOutputStream(
								new File(dir,"statistics.save"))));
		oos.writeObject(statistics);
		oos.close();
	}
	
	public void saveFrequentItemsets() {
		
		if(!frequentItemsets.isEmpty()) {
			
			ObjectOutputStream oos;
			try {
				File dir = new File(System.getProperty("user.home")
						+File.separatorChar
						+"Data Mining App"+File.separatorChar+"archives"+File.separatorChar);
				if(!dir.exists())
					dir.mkdirs();
				
				oos = new ObjectOutputStream(
						new BufferedOutputStream(
								new FileOutputStream(
										new File(dir,"frequentItemsetsOptimized.save"))));
				oos.writeObject(frequentItemsets);
				oos.close();
			} catch (FileNotFoundException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
	}
	
	public void saveRareItemsets() {
		
		if(!rareItemsets.isEmpty()) {
			
			ObjectOutputStream oos;
			try {
				File dir = new File(System.getProperty("user.home")
						+File.separatorChar
						+"Data Mining App"+File.separatorChar+"archives"+File.separatorChar);
				if(!dir.exists())
					dir.mkdirs();
				
				oos = new ObjectOutputStream(
						new BufferedOutputStream(
								new FileOutputStream(
										new File(dir,"rareItemsetsOptimized.save"))));
				oos.writeObject(rareItemsets);
				oos.close();
			} catch (FileNotFoundException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
	}
	
	public void saveAssociationRules() {
		
		if(!associationRules.isEmpty()) {
			
			ObjectOutputStream oos;
			try {
				File dir = new File(System.getProperty("user.home")
						+File.separatorChar
						+"Data Mining App"+File.separatorChar+"archives"+File.separatorChar);
				if(!dir.exists())
					dir.mkdirs();
				
				oos = new ObjectOutputStream(
						new BufferedOutputStream(
								new FileOutputStream(
										new File(dir,"associationRulesOptimized.save"))));
				oos.writeObject(associationRules);
				oos.close();
			} catch (FileNotFoundException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
	}
}
