package jmxservleteditor.perspective;

import jmxservleteditor.explorer.JmxServletProjectExplorer;
import jmxservleteditor.template.TemplateView;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import remotejmxconnection.view.RjmxConnectionView;

public class JmxServletPerspectiveFactory implements IPerspectiveFactory{

	@Override
	public void createInitialLayout(IPageLayout layout) {


		String editorArea = layout.getEditorArea();
		
		//jmxservlet.navigator.id
		//IPageLayout.ID_PROJECT_EXPLORER
		
		 // Put the project explorer view on the left.
		
		layout.addView(

				JmxServletProjectExplorer.View_ID,

		IPageLayout.LEFT,

		0.25f, editorArea);
		
		
		 // Put the connection view on the bottom with  the template view. 
		
		
		
		IFolderLayout bottom = layout.createFolder( "bottom",

		IPageLayout.BOTTOM,

		0.66f, editorArea);
		bottom.addView(RjmxConnectionView.VIEWID );
		bottom.addView(TemplateView.VIEWID);
		bottom.addView("org.eclipse.pde.runtime.LogView");
		
		
		
		
		
		
		
	}

}
