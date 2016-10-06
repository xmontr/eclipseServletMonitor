package jmxservleteditor.composite;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import jmxservleteditor.template.ApplyTemplateDialog;
import jmxservleteditor.template.Template;
import jmxservleteditor.template.TemplateManager;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import cec.monitor.type.Attribut;
import cec.monitor.type.Counter;
import cec.monitor.type.MonitorConfig;
import cec.monitor.type.MonitorConfigDocument;
import remotejmxconnection.RemoteJmxConnectionBean;
import remotejmxconnection.RemoteJmxConnectionManager;

public class VisualConfigEditor extends Composite implements MouseListener, PropertyChangeListener {

	private static final String APPLY_TEMPLATES = "Apply Templates";
	private static final String BINDING = "binding";
	private static final String NEW_COUNTER = "new Counter";
	private static final String DELETE_COUNTER = "Delete counter";
	private static final String SAVE = "Save";
	private static final String ADD_COUNTER = "Add Counter";
	private CTabFolder counterManager;
	private MonitorConfigDocument configFile;
	
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	private DataBindingContext m_bindingContext;
	private RemoteJmxConnectionBean connection;
	

	public VisualConfigEditor(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));
		
		Composite counterAction = new Composite(this, SWT.NONE);
		counterAction.setLayout(new FillLayout(SWT.HORIZONTAL));
		counterAction.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		Button btnNewCounter = new Button(counterAction, SWT.NONE);
		btnNewCounter.addMouseListener(this);
		btnNewCounter.setText(ADD_COUNTER);
		btnNewCounter.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ADD));
		
		
		/*Button btnSaveButton = new Button(counterAction, SWT.NONE);
		btnSaveButton.setText(SAVE);
		btnSaveButton.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_ETOOL_SAVE_EDIT));
		btnSaveButton.addMouseListener(this);*/
		
		Button btnDeleteButton = new Button(counterAction, SWT.NONE);
		btnDeleteButton.addMouseListener(this);
		btnDeleteButton.setText(DELETE_COUNTER);
		btnDeleteButton.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_ETOOL_DELETE));
		
		Button btnApplyTemplate = new Button(counterAction, SWT.NONE);
		btnApplyTemplate.setText(APPLY_TEMPLATES);
		btnApplyTemplate.addMouseListener(this);
		new Label(this, SWT.NONE);
		
		counterManager = new CTabFolder(this, SWT.BORDER);
		counterManager.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		counterManager.setTabPosition(SWT.TOP);
		counterManager.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		new Label(this, SWT.NONE);
	m_bindingContext=new DataBindingContext();
	}
	
	

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDown(MouseEvent e) {
	if(	e.getSource() instanceof Button){
		Button b =(Button) e.getSource();
		if(b.getText().equals(ADD_COUNTER)){
			addNewCounter();
			
		}
		if(b.getText().equals(SAVE)){
			// notify changes to parent
			
		}
		if(b.getText().equals(DELETE_COUNTER)){
			removeCurrentCounter();

			
		}
		
		if(b.getText().equals(APPLY_TEMPLATES)){
			showApplyTemplate(b.getShell());

			
		}
		
		
	}
		
	}
	
	private void showApplyTemplate(Shell parentShell) {
		ApplyTemplateDialog dialog = new ApplyTemplateDialog(parentShell,connection);
		dialog.create();
		if (dialog.open() == Window.OK) {
		  applyTemplates(dialog.getChoice());

		}
		
	}



	private void applyTemplates(List<String> choice) {
		for (String templatename : choice) {			
			Template template= TemplateManager.getInstance().getTemplateByName(templatename);
			addCounterFromTemplate(template);
		}
		
	}



	private void addCounterFromTemplate(Template template) {
		
		CounterPanel newct = addNewCounter();
		CounterPanelModel model = newct.getModel();
		model.setSelectedDomain(template.getDomain());
		model.setSelectedType(template.getType());
		model.setSelectedTemplate(template.getName());
		newct.applyTemplate();
		
	}



	/**
	 * 
	 * 
	 * remove the current selected counter in the tab control
	 */

	private void removeCurrentCounter() {
		CTabItem current = counterManager.getSelection();
		if(current != null){
			removeCounter(current);
		}
		
	}
	
	
	private void removeCounter(CTabItem item){
		CounterPanel counterpanel =(CounterPanel)  item.getControl();
		Binding binding =(Binding)item.getData(BINDING);
		m_bindingContext.removeBinding(binding);
		removeCounterPanelListener(counterpanel);
		counterpanel.dispose();
		item.dispose();
		propertyChangeSupport.firePropertyChange("designModified", false,
				true);
		
	}
	
	/***
	 * 
	 * 
	 *  remove all counters
	 */
	
	private void removeAllCounter(){
		int nb = counterManager.getItemCount();
		CTabItem[] elements = counterManager.getItems();
		for (CTabItem cTabItem : elements) {
			removeCounter(cTabItem);
			
			
		}
		
		
		
	}

	public String getSavedsaveCounters(RemoteJmxConnectionBean remoteJmxConnectionBean) {
		int count = counterManager.getItemCount();
		configFile = MonitorConfigDocument.Factory.newInstance();

		MonitorConfig config = configFile.addNewMonitorConfig();
		 XmlCursor curs = configFile.newCursor();
		 //build comment 
		
		curs.toFirstChild();
		curs.insertComment(buildXmlComment(remoteJmxConnectionBean));
		Vector<Counter> counterList = new Vector<Counter>();
		
		
		for(int j=0;j<count;j++){// for all counter pannel 
		CTabItem tabct = counterManager.getItem(j);
		CounterPanel panel =(CounterPanel) tabct.getControl();
		List<Counter> li = panel.getModel().getCounters();
		
		for (Counter counter : li) { // for all counter
			counterList.add(counter);
		}

			
		}
		config.setCounterArray(counterList.toArray(new Counter[counterList.size()]));
		XmlOptions opt = new XmlOptions();
		opt.setSavePrettyPrint();
		return configFile.xmlText(opt);
		
	}

	private String buildXmlComment(
			RemoteJmxConnectionBean remoteJmxConnectionBean) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		StringBuilder sb =new StringBuilder();
		sb.append("\n").append("generation date:").append(dateFormat.format(cal.getTime()) ).append("\n");
		sb.append("host:").append(remoteJmxConnectionBean.getHost()).append("\n");
		sb.append("port:").append(remoteJmxConnectionBean.getPort()).append("\n");
		
		return sb.toString();
	}



	private CounterPanel addNewCounter() {
		CounterPanel ct = new CounterPanel(counterManager, SWT.NONE);
		ct.setFocus();
		
		
		
		ct.getModel().setConnection(connection );
		
		CTabItem tbtmCounter = new CTabItem(counterManager, SWT.NONE);
		tbtmCounter.setData(BINDING, bindTab2CounterPanel(tbtmCounter,ct));
		tbtmCounter.setText(NEW_COUNTER);
		tbtmCounter.setControl(ct);
		
		
		
		addCounterPanelListener(ct);
		return (ct);
		
	}
	
	public RemoteJmxConnectionBean getConnection() {
		return connection;
	}



	public void setConnection(RemoteJmxConnectionBean connection) {
		this.connection = connection;
	}



	/**
	 * 
	 *  link counterpanel.getModel.selectedType with text properties of 
	 * @param ct 
	 * @param tbtmCounter 
	 */
	
	private Binding bindTab2CounterPanel(CTabItem tbtmCounter, CounterPanel ct) {
	ISWTObservableValue observedText = WidgetProperties.text().observe(tbtmCounter);
	IObservableValue modelObservableValue = BeanProperties.value("selectedType").observe(ct.getModel());
	UpdateValueStrategy targetToModel = new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER);
	UpdateValueStrategy modelToTarget= new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE);
	
	Binding ret = m_bindingContext.bindValue(observedText, modelObservableValue, targetToModel, modelToTarget);
	
	return ret;	
	}



	private void addCounterPanelListener(CounterPanel ct) {
		ct.getModel().addPropertyChangeListener("selectedDomain", this);
		ct.getModel().addPropertyChangeListener("selectedType", this);
		ct.getModel().addPropertyChangeListener("selectedAttributes", this);
		
	}
	
	private void removeCounterPanelListener(CounterPanel ct) {
		ct.getModel().removePropertyChangeListener( this);
		
	}
	

	public void addCounter(Counter count){
		CounterPanel ct = new CounterPanel(counterManager, SWT.NONE);
		CounterPanelModel model = ct.getModel();
		
		addCounterPanelListener(ct);
		try {		

			
			CTabItem tbtmCounter = new CTabItem(counterManager, SWT.NONE);
			tbtmCounter.setData(BINDING, bindTab2CounterPanel(tbtmCounter,ct));
			tbtmCounter.setText(NEW_COUNTER);
			tbtmCounter.setControl(ct);
			
			//set connection
		model.setConnection(connection );	
		//set domain
			String theDomain = new ObjectName(count.getJmxObjectName()).getDomain();
			model.setSelectedDomain(theDomain);
			//set type
			String theType=count.getJmxType();
			model.setSelectedType(theType);
			//set selected attributes
			Attribut[] liatt = count.getAttributArray();
			Set<Object> theselection = model.buildAttributeSelection(liatt);
			Object[] counterWithAttribute= model.buildCounterSelection(liatt); 
			model.setSelectedAttributes(theselection);
			
			
			//expande the tree in panel
			ct.expandTree(counterWithAttribute);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			remotejmxconnection.RemoteJmxConnectionManager.getInstance().log(e.getMessage(), IStatus.ERROR, e);
		}
		
		
		
		
		
	}
	
	
	
	
	
	
	

	

	public void setSavedCounters(String theconfig) {
		removeAllCounter();
		try {
			configFile =  MonitorConfigDocument.Factory.parse(theconfig);
			Counter[] counterlist = configFile.getMonitorConfig().getCounterArray();
			for (Counter counter : counterlist) {
				addCounter(counter);
				
			}
			
			
		} catch (XmlException e) {
			// TODO Auto-generated catch block
			RemoteJmxConnectionManager.getInstance().log(e.getMessage(), IStatus.ERROR, e);
		}
		
		
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
	
		propertyChangeSupport.firePropertyChange("designModified", false,
				true);
	}
	
	
	public void addPropertyChangeListener(String propertyName,
		      PropertyChangeListener listener) {
		    propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
		  }

		  public void removePropertyChangeListener(PropertyChangeListener listener) {
		    propertyChangeSupport.removePropertyChangeListener(listener);
		  }
		  
		  /**
		   * 
		   * return wether the visualconfigeditor contains data or not
		   * @return
		   */
		  public boolean  isEmpty(){
			  return configFile ==null ? true :false ;
			  
			  
		  }
	

	@Override
	public void mouseUp(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void dispose() {		
		super.dispose();
		CTabItem[] tabs = counterManager.getItems();
		for (CTabItem cTabItem : tabs) {
			removeCounter(cTabItem);
			
		}
	}

}
