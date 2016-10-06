package jmxservleteditor.composite;

import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;

import remotejmxconnection.RemoteJmxConnectionBean;
import remotejmxconnection.RemoteJmxConnectionManager;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateSetStrategy;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.beans.IBeanListProperty;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.fieldassist.ControlDecoration;

public class ConnectionConfigEditor extends Composite{

	
	private DataBindingContext m_bindingContext;

	private ConnectionConfigModel model;
	private Combo combo;
	private ComboViewer comboViewer;

	public ConnectionConfigEditor(Composite parent, int style) {
		

		
		
		super(parent, style);		
		model = new ConnectionConfigModel() ;
		setLayout(new GridLayout(2, false));
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		
		Label lblConnection = new Label(this, SWT.NONE);
		lblConnection.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblConnection.setText("Connection:");
		
		comboViewer = new ComboViewer(this, SWT.NONE);
		combo = comboViewer.getCombo();
		
		comboViewer.setContentProvider(ArrayContentProvider.getInstance());
		comboViewer.setLabelProvider(new LabelProvider(){
			@Override
			public String getText(Object element) {
				RemoteJmxConnectionBean con = (RemoteJmxConnectionBean)element;
				StringBuffer ret = new StringBuffer();
				ret.append(con.getHost()).append(":").append(con.getPort());
				
				return ret.toString();
			}
			
			
		});
		comboViewer.setInput(model.getConnections());
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		m_bindingContext = initDataBindings();

	
		
	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeSingleSelectionComboViewer = ViewerProperties.singleSelection().observe(comboViewer);
		IObservableValue activeConnectionModelObserveValue = PojoProperties.value("activeConnection").observe(model);
		UpdateValueStrategy targetToModel= new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE);
		IValidator connectionValidator = new IValidator() {
			
			@Override
			public IStatus validate(Object value) {	
				if(value == null){
					return ValidationStatus.error(" Connection cannot be null");
				}
				IStatus ret = ValidationStatus.ok();
				
				RemoteJmxConnectionBean con = (RemoteJmxConnectionBean)value;
				try{
					RemoteJmxConnectionManager.getInstance().ActivateConnection(con);
				
			}catch( Exception e){
			ret=ValidationStatus.error(e.getMessage());	
				
			}
				return ret;
			}
		};
		targetToModel.setBeforeSetValidator(connectionValidator );
		
	Binding comboBinding = bindingContext.bindValue(observeSingleSelectionComboViewer, activeConnectionModelObserveValue, targetToModel, null);
	ControlDecorationSupport.create(comboBinding,SWT.TOP | SWT.LEFT);
		return bindingContext;
	}
	public ConnectionConfigModel getModel() {
		return model;
	}
	public void setModel(ConnectionConfigModel model) {
		this.model = model;
	}
}
