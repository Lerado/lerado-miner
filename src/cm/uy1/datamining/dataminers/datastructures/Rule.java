package cm.uy1.datamining.dataminers.datastructures;

import java.io.Serializable;

public class Rule implements Serializable{
	
	private static final long serialVersionUID = 4184378193598273668L;
	protected Itemset leftItemset;
	protected Itemset rightItemset;
	protected double confidence;
	
	public Rule(Itemset anItemset, Itemset anotherItemset, double ruleConfidence) {
		leftItemset = anItemset;
		rightItemset = anotherItemset;
		confidence = ruleConfidence;
	}
	
	public Itemset getLeftItemset() {
		
		return leftItemset;
	}
	
	public Itemset getRightItemset() {
		
		return rightItemset;
	}
	
	public double getConfidence() {
		
		return confidence;
	}
	
	public boolean equals(Rule anotherRule) {

		return this.leftItemset.equals(anotherRule.leftItemset)
				&& this.rightItemset.equals(anotherRule.rightItemset)
				&& this.confidence == anotherRule.confidence;
	}
	
	public boolean contains(Item item) {
		
		return leftItemset.contains(item) || rightItemset.contains(item);
	}
	
	public String toString() {
		
		return leftItemset.toStringIgnoringSupport()+" --> "
				+rightItemset.toStringIgnoringSupport()+" : "
					+String.valueOf(confidence);
	}

}
