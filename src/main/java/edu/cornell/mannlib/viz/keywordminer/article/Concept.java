package edu.cornell.mannlib.viz.keywordminer.article;

public class Concept {

	private String concept;
	private String conceptLabel;
	
	public Concept(String concept, String conceptLabel) {
		super();
		this.concept = concept;
		this.conceptLabel = conceptLabel;
	}
	public Concept() {
	}
	
	public String getConcept() {
		return concept;
	}
	public void setConcept(String concept) {
		this.concept = concept;
	}
	public String getConceptLabel() {
		return conceptLabel;
	}
	public void setConceptLabel(String conceptLabel) {
		this.conceptLabel = conceptLabel;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((conceptLabel == null) ? 0 : conceptLabel.hashCode());
		result = prime * result + ((concept == null) ? 0 : concept.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Concept other = (Concept) obj;
		if (conceptLabel == null) {
			if (other.conceptLabel != null)
				return false;
		} else if (!conceptLabel.equals(other.conceptLabel))
			return false;
		if (concept == null) {
			if (other.concept != null)
				return false;
		} else if (!concept.equals(other.concept))
			return false;
		return true;
	}
	
	
	
	
}
