package jmxservleteditor.composite;

import org.eclipse.jface.databinding.viewers.TreeStructureAdvisor;

import cec.monitor.type.Counter;

public class CounterTreeAdvisor extends TreeStructureAdvisor {
	
	
	
	
	@Override
	public Object getParent(Object element) {
	
		return null;
	}
	
	
	@Override
	public Boolean hasChildren(Object element) {
		boolean ret = false;
		if(element instanceof Counter){
			Counter ct = (Counter) element;
			ret = ct.getAttributArray().length >0 ?  true : false ;
		}
		
		return ret;
	}

}
