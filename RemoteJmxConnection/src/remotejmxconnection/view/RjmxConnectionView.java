package remotejmxconnection.view;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import remotejmxconnection.Activator;
import remotejmxconnection.RemoteJmxConnectionBean;
import remotejmxconnection.RemoteJmxConnectionManager;
import remotejmxconnection.wizard.NewJmxConnectionWizard;

public class RjmxConnectionView extends ViewPart implements ISelectionChangedListener {
	
	
	public static String VIEWID="RemoteJmxConnection.view1";

	private TreeViewer treeViewer;
	private Action addItemAction;
	private IAction deleteItemAction;
	
	
	
	
	
	public RjmxConnectionView(){
		
	
		addItemAction = new Action("New connection") {
			public void run() { 
				addNewConnection();
			}


		};
		
		
		addItemAction.setImageDescriptor(Activator.getImageDescriptorById(Activator.CONNECTION_IMG));
		
		
		deleteItemAction = new Action("Delete") {
			public void run() { 
				deleteSelection();
			}




		};
		
		deleteItemAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
		deleteItemAction.setDisabledImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE_DISABLED));
		
		
		
		
		
		
		
		
		
	}
	
	
	



	@Override
	public void createPartControl(Composite parent) {
		
		treeViewer = new TreeViewer(parent);
		List<RemoteJmxConnectionBean> liconn = RemoteJmxConnectionManager.getInstance().getConnections();
		treeViewer.setContentProvider(new RjmxContentProvider());
		treeViewer.setLabelProvider(new RjmxLabelProvider());		
		
		treeViewer.setInput(RemoteJmxConnectionManager.getInstance().getConnections());
		treeViewer.expandAll();
		RemoteJmxConnectionManager.getInstance().setView(this);
		treeViewer.getTree().setHeaderVisible(true);
		treeViewer.getTree().setLinesVisible(true);
		getSite().setSelectionProvider(treeViewer);
		
		treeViewer.addSelectionChangedListener(this);
		
		createToolbarButtons();
		
		createContextMenu();
	}
	
	public void refresh(){
		List<RemoteJmxConnectionBean> liconn = RemoteJmxConnectionManager.getInstance().getConnections();
		treeViewer.setInput(liconn);
		treeViewer.refresh();
		
		
	}
	
	
	private void createToolbarButtons() {
		IToolBarManager toolBarMgr =
		getViewSite().getActionBars().getToolBarManager();
		toolBarMgr.add(new GroupMarker("edit"));
		
		toolBarMgr.add(addItemAction);
		toolBarMgr.add(deleteItemAction);
		

			
		toolBarMgr.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		}
	
	
	
	
	/**
	 * Create context menu.
	 */
	private void createContextMenu() {
		// Create menu manager.
		MenuManager menuMgr = new MenuManager();
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager mgr) {
				fillContextMenu(mgr);
			}
		});
		
		// Create menu.
		Menu menu = menuMgr.createContextMenu(treeViewer.getControl());
		treeViewer.getControl().setMenu(menu);
		
		// Register menu for extension.
		getSite().registerContextMenu(menuMgr, treeViewer);
	}
	
	
	private void fillContextMenu(IMenuManager mgr) {
		mgr.add(addItemAction);		
		mgr.add(deleteItemAction);
		
		mgr.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		mgr.add(new Separator());
		
	}
	

	








	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}
	
	private void deleteSelection() {
		ISelection selection = treeViewer.getSelection();
		if(selection instanceof IStructuredSelection){
			
			RemoteJmxConnectionBean el =(RemoteJmxConnectionBean)((IStructuredSelection) selection).getFirstElement();
			RemoteJmxConnectionManager.getInstance().removeConnection(el);
		}
		
	}
	
	private void addNewConnection() {
		// lauch wizzard id : RemoteJmxConnection.newConnectionWizard
		   IWizard wizard = new NewJmxConnectionWizard();
		    WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), wizard);
		    dialog.open();
		
	}






	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		ISelection selection = event.getSelection();
		if(selection instanceof IStructuredSelection){
		Object cuurent = ( (IStructuredSelection) selection).getFirstElement();
		if(cuurent instanceof RemoteJmxConnectionBean )	
			deleteItemAction.setEnabled(true);
		else
			deleteItemAction.setEnabled(false);
		}
		
		
	}



}
