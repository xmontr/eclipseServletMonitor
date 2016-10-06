package jmxservleteditor.template;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;


public class TemplateViewComposite extends Composite {

	private ListViewer systemTemplateViewer;
	private ListViewer localTemplateViewer;

	public TemplateViewComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(3, false));
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		
		Label lblLocalTemplate = new Label(this, SWT.NONE);
		lblLocalTemplate.setText("local template:");
		new Label(this, SWT.NONE);
		
		localTemplateViewer = new ListViewer(this, SWT.BORDER | SWT.V_SCROLL);
		org.eclipse.swt.widgets.List list = localTemplateViewer.getList();
		list.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		
		
		Label lblSystemTemplate = new Label(this, SWT.NONE);
		lblSystemTemplate.setText("system template");
		new Label(this, SWT.NONE);
		
		systemTemplateViewer = new ListViewer(this, SWT.BORDER | SWT.V_SCROLL);
		org.eclipse.swt.widgets.List list_1 = systemTemplateViewer.getList();
		list_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		
		
		IBaseLabelProvider templateLabelProvider = new TemplateLabelprovider();
		
		IContentProvider templateProvider = new TemplateListProvider();
		
		systemTemplateViewer.setContentProvider(templateProvider );
		systemTemplateViewer.setLabelProvider(templateLabelProvider);
		

		localTemplateViewer.setContentProvider(templateProvider);
		localTemplateViewer.setLabelProvider(templateLabelProvider );
		refresh();
	}
	
	
	
	
	public ListViewer getSystemTemplateViewer() {
		return systemTemplateViewer;
	}




	public ListViewer getLocalTemplateViewer() {
		return localTemplateViewer;
	}




	public void refresh() {
		List<Template> local = new ArrayList<Template>();
		List<Template> system = new ArrayList<Template>();
		
		List<Template> fulllist = TemplateManager.getInstance().getTemplates();
		
		for (Template template : fulllist) {
			if(template.isSystem()){
				system.add(template);
			}else {
				local.add(template);	
			}
			
		}
		
	localTemplateViewer.setInput(local);
	systemTemplateViewer.setInput(system);
		
		
	}

	

}
