package jmxservleteditor.wizard;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import jmxservleteditor.Activator;
import jmxservleteditor.nature.JmxServletNature;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.deferred.SetModel;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;

import cec.monitor.type.MonitorConfigDocument;
import remotejmxconnection.RemoteJmxConnectionManager;

public class JmxServletProject extends Wizard    implements IWorkbenchWizard{	
	
	private IWorkbench workbench;
	private IStructuredSelection selection;
	private JmxServletProjectFormPage formpage;

	public JmxServletProject(){
		setWindowTitle("JMX servlet project creation");
	
		
		
		
		
	}
	
	@Override
	public void addPages() {
		formpage=new JmxServletProjectFormPage("main");
		addPage(formpage );
	}

	@Override
	public boolean performFinish() {
		boolean ret=true;
		try {
			createSkeletonProject(formpage.getProjectName());
		} catch (CoreException e) {
			//
			//RemoteJmxConnectionManager.getInstance()
			Activator.getDefault().log(e.getMessage(), IStatus.ERROR, e);
			ret=false;
		}
		
		return ret;
	}

	private void createSkeletonProject(String projectName) throws CoreException {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IProject proj = root.getProject(projectName);
		
		
	
		
		//create project and structure
		proj.create(null, null);		
		proj.open(null);
		IProjectDescription description=proj.getDescription();
		
				
		String[] natures = description.getNatureIds();
	      String[] newNatures = new String[natures.length + 1];
	      System.arraycopy(natures, 0, newNatures, 0, natures.length);
	      newNatures[natures.length] = JmxServletNature.NATUREID;
	      description.setNatureIds(newNatures);
	      
	      proj.setDescription(description, null);
	      
		
		
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		workbench = workbench;
		 selection = selection;
		
	}

}
