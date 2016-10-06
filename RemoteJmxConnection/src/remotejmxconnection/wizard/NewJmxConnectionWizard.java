package remotejmxconnection.wizard;

import java.awt.Label;

import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;

import remotejmxconnection.RemoteJmxConnectionBean;
import remotejmxconnection.RemoteJmxConnectionManager;
import remotejmxconnection.composite.JmxConnectionForm;




public class NewJmxConnectionWizard extends Wizard implements IWorkbenchWizard{
	
	
	
	private static  class Page1 extends WizardPage implements IValueChangeListener     {

		private JmxConnectionForm form;
		private boolean pageComplete;
		protected Page1() {
			super("connection details");
		
		}

		@Override
		public void createControl(Composite parent) {
			form = new JmxConnectionForm(parent, SWT.NONE);
			setControl(form);
			this.setDescription("Enter JMX connection details");
			form.getAggregateValidationStatus().addValueChangeListener(this);
			
		}
	
		
		
		
		
		
		
		public RemoteJmxConnectionBean getModel(){
			return form.getModel();
			
		}
		
		@Override
		public boolean isPageComplete() {
			// TODO Auto-generated method stub
			return pageComplete;
		}

		@Override
		public void handleValueChange(ValueChangeEvent event) {
			IStatus newVal = (IStatus) event.diff.getNewValue();
		      if (!newVal.isOK()) {
		       pageComplete = false;
		       
		      }else {
		    	  pageComplete = true;
		    	  
		      }
		      IWizardContainer cont = getContainer();
		      if(cont != null ) getContainer().updateButtons();
			
		}



	

	
		
		
	}
	
	
	private Page1 page1;
	private IWorkbench workbench;
	private IStructuredSelection selection;

	public NewJmxConnectionWizard(){
		super();
		setWindowTitle("Remote JMX connection creation");
		page1=new Page1();
		
		
		
	}
	
	
	@Override
	public IWizardPage getStartingPage() {
		// TODO Auto-generated method stub
		return page1;
	}
	
	@Override
	public void addPages() {
		// TODO Auto-generated method stub
		addPage(page1);
	}
	


	@Override
	public boolean performFinish() 
	{
		RemoteJmxConnectionBean model = page1.getModel();
		RemoteJmxConnectionManager.getInstance().loadConnections();
		RemoteJmxConnectionManager.getInstance().addConnection(model);
		RemoteJmxConnectionManager.getInstance().saveConnections();
		RemoteJmxConnectionManager.getInstance().refresh();
		return true;
	}
	

	


	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
		
	}

}
