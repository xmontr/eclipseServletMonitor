package jmxservleteditor.template;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import remotejmxconnection.RemoteJmxConnectionBean;
import remotejmxconnection.RemoteJmxConnectionManager;

public class TemplateView extends ViewPart implements ISelectionChangedListener {
	
	
	public static String VIEWID = "JmxServletEditor.view1";

	private IAction refreshItemAction;
	private IAction deleteItemAction;
	private TemplateViewComposite tpvc;
	
	
	
	
	
	public TemplateView() {
		
		refreshItemAction = new Action("referesh") {
			public void run() { 
				refresh();
			}


		};
		
		
		refreshItemAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_REDO));
		
		
		deleteItemAction = new Action("Delete") {
			public void run() { 
				deleteSelection();
			}




		};
		
		deleteItemAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
		deleteItemAction.setDisabledImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE_DISABLED));
		
		
		
		
		
		
	}

	protected void deleteSelection() {
		ISelection selection = tpvc.getLocalTemplateViewer().getSelection();
		if(selection instanceof IStructuredSelection){
			
			Template el =(Template)((IStructuredSelection) selection).getFirstElement();
			TemplateManager.getInstance().removeTemplate(el);
		}
		
	}

	protected void refresh() {
		tpvc.refresh();
		
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		ISelection selection = event.getSelection();
		if(selection instanceof IStructuredSelection){
		Object cuurent = ( (IStructuredSelection) selection).getFirstElement();
		if(cuurent instanceof Template ){	
			Template t = (Template)cuurent;
			if( t.isSystem())
				deleteItemAction.setEnabled(false);
			else {
				deleteItemAction.setEnabled(true);
				
			}
		}
		else {
			deleteItemAction.setEnabled(false);
			
		}
		}
		
	}

	@Override
	public void createPartControl(Composite parent) {
		tpvc = new TemplateViewComposite(parent, SWT.NONE);
		
		createToolbarButtons();
		
		tpvc.getLocalTemplateViewer().addSelectionChangedListener(this);
		tpvc.getSystemTemplateViewer().addSelectionChangedListener(this);
	}

	private void createToolbarButtons() {
		IToolBarManager toolBarMgr =
		getViewSite().getActionBars().getToolBarManager();
		toolBarMgr.add(new GroupMarker("edit"));
		
		toolBarMgr.add(refreshItemAction);
		toolBarMgr.add(deleteItemAction);
		

			
		toolBarMgr.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		
		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

}
