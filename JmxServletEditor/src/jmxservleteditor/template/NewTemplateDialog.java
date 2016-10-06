package jmxservleteditor.template;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class NewTemplateDialog extends TitleAreaDialog {

	private Text txtName;
	private String newTemplateName;

	public NewTemplateDialog(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}
	
	
	
	@Override
	  public void create() {
	    super.create();
	    setTitle("New template cration");
	    setMessage("Create a new template by choosing a unique name", IMessageProvider.INFORMATION);
	  }
	
	
	@Override
	  protected Control createDialogArea(Composite parent) {
	    Composite area = (Composite) super.createDialogArea(parent);
	    Composite container = new Composite(area, SWT.NONE);
	    container.setLayoutData(new GridData(GridData.FILL_BOTH));
	    GridLayout layout = new GridLayout(2, false);
	    container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	    container.setLayout(layout);

	    createName(container);
	    

	    return area;
	  }
	
	private void createName(Composite container) {
	    Label lbtFirstName = new Label(container, SWT.NONE);
	    lbtFirstName.setText("Template Name");

	    GridData dataFirstName = new GridData();
	    dataFirstName.grabExcessHorizontalSpace = true;
	    dataFirstName.horizontalAlignment = GridData.FILL;

	    txtName = new Text(container, SWT.BORDER);
	    txtName.setLayoutData(dataFirstName);
	  }
	
	  @Override
	  protected boolean isResizable() {
	    return true;
	  }
	  
	  
	  @Override
	  protected void okPressed() {
	    saveInput();
	    super.okPressed();
	  }
	  
	  private void saveInput() {
		newTemplateName=txtName.getText();
		
	}



	public String getTemplateName(){
		  return this.newTemplateName;
		  
		  
		  
	  }
	
	

}
