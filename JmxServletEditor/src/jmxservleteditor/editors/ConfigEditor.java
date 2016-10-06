package jmxservleteditor.editors;



import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import jmxservleteditor.Activator;
import jmxservleteditor.composite.ConnectionConfigEditor;
import jmxservleteditor.composite.VisualConfigEditor;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.part.MultiPageEditorPart;



















import remotejmxconnection.RemoteJmxConnectionBean;
import remotejmxconnection.RemoteJmxConnectionManager;

public class ConfigEditor extends MultiPageEditorPart implements PropertyChangeListener {

	
	private static final int SOURCE_INDEX =1;
	private static final int DESIGN_INDEX = 2;
	private static final int CONNECTION_INDEX = 0;
	private XMLEditor xmleditor;
	private VisualConfigEditor visualConfigEditor;
	private boolean isPageModified;
	
	private ConnectionConfigEditorPart   connectionEditorPart;

	
	
	public ConfigEditor(){
		
		xmleditor = new XMLEditor();
		
		connectionEditorPart = new ConnectionConfigEditorPart();
	}
	
	
	@Override
	public boolean isDirty() {
		return  super.isDirty();
		
	}
	  
	
	@Override
	protected void handlePropertyChange(int propertyId) {
		 if (propertyId == IEditorPart.PROP_DIRTY)
			 super.handlePropertyChange(propertyId);
		super.handlePropertyChange(propertyId);
	}

	@Override
	protected void createPages() {
		createConnectionPage();

		
		
	}
	
	
	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		// TODO Auto-generated method stub
		super.init(site, input);
		xmleditor.init(site, input);
		connectionEditorPart.init(site, input);
		
	}
	
	
	

	private void createSourcePage() {
		
	
		try {
			Image fileimg = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
			
			 addPage(SOURCE_INDEX,xmleditor, xmleditor.getEditorInput());
			setPageText(SOURCE_INDEX, "Source");
			setPageImage(SOURCE_INDEX, fileimg);
		} catch (PartInitException e) {
			Activator.getDefault().log(e.getMessage(),IStatus.ERROR, e);
			
			
		}
		
	}
	
	
	public void designModified() 
	{ boolean wasDirty = isDirty();
	isPageModified = true; 
//	if (!wasDirty) firePropertyChange(IEditorPart.PROP_DIRTY);

	}
	
	public void gotoMarker(IMarker marker) {
		setActivePage(0);	
		
			 ((IGotoMarker) xmleditor.getAdapter(IGotoMarker.class))

		 .gotoMarker(marker);
		
		
	};
	
	
	@Override
	protected void pageChange(int newPageIndex) {
		
		if(newPageIndex == SOURCE_INDEX){ 
			if(isPageModified) {
				updateXmlEditorFromDesign(); 
				isPageModified=false;
			}
			}
		
		if(newPageIndex == DESIGN_INDEX){ 
			if(isDirty() || visualConfigEditor.isEmpty()){
			updateDesignFromTextEditor(); 
			isPageModified=false;
			}
		}
		super.pageChange(newPageIndex);
	}
	

	private void updateXmlEditorFromDesign() {
		 xmleditor.getDocumentProvider()
		 .getDocument(xmleditor.getEditorInput())
		 .set(visualConfigEditor
		 .getSavedsaveCounters(connectionEditorPart.getModel().getActiveConnection()   )); 
		
	}

	private void updateDesignFromTextEditor() {
		String doc = xmleditor.getDocumentProvider()
		 .getDocument(xmleditor.getEditorInput())

		 .get();
		visualConfigEditor.setSavedCounters(doc);
		
	}

	private void createCounterPage(RemoteJmxConnectionBean conn) {
		visualConfigEditor = new VisualConfigEditor(this.getContainer(),SWT.NONE);
		visualConfigEditor.setConnection(conn);
		//visualConfigEditor.setConfigEditor(this);
		visualConfigEditor.addPropertyChangeListener("designModified", this);
		Image designImage = Activator.getDefault().getImageRegistry().get(Activator.DESIGN_IMG);
		addPage(DESIGN_INDEX,visualConfigEditor );
		setPageText(DESIGN_INDEX, "Design");
		setPageImage(DESIGN_INDEX, designImage);
		
		
	}

	private void createConnectionPage() {
		
	try {
		
		addPage(CONNECTION_INDEX, connectionEditorPart, connectionEditorPart.getEditorInput());
		setPageText(CONNECTION_INDEX, connectionEditorPart.getPartName());
		setPageImage(CONNECTION_INDEX,connectionEditorPart.getTitleImage());
		connectionEditorPart.getModel().addPropertyChangeListener("connectionModified", this);
		
	
		
	} catch (PartInitException e) {
		Activator.getDefault().log("error when creating connection page for editor", IStatus.ERROR, e);
	
	}	
	
	
	
		
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		 if (getActivePage() == 0 && ( isPageModified || true ))
			 updateXmlEditorFromDesign();
		 isPageModified = false;
		xmleditor.doSave(monitor);
		
	}

	@Override
	public void doSaveAs() {
		 if (getActivePage() == 0 && isPageModified)
			 updateXmlEditorFromDesign();
		 isPageModified = false;
		 xmleditor.doSaveAs();
		 setInput(xmleditor.getEditorInput());
		// updateTitle();
	
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals("designModified")){
		designModified();
		}
		if(evt.getPropertyName().equals("connectionModified")){
		connectionModified((RemoteJmxConnectionBean)evt.getOldValue(),(RemoteJmxConnectionBean)evt.getNewValue());
		}
		
	}

/***
 * 
 *  change the connection source 
 * @param remoteJmxConnectionBean 
 * 
 * 
 * @param newValue
 */
	
	private void connectionModified(final RemoteJmxConnectionBean old, final RemoteJmxConnectionBean newconnection) {
		
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				if(old!= null){
					releasePageAndResources(old);
					}
					createSourcePage();				
					createCounterPage(newconnection);
				
			}
		};
		
		
		
		BusyIndicator.showWhile(Display.getCurrent(), r);
			
		
		/*
		if(old!= null){
		releasePageAndResources(old);
		}
		createSourcePage();				
		createCounterPage(newconnection);*/
		
	}


private void releasePageAndResources(RemoteJmxConnectionBean old) {
	//delete old connection
	RemoteJmxConnectionManager.getInstance().release(old);
	//remove old editors
	visualConfigEditor.dispose();
	
	
	
}


	
	

}
