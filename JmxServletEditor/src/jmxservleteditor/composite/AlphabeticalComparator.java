package jmxservleteditor.composite;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

public class AlphabeticalComparator extends ViewerComparator {
	
	
	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		
		int ret = 0;
		
		if( e1 instanceof String && e2 instanceof String) {
			String s1 = (String) e1;
			String s2 = (String) e2;
			ret = s1.compareTo(s2);
		}
		
		
		
		return ret;
		
	}
	
	

}
