package jmxservleteditor.composite;

import org.eclipse.jface.viewers.LabelProvider;

import cec.monitor.type.Attribut;
import cec.monitor.type.Counter;

public class CounterTreeLabelProvider extends LabelProvider {
	
	
	
	@Override
	public String getText(Object element) {
		String ret = null;
			if(element instanceof Counter){
				Counter ct = (Counter)element;
				ret=ct.getJmxObjectName();
				
				
			}
		if(element instanceof Attribut){
			Attribut at = (Attribut)element;
				ret=at.getName();
				
				
			}
		
		return ret;	
	}
	
	

}
