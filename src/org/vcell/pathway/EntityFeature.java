package org.vcell.pathway;

import java.util.ArrayList;

public interface EntityFeature extends UtilityClass {

	public ArrayList<SequenceLocation> getFeatureLocation();
	
	public ArrayList<SequenceRegionVocabulary> getFeatureLocationType();
	
	public ArrayList<EntityFeature> getMemberFeature();
	
	public void setFeatureLocation(ArrayList<SequenceLocation> featureLocation);
	
	public void setFeatureLocationType(ArrayList<SequenceRegionVocabulary> featureLocationType);

	public void setMemberFeature(ArrayList<EntityFeature> memberFeature);

}
