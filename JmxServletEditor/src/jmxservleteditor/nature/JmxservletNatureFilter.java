package jmxservleteditor.nature;

import jmxservleteditor.Activator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class JmxservletNatureFilter extends ViewerFilter {

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		boolean ret = false;
		Activator.getDefault().log("filtering resource", IStatus.INFO, null);
		
		
		
		
		if(element instanceof IProject){
			
			ret = hasprojectNature((IProject) element);
			
			
			
		} else {
			if (element instanceof IResource ){
				IResource r = (IResource)element;
				ret = hasprojectNature(r.getProject());
				
			}
			
		}
		return ret;
	}

	private boolean hasprojectNature(IProject project) {
		boolean ret = false;
		try {
			String[] natures = project.getDescription().getNatureIds();
			for (String current : natures) {
				if(current.equals(JmxServletNature.NATUREID)){
					
					ret= true;
					Activator.getDefault().log("found JMX servlet project " + project.getName(), IStatus.INFO, null);
					
				}
				
			}
			
		} catch (CoreException e) {
			Activator.getDefault().log("error when filtering resource", IStatus.INFO, e);
		
		}
		finally { return ret;}
	}

}
