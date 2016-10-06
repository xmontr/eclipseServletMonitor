package remotejmxconnection.view;


import java.util.List;
import java.util.Vector;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import remotejmxconnection.RemoteConnectiontype;
import remotejmxconnection.RemoteJmxConnectionBean;
import remotejmxconnection.RemoteJmxConnectionManager;

public class RjmxContentProvider  implements ITreeContentProvider {
	
	private List<RemoteJmxConnectionBean> connections;
	



	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		connections=(List<RemoteJmxConnectionBean>)newInput;
		
	}

	@Override
	public Object[] getElements(Object inputElement) {		
		return RemoteConnectiontype.values();
	}



	@Override
	public Object getParent(Object element) {
		Object ret = null;
		if(element instanceof RemoteConnectiontype){
			
		
			
		}
		
		if(element instanceof RemoteJmxConnectionBean){	
		
			ret = ((RemoteJmxConnectionBean) element).getType();
			
			
		}
		
	
		return ret;
	}

	@Override
	public boolean hasChildren(Object element) {
		boolean ret=false;
		
		if(element instanceof RemoteConnectiontype){			
			RemoteConnectiontype cur = (RemoteConnectiontype)element;
			
			 for (RemoteJmxConnectionBean remoteJmxConnectionBean : connections) {
				 
				if( remoteJmxConnectionBean.getType().equals(cur))
				 { ret =true;break;}
			}				
			}		
		return ret;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		Vector<RemoteJmxConnectionBean> ret = new Vector<RemoteJmxConnectionBean>();
		if(parentElement instanceof RemoteConnectiontype){
			
			
			for (RemoteJmxConnectionBean remoteJmxConnectionBean : connections) {
				RemoteConnectiontype theType = (RemoteConnectiontype)parentElement;
				if(remoteJmxConnectionBean.getType().equals(theType )){
					
					ret.add(remoteJmxConnectionBean);
				}
				
			}
			
			
			
			
		}
		return ret.toArray();
	}

	

}
