package remotejmxconnection.composite;

import org.eclipse.core.databinding.AggregateValidationStatus;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import remotejmxconnection.RemoteConnectiontype;
import remotejmxconnection.RemoteJmxConnectionBean;
import remotejmxconnection.convertor.ConnectionTypeConvertor;
import remotejmxconnection.validator.NonEmptyStringFieldValidator;

public class JmxConnectionForm extends Composite {
	private DataBindingContext m_bindingContext;
	private Text hostInput;
	private Text portInput;
	private Combo typeInput;
	private RemoteJmxConnectionBean model = new RemoteJmxConnectionBean();
	
	private IChangeListener changelistener;
	private AggregateValidationStatus aggregateValidationStatus;
	private Text userInput;
	private Text passwordInput;

	public JmxConnectionForm(Composite parent, int style) {
		super(parent, style);
		setLayout(new FormLayout());
		
		Label lblHost = new Label(this, SWT.NONE);
		lblHost.setAlignment(SWT.RIGHT);
		FormData fd_lblHost = new FormData();
		fd_lblHost.right = new FormAttachment(0, 97);
		fd_lblHost.top = new FormAttachment(0, 29);
		fd_lblHost.left = new FormAttachment(0, 20);
		lblHost.setLayoutData(fd_lblHost);
		lblHost.setText("host");
		
		Label lblPort = new Label(this, SWT.NONE);
		lblPort.setAlignment(SWT.RIGHT);
		FormData fd_lblPort = new FormData();
		fd_lblPort.right = new FormAttachment(lblHost, 0, SWT.RIGHT);
		fd_lblPort.top = new FormAttachment(lblHost, 29);
		fd_lblPort.left = new FormAttachment(0);
		lblPort.setLayoutData(fd_lblPort);
		lblPort.setText("port");
		
		hostInput = new Text(this, SWT.BORDER);
		FormData fd_hostInput = new FormData();
		fd_hostInput.top = new FormAttachment(0, 23);
		fd_hostInput.right = new FormAttachment(100, -152);
		fd_hostInput.left = new FormAttachment(lblHost, 20);
		hostInput.setLayoutData(fd_hostInput);
		
		portInput = new Text(this, SWT.BORDER);
		FormData fd_portInput = new FormData();
		fd_portInput.top = new FormAttachment(hostInput, 29);
		fd_portInput.left = new FormAttachment(hostInput, 0, SWT.LEFT);
		fd_portInput.right = new FormAttachment(hostInput, 0, SWT.RIGHT);
		portInput.setLayoutData(fd_portInput);
		
		Label lblType = new Label(this, SWT.NONE);
		lblType.setAlignment(SWT.RIGHT);
		FormData fd_lblType = new FormData();
		fd_lblType.right = new FormAttachment(lblHost, 0, SWT.RIGHT);
		fd_lblType.top = new FormAttachment(lblPort, 22);
		fd_lblType.left = new FormAttachment(0);
		lblType.setLayoutData(fd_lblType);
		lblType.setText("type");
		
		typeInput = new Combo(this, SWT.NONE);
		FormData fd_typeInput = new FormData();
		fd_typeInput.top = new FormAttachment(portInput, 16);
		fd_typeInput.right = new FormAttachment(100, -152);
		fd_typeInput.left = new FormAttachment(lblType, 20);
		typeInput.setLayoutData(fd_typeInput);
		
		userInput = new Text(this, SWT.BORDER);
		FormData fd_userInput = new FormData();
		fd_userInput.left = new FormAttachment(hostInput, 0, SWT.LEFT);
		fd_userInput.top = new FormAttachment(typeInput, 6);
		fd_userInput.right = new FormAttachment(100, -152);
		userInput.setLayoutData(fd_userInput);
		
		Label lblUser = new Label(this, SWT.NONE);
		FormData fd_lblUser = new FormData();
		fd_lblUser.bottom = new FormAttachment(userInput, 0, SWT.BOTTOM);
		fd_lblUser.right = new FormAttachment(userInput, -17);
		lblUser.setLayoutData(fd_lblUser);
		lblUser.setText("user");
		
		Label lblPassword = new Label(this, SWT.NONE);
		FormData fd_lblPassword = new FormData();
		fd_lblPassword.top = new FormAttachment(lblUser, 17);
		fd_lblPassword.right = new FormAttachment(lblHost, 0, SWT.RIGHT);
		lblPassword.setLayoutData(fd_lblPassword);
		lblPassword.setText("password");
		
		passwordInput = new Text(this, SWT.BORDER);
		FormData fd_passwordInput = new FormData();
		fd_passwordInput.right = new FormAttachment(hostInput, 181);
		fd_passwordInput.bottom = new FormAttachment(lblPassword, 0, SWT.BOTTOM);
		fd_passwordInput.left = new FormAttachment(hostInput, 0, SWT.LEFT);
		passwordInput.setLayoutData(fd_passwordInput);
		buildCombType();
		m_bindingContext = initDataBindings();
		
		aggregateValidationStatus = new AggregateValidationStatus(
				 m_bindingContext,
		     AggregateValidationStatus.MAX_SEVERITY);
		
	}

	public AggregateValidationStatus getAggregateValidationStatus() {
		return aggregateValidationStatus;
	}

	public RemoteJmxConnectionBean getModel() {
		return model;
	}

	public void setModel(RemoteJmxConnectionBean model) {
		this.model = model;
	}

	private void buildCombType() {
		for (RemoteConnectiontype type : RemoteConnectiontype.values()) {
			typeInput.add(type.name());
			
			
			
		}
		
	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeTextHostInputObserveWidget = WidgetProperties.text(SWT.Modify).observe(hostInput);
		IObservableValue hostModelObserveValue = PojoProperties.value("host").observe(model);
		UpdateValueStrategy hostTarget2modelStrategy = new UpdateValueStrategy();
		IValidator validator = new NonEmptyStringFieldValidator("host");
		hostTarget2modelStrategy.setAfterGetValidator(validator);
		UpdateValueStrategy hostModel2targetStartegy = new UpdateValueStrategy();
		Binding hostbbv = bindingContext.bindValue(observeTextHostInputObserveWidget, hostModelObserveValue, hostTarget2modelStrategy, hostModel2targetStartegy);
		//
		IObservableValue observeTextPortInputObserveWidget = WidgetProperties.text(SWT.Modify).observe(portInput);
		IObservableValue portModelObserveValue = PojoProperties.value("port").observe(model);
		bindingContext.bindValue(observeTextPortInputObserveWidget, portModelObserveValue, null, null);
		//
		IObservableValue observeTextTypeInputObserveWidget = WidgetProperties.selection().observe(typeInput);
		IObservableValue typeModelObserveValue = PojoProperties.value("type").observe(model);
		UpdateValueStrategy typetarget2model = new UpdateValueStrategy();
		IConverter cv2 = new ConnectionTypeConvertor(String.class,RemoteConnectiontype.class );
		typetarget2model.setConverter(cv2);
		IValidator validator2 = new NonEmptyStringFieldValidator("Type of connection");
		typetarget2model.setAfterGetValidator(validator2);
		UpdateValueStrategy typemodel2target = new UpdateValueStrategy();
		IConverter cv1 = new ConnectionTypeConvertor(RemoteConnectiontype.class, String.class);
		typemodel2target.setConverter(cv1);
		Binding typebindingvalue = bindingContext.bindValue(observeTextTypeInputObserveWidget, typeModelObserveValue, typetarget2model, typemodel2target);
		//
		IObservableValue observeTextUserInputObserveWidget = WidgetProperties.text(SWT.Modify).observe(userInput);
		IObservableValue userModelObserveValue = PojoProperties.value("user").observe(model);
		bindingContext.bindValue(observeTextUserInputObserveWidget, userModelObserveValue, null, null);
		//
		IObservableValue observeTextPasswordInputObserveWidget = WidgetProperties.text(SWT.Modify).observe(passwordInput);
		IObservableValue passwordModelObserveValue = PojoProperties.value("password").observe(model);
		bindingContext.bindValue(observeTextPasswordInputObserveWidget, passwordModelObserveValue, null, null);
		//
		return bindingContext;
	}
}
