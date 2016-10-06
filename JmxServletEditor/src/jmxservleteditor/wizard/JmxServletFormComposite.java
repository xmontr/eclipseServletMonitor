package jmxservleteditor.wizard;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateSetStrategy;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.fieldassist.ControlDecoration;

public class JmxServletFormComposite extends Composite {
	private DataBindingContext m_bindingContext;
	private Text text;
	
	
	private JmxServletProjectModel model =   new JmxServletProjectModel();
	

	public JmxServletFormComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		
		Label lblProjectName = new Label(this, SWT.NONE);
		lblProjectName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblProjectName.setText("Project Name:");
		
		text = new Text(this, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		
	
		m_bindingContext = initDataBindings();
		// TODO Auto-generated constructor stub
	}

	public JmxServletProjectModel getModel() {
		return model;
	}

	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeTextTextObserveWidget = WidgetProperties.text(SWT.Modify).observe(text);
		IObservableValue projectNameModelObserveValue = PojoProperties.value("projectName").observe(model);
		UpdateValueStrategy target2model = new UpdateValueStrategy(UpdateSetStrategy.POLICY_UPDATE);
		
		target2model.setBeforeSetValidator(new IValidator() {
			
			@Override
			public IStatus validate(Object value) {
				String el =(String)value;
				IStatus ret=ValidationStatus.ok();
				if(el.isEmpty()){
					ret =ValidationStatus.error(" Project Name cannnot be empty");
				}else {
					if(ResourcesPlugin.getWorkspace().getRoot().getProject(el).exists()){
						ret=ValidationStatus.error("project already exist");
					}
				}
				
				
				return ret;
			}
		});
		
		Binding binding = bindingContext.bindValue(observeTextTextObserveWidget, projectNameModelObserveValue, target2model , null);
		//
		 ControlDecorationSupport.create(binding, SWT.LEFT | SWT.TOP);
		
		return bindingContext;
	}
	
	
	public boolean isFormCompleted(){
		return model.getProjectName()==null ? false : true ;
		
	}
}
