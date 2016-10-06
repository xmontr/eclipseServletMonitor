package remotejmxconnection.view;


import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import remotejmxconnection.RemoteConnectiontype;
import remotejmxconnection.RemoteJmxConnectionBean;

public class RjmxLabelProvider extends LabelProvider {
	
	
	@Override
	public String getText(Object element) {
		
		String ret=null;
		
		if(element instanceof RemoteConnectiontype){
			RemoteConnectiontype temp= (RemoteConnectiontype)element;
			ret=temp.toString();
			
	}
		
		if(element instanceof RemoteJmxConnectionBean){
			
			RemoteJmxConnectionBean temp=(RemoteJmxConnectionBean)element;
			ret= temp.getHost() +":" + temp.getPort();
			
		}
		
		
		
		return ret;
	}


@Override
public Image getImage(Object element) {
	Image ret = null;

	if(element instanceof RemoteConnectiontype){
	 ret = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
	
}

	return ret;

}

}
