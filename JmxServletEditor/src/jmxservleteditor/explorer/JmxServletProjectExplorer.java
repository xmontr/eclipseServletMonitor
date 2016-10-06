package jmxservleteditor.explorer;

import jmxservleteditor.wizard.JmxServletProject;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.navigator.resources.ResourceToItemsMapper;



public class JmxServletProjectExplorer extends org.eclipse.ui.navigator.CommonNavigator {
	
	
	public static String View_ID="jmxservleteditor.explorer.JmxServletProjectExplorer.id";
	private IAction addProjectAction;
	
	
	public JmxServletProjectExplorer()  {
		
		addProjectAction = new Action("New JMX Project") {
			public void run() { 
				addNewProject();
			}

			


		};
		
		addProjectAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_NEW_WIZARD));
		
		

		
	}
	
	protected void addNewProject() {
		// lauch wizzard id : RemoteJmxConnection.newConnectionWizard
		   IWizard wizard = new JmxServletProject();
		    WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), wizard);
		    dialog.open();
		
	}

	@Override
	public void createPartControl(Composite aParent) {
		super.createPartControl(aParent);
		
		if (!false)
			getCommonViewer().setMapper(new ResourceToItemsMapper(getCommonViewer()));
		
		
		createToolbarButtons();
		
	}

	private void createToolbarButtons() {

		IToolBarManager toolBarMgr =
		getViewSite().getActionBars().getToolBarManager();
		toolBarMgr.add(new GroupMarker("edit"));
		
		toolBarMgr.add(addProjectAction);
		
		

			
		toolBarMgr.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		
	}
	
	
	

}
