package jmxservleteditor.wizard;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class JmxServletProjectFormPage extends WizardPage implements PropertyChangeListener {

	private JmxServletFormComposite composite;
	private String projectName;

	protected JmxServletProjectFormPage(String pageName) {
		super(pageName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createControl(Composite parent) {
		composite = new JmxServletFormComposite(parent,SWT.NONE);
		setControl(composite);
		composite.getModel().addPropertyChangeListener("projectName", this);

	}
	
	@Override
	public boolean isPageComplete() {
		// TODO Auto-generated method stub
		return composite.isFormCompleted();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		projectName=(String)evt.getNewValue();
		getContainer().updateButtons();
		
	}

	public String getProjectName() {
		return projectName;
	}
	
	
	

}
