package cbit.vcell.graph;
/*�
 * (C) Copyright University of Connecticut Health Center 2001.
 * All rights reserved.
�*/
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cbit.gui.graph.*;
import cbit.vcell.model.*;
/**
 * This class was generated by a SmartGuide.
 * 
 */
public abstract class ModelCartoon extends GraphModel 
implements java.beans.PropertyChangeListener, Model.Owner {
	public static final String PROPERTY_NAME_MODEL = "model";
	private Model fieldModel = null;

	/**
	 * Insert the method's description here.
	 * Creation date: (5/13/2003 9:46:54 AM)
	 * @return cbit.vcell.model.Model
	 */
	public Model getModel() {
		return fieldModel;
	}


	/**
	 * Insert the method's description here.
	 * Creation date: (5/13/2003 9:46:54 AM)
	 * @param newFieldModel cbit.vcell.model.Model
	 */
	public void setModel(Model newFieldModel) {
		Model oldModel = fieldModel;
		if (oldModel != null){
			oldModel.removePropertyChangeListener(this);
		}
		fieldModel = newFieldModel;
		if(fieldModel != null){
			fieldModel.removePropertyChangeListener(this);
			fieldModel.addPropertyChangeListener(this);
		}
		refreshAll();
		firePropertyChange(PROPERTY_NAME_MODEL, oldModel, newFieldModel);
	}
	
	@Override
	public void searchText(String text) {
		String lowerCaseText = text.toLowerCase();
		Set<Object> selectedObjectsNew = new HashSet<Object>();
		for(Map.Entry<Object, Shape> entry : objectShapeMap.entrySet()) {
			Object object = entry.getKey();
			Shape shape = entry.getValue();
			if(!(object instanceof Structure) && text != null && text.length() != 0 && shape.getLabel() != null && shape.getLabel().toLowerCase().contains(lowerCaseText)) {
				selectedObjectsNew.add(object);
			}
		}
		setSelectedObjects(selectedObjectsNew.toArray());
	}
}