package jmxservleteditor.editors;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import jmxservleteditor.Activator;
import jmxservleteditor.composite.ConnectionConfigEditor;
import jmxservleteditor.composite.ConnectionConfigModel;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.WorkbenchPart;

public class ConnectionConfigEditorPart extends EditorPart implements PropertyChangeListener{
	
	private ConnectionConfigEditor composite;
	private ConnectionConfigModel model;
	

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		composite = new ConnectionConfigEditor(parent,SWT.NONE);
		 model = composite.getModel();
	
		
		
	}

	public ConnectionConfigModel getModel() {
		return model;
	}

	@Override
	public void setFocus() {
		composite.setFocus();
		
	}
	


	
	
	



	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		String tttext  =" select available remote jmx connection";
		setInput(input);
		setSite(site);
		setTitleImage(Activator.getDefault().getImageRegistry().get(Activator.CONNECTION_IMG));
		setTitleToolTip(tttext);
		setPartName("Remote Jmx Connection");
		setContentDescription("select a remote jmx connection");
		
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		firePartPropertyChanged("connectionModified", "", "");
		
	}
	
	
}
