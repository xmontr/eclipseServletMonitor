package jmxservleteditor.preferences;

import java.net.MalformedURLException;
import java.net.URL;

import jmxservleteditor.Activator;




import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class JmxServletpreference extends FieldEditorPreferencePage implements IWorkbenchPreferencePage{

	public static final String URL = "url";
	private StringFieldEditor urlField;


	@Override
	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	 public JmxServletpreference() {
		 setPreferenceStore(

				 Activator.getDefault().getPreferenceStore()); 
		 setDescription("Jmx Monitoring config");
	}
	

	@Override
	protected void createFieldEditors() {
		urlField = new StringFieldEditor("urlfield", URL, getFieldEditorParent());
		urlField.setEmptyStringAllowed(false);
		urlField.setPreferenceName(URL);
		
		addField(urlField);
		
		
		
	}
	
	
	@Override
	public boolean performOk() {
		urlField.store();
		return true;
	}
	
	
	@Override
	protected void checkState() {
		if(isValid()){
			
			setValid(true);
		}else {
			
	setValid(false);		
			
		}
	}
	
	
	@Override
	public boolean isValid() {
boolean ret = false;

try {
	URL targeturl = new URL(urlField.getStringValue());
	ret=true;
	
} catch (MalformedURLException e) {

Activator.getDefault().log(" fail to retriev templates from url " + urlField.getStringValue() , Status.ERROR, e);
	setErrorMessage(e.getMessage());
	ret = false;
	e.printStackTrace();
}

return ret;
	}
	
	public void propertyChange(org.eclipse.jface.util.PropertyChangeEvent event) { super.propertyChange(event);

	if (event.getProperty().equals(FieldEditor.VALUE)) { if (event.getSource() == urlField  ) checkState();

	}

	}
	
	

}
