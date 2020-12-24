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
import java.util.Collection;
import java.util.LinkedList;

public class DataMinerLinkedList {
	
	protected int minSupport;
	protected double minConfidence;
	
	protected ArrayList<String> items;
	protected LinkedList<ArrayList<String>> transactions;
	
	protected ArrayList<ArrayList<String>> candidates;
	protected ArrayList<ArrayList<String>> currentFrequentItemsets;
	protected ArrayList<ArrayList<String>> currentRareItemsets;
	protected ArrayList<ArrayList<ArrayList<String>>> frequentItemsets;
	protected ArrayList<ArrayList<ArrayList<String>>> rareItemsets;
	protected int numberOfFrequentItemsets;
	protected int numberOfRareItemsets;
	protected int numberOfAssociationRules;
	protected int level;
	protected long execTime1;
	protected long execTime2;
	protected String startDate;
	
	protected ArrayList<ArrayList<ArrayList<String>>> associationRules;
	
	public DataMinerLinkedList() {
		
		level = 0;
		execTime1 = 0;
		execTime2 = 0;
		startDate = "";
		frequentItemsets = new ArrayList<ArrayList<ArrayList<String>>>();
		rareItemsets = new ArrayList<ArrayList<ArrayList<String>>>();
		associationRules = new ArrayList<ArrayList<ArrayList<String>>>();
		numberOfFrequentItemsets = 0;
		numberOfRareItemsets = 0;
		numberOfAssociationRules = 0;
		readItems();
		readTransactions();
	}
	
	public void configure(double support_min, double confidence_min) {
		
		minSupport = (int)(support_min*transactions.size()/100);
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
			items = (ArrayList<String>) ois.readObject();
			ois.close();
			
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
			transactions = new LinkedList<ArrayList<String>>(
					(Collection<? extends ArrayList<String>>)ois.readObject());
			ois.close();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void levelOneCandidates() {
		
		candidates = new ArrayList<ArrayList<String>>();
		for(String s: items) {
			ArrayList<String> candidate = new ArrayList<String>();
			candidate.add(s);
			candidate.add("0");
			candidates.add(candidate);
		}
		level = 1;
	}
	
	private void countSupports() {
		
		for(ArrayList<String> transaction: transactions) {
				for(ArrayList<String> candidate : candidates) {
					int support = Integer.valueOf(candidate.remove(candidate.size()-1)).intValue();
					if(Utils.isIncludedInArrayList(candidate, transaction)) {
						support++;
						candidate.add(String.valueOf(support));
					}
					else
						candidate.add(String.valueOf(support));
					
				}
		}
	}
	
	private void eliminateCandidates() {
		currentFrequentItemsets = new ArrayList<ArrayList<String>>();
		currentRareItemsets = new ArrayList<ArrayList<String>>();
		
		for(ArrayList<String> v : candidates) {
			int support = Integer.valueOf(v.get(v.size()-1)).intValue();
			if(support >= minSupport) {
				currentFrequentItemsets.add(v);
				numberOfFrequentItemsets++;
			}
			else if(support < minSupport/10) {
				currentRareItemsets.add(v);
				numberOfRareItemsets++;
			}
		}
		
		if(!currentFrequentItemsets.isEmpty())
			frequentItemsets.add(currentFrequentItemsets);
		if(!currentRareItemsets.isEmpty())
			rareItemsets.add(currentRareItemsets);
	}
	
	private void aPrioriGen(ArrayList<ArrayList<String>> previousFrequentItemsets) {
		
		candidates = new ArrayList<ArrayList<String>>();
		
		if(!previousFrequentItemsets.isEmpty()) {
			
			for(int i = 0; i<previousFrequentItemsets.size(); i++) {

				ArrayList<String> cand1 = new ArrayList<String>(previousFrequentItemsets.get(i));
				cand1.remove(cand1.size()-1);
			
				for(int j = i+1; j<previousFrequentItemsets.size(); j++) {
					
					ArrayList<String> cand2 = new ArrayList<String>(previousFrequentItemsets.get(j));
					cand2.remove(cand2.size()-1);
					
					assert(cand1.size() == cand2.size());
					
					ArrayList<String> candidate = new ArrayList<String>(cand1);
					int nDifferent = 0;
					
					for(int k = 0; k<cand2.size(); k++) {
						boolean checker = false;
						String s2 = cand2.get(k);
						for(String s1 : cand1) {
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
						candidate.sort(null);
						if(!Utils.isInSetOfItemsets(candidate,candidates) 
								&& filterCandidate(candidate))
							candidates.add(candidate);
					}
				}
			}
			
		}
		
	}
	
	public boolean filterCandidate(ArrayList<String> candidate) {
			
		boolean checker = false;
		ArrayList<ArrayList<String>> subsets = Utils.listSubsetsOfLowerSize(candidate);
		
		for(ArrayList<String> subset : subsets) {
			for(ArrayList<String> frequentItemset : currentFrequentItemsets) {
				if(Utils.isIncludedInArrayList(subset, frequentItemset)) {
					checker = true;
					break;
				}
			}
		}
		
		if(!checker)
			System.exit(0);
		
		return checker;
	}
	
	public void cleanCandidates() {
		for(ArrayList<String> v : candidates)
			v.remove(v.size()-1);
	}
	
	public void initializeCandidatesCounters() {
		for(ArrayList<String> v : candidates)
			v.add("0");
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
				initializeCandidatesCounters();
				level++;
			}
		}
		
		execTime1 = System.currentTimeMillis() - execTime1;
	}
	
	public void fetchSupports(ArrayList<ArrayList<String>> set) {
		for(ArrayList<String> v : set)
			fetchSupport(v);
	}
	
	public void fetchSupport(ArrayList<String> v) {
		
		ArrayList<ArrayList<String>> bucket = frequentItemsets.get(v.size() - 1);
		for(ArrayList<String> v_ : bucket)
			if(Utils.isIncludedInArrayList(v, v_) && v.size() == v_.size()-1) {
				v.add(String.valueOf((Utils.readSupport(v_))));
			}
	}
	
	public void calculateAssociationRules() {
		
		execTime2 = System.currentTimeMillis();
		if(frequentItemsets.size() > 1) {
			for(int i = 1; i<frequentItemsets.size(); i++)
				for(int j = 0; j<frequentItemsets.get(i).size(); j++) {
					currentFrequentItemsets = frequentItemsets.get(i);
					ArrayList<String> v = new ArrayList<String>(currentFrequentItemsets.get(j));
					int m = 1;
					ArrayList<ArrayList<String>> H = Utils.listSingletonsOfItemset(v);
					fetchSupports(H);
					
					while(m < v.size()-1) {
						for(int k = 0; k<H.size(); k++) {
							ArrayList<String> h = H.get(k);
							double confidence;
							ArrayList<String> subing = Utils.sub(v, h);
							fetchSupport(subing);
							confidence = (double)Utils.readSupport(v)/Utils.readSupport(subing);
							if(confidence >= minConfidence) {
								ArrayList<ArrayList<String>> rule = new ArrayList<ArrayList<String>>();
								ArrayList<String> keepConfidence = new ArrayList<String>();
								keepConfidence.add(String.valueOf(confidence));
								rule.add(subing);
								rule.add(h);
								rule.add(keepConfidence);
								associationRules.add(rule);
								numberOfAssociationRules++;
							}
							else
								H.remove(h);
						}
						
						aPrioriGen(H);
						H = new ArrayList<ArrayList<String>>(candidates);
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
		statistics.add(1);
		
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
										new File(dir,"frequentItemsets.save"))));
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
										new File(dir,"rareItemsets.save"))));
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
										new File(dir,"associationRules.save"))));
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