package jmxservleteditor.template;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.plaf.basic.BasicBorders.RadioButtonBorder;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;

import remotejmxconnection.RemoteJmxConnectionBean;
import remotejmxconnection.RemoteJmxConnectionManager;

public class ApplyTemplateDialog extends TitleAreaDialog {
	
	private List<String> choice;
	
	private List<Group>   tpgroups;
	
	private RemoteJmxConnectionBean connection;
	

	public ApplyTemplateDialog(Shell parentShell, RemoteJmxConnectionBean connection) {
		super(parentShell);
		tpgroups = new ArrayList<Group>();
		this.connection=connection;
	}
	
	@Override
	  public void create() {
	    super.create();
	    setTitle("Applying template");
	    setMessage("Choose the different templates you want to be applyed", IMessageProvider.INFORMATION);
	  }
	
	@Override
	  protected Control createDialogArea(Composite parent) {
	    Composite area = (Composite) super.createDialogArea(parent);
	    Composite container = new Composite(area, SWT.NONE);
	    container.setLayoutData(new GridData(GridData.FILL_BOTH));
	    GridLayout layout = new GridLayout(2, false);
	    container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	    container.setLayout(layout);
	    
	    Map<String,List<Template>> groupedTemplate = TemplateManager.getInstance().getTemplatesGroupedByType();
	    Iterator<String> git = groupedTemplate.keySet().iterator();
	    tpgroups.clear();
	    while (git.hasNext()) {
			String key = (String) git.next();
			String[] valu = key.split(":");
			String domain = valu[0];
			String type= valu[1];
			
			List<Template> tpl = groupedTemplate.get(key);
			addTemplateGroup(container,domain,type,tpl);
			
		}
	    
	    
	    
	    
	    return area;
	}

	private void addTemplateGroup(Composite container, String domain,
			String type, List<Template> tpl) {		
		Group group = new Group(container, SWT.SHADOW_IN);
		tpgroups.add(group);
		group.setText(type);
		boolean isTypeAvailable = RemoteJmxConnectionManager.getInstance().isTypeAvailable(connection,domain,type);
		group.setEnabled(isTypeAvailable );

		GridData layoutData = new GridData();
		layoutData.grabExcessHorizontalSpace=true;
		group.setLayoutData(layoutData);
		group.setLayout(new RowLayout(SWT.VERTICAL));
		for (Template template : tpl) {
			new Button(group, tpl.size()>1 ? SWT.RADIO : SWT.CHECK).setText(template.getName());
			
		}
		
		
	}



	  @Override
	  protected void okPressed() {
	    saveInput();
	    super.okPressed();
	  }
	  
	  

	private void saveInput() {
		List<String> newlist = new ArrayList<String>();
		for (Group gr : tpgroups) {
			Control[] li = gr.getChildren();
			for (Control control : li) {
				Button b = (Button)control;
				if(b.getSelection()){
					newlist.add(b.getText());
				}
			}
			
		}		
		 choice =newlist;		
	}

	public List<String> getChoice() {
		return choice;
	}
	
	
	
	

}
