package jmxservleteditor.explorer;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.navigator.CommonActionProvider;

import remotejmxconnection.Activator;

public class ExplorerActionProvider extends CommonActionProvider  {
	
	
	
	
	
	private Action addProjectAction;



	public  ExplorerActionProvider() {
		
		addProjectAction = new Action("New Project") {
			public void run() { 
				addNewProject();
			}

			


		};
		
		
		addProjectAction.setImageDescriptor(Activator.getImageDescriptorById(Activator.CONNECTION_IMG));
		
		
		
	}
	
	
	private void addNewProject() {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public void fillActionBars(IActionBars actionBars) {
		actionBars.getToolBarManager().add(new GroupMarker("edit"));
		actionBars.getToolBarManager().add(addProjectAction);
		updateActionBars();
		

}
	
}
