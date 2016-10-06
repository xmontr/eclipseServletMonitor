package jmxservleteditor.template;

import org.eclipse.jface.viewers.LabelProvider;

import remotejmxconnection.RemoteConnectiontype;

public class TemplateLabelprovider extends LabelProvider {
	
	
	@Override
	public String getText(Object element) {
		
		StringBuffer sb = new StringBuffer();
		
		if(element instanceof Template){
			Template temp= (Template )element;
			
			sb.append(temp.getName()).append(" (" ).append(temp.getDomain()).append(")");
			
			
	}
		
	
	
	
	return sb.toString();

}
	
}
