package jmxservleteditor.composite;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import jmxservleteditor.template.NewTemplateDialog;
import jmxservleteditor.template.Template;
import jmxservleteditor.template.TemplateManager;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateListStrategy;
import org.eclipse.core.databinding.UpdateSetStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.IViewerObservableList;
import org.eclipse.jface.databinding.viewers.IViewerObservableSet;
import org.eclipse.jface.databinding.viewers.ObservableListTreeContentProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.wb.rcp.databinding.BeansListObservableFactory;
import org.eclipse.wb.rcp.databinding.TreeBeanAdvisor;
import org.eclipse.wb.rcp.databinding.TreeObservableLabelProvider;

import remotejmxconnection.RemoteJmxConnectionBean;
import cec.monitor.type.Attribut;
import cec.monitor.type.Counter;
import cec.monitor.type.impl.CounterImpl;

import org.eclipse.core.databinding.observable.masterdetail.IObservableFactory;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.viewers.TreeStructureAdvisor;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.set.ISetChangeListener;
import org.eclipse.core.databinding.observable.set.ListToSetAdapter;
import org.eclipse.core.databinding.observable.set.SetChangeEvent;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class CounterPanel extends Composite{
	
	
	
	
	private DataBindingContext m_bindingContext;
	
	
	private RemoteJmxConnectionBean theConnection;
	private Combo domainCombo;
	private Combo typeCombo;
	private Tree tree;
	private CounterPanelModel model =new CounterPanelModel();
	private ComboViewer domainComboViewer;


	private CheckboxTreeViewer treeViewer;
	private Label templatelabel;
	private Combo templateCombo;
	private ComboViewer templateComboViewer;
	private Composite composite;
	private Button btnSaveAs;
	private Button btnApply;
	
	


	public CounterPanel(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));
	
		
		
		Label domainLabel = new Label(this, SWT.NONE);
		domainLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		domainLabel.setText("Domain:");
		
		domainComboViewer = new ComboViewer(this, SWT.NONE); 
		
		
		
		
		
		
		domainCombo = domainComboViewer.getCombo();
		domainCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label typeLabel = new Label(this, SWT.NONE);
		typeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		typeLabel.setText("Type:");
		
		ComboViewer comboViewer_1 = new ComboViewer(this, SWT.NONE);
		typeCombo = comboViewer_1.getCombo();
		typeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label attributeLabel = new Label(this, SWT.NONE);
		attributeLabel.setText("JMX attribut:");
		
		
		
		treeViewer=new CheckboxTreeViewer(this,SWT.BORDER);
		/*treeViewer.addCheckStateListener(new ICheckStateListener() {
		      public void checkStateChanged(CheckStateChangedEvent event) {
		          // If the item is checked . . .
		          if (event.getChecked()) {
		            // . . . check all its children
		        	  treeViewer.setSubtreeChecked(event.getElement(), true);
		          }
		        }
		      });*/

	tree = treeViewer.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		templatelabel = new Label(this, SWT.NONE);
		templatelabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		templatelabel.setText("Template:");
		
		templateComboViewer = new ComboViewer(this, SWT.NONE);
		templateCombo = templateComboViewer.getCombo();
		templateCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(this, SWT.NONE);
		
		composite = new Composite(this, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		btnSaveAs = new Button(composite, SWT.NONE);
		btnSaveAs.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				
			NewTemplateDialog dialog = new NewTemplateDialog(e.display.getActiveShell());	
			dialog.create();
			if (dialog.open() == Window.OK) {
			  saveAsNewTemplate(dialog.getTemplateName());
			  model.reloadTemplates();

			}
				
			}

			
			
			

		});
		btnSaveAs.setText("Save as template");
		
		btnApply = new Button(composite, SWT.NONE);
		btnApply.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				applyTemplate();
			}
		});
		btnApply.setText("Apply selected template");
		m_bindingContext = initDataBindings();
		
		
		
	}
	

	public CounterPanelModel getModel() {
		return model;
	}


	public void setModel(CounterPanelModel model) {
		this.model = model;
	}
	
	@Override
	public void dispose() {
	
		super.dispose();
		tree.dispose();
		domainCombo.dispose();
		typeCombo.dispose();
	}


	public void expandTree(Object[] counterWithAttribute) {
		treeViewer.setExpandedElements(counterWithAttribute);
	
		
	}
	
	private void saveAsNewTemplate(String templateName) {
		Template newtemplate = new Template();
		newtemplate.setName(templateName);
		newtemplate.setType(model.getSelectedType());
		newtemplate.setDomain(model.getSelectedDomain());
		
		Set<Object> selection = model.getSelectedAttributes();
	Iterator<Object> it = selection.iterator();
	List<String> names = new ArrayList<String>();
	while (it.hasNext()) {
		Object object = (Object) it.next();
		if(object instanceof Attribut){
			Attribut a = (Attribut)object;
			names.add(a.getJmxAttribut());					
		}
		
		
	}
	newtemplate.setAttributesName(names);
	TemplateManager.getInstance().addTemplate(newtemplate);	
	TemplateManager.getInstance().saveTemplates();
	}
	
	
	
	public void applyTemplate(){
		if(model.getSelectedTemplate() != null ){
			Template theTemplate = TemplateManager.getInstance().getTemplateByName(model.getSelectedTemplate());
			
			Set<Object> theselection = model.buildAttributeFromTemplate(theTemplate);
			Object[] counterWithAttribute= model.buildCounterFromTemplate(theTemplate); 
			model.setSelectedAttributes(theselection);
			
			
			//expande the tree in panel
			expandTree(counterWithAttribute);
			
			
			
		}
		
		
		
		
	}
	
	
	
	
	
	
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableList itemsDomainComboObserveWidget = WidgetProperties.items().observe(domainCombo);
		IObservableList allDomainsModelObserveList = BeanProperties.list("allDomains").observe(model);
		bindingContext.bindList(itemsDomainComboObserveWidget, allDomainsModelObserveList, new UpdateListStrategy(UpdateListStrategy.POLICY_NEVER), null);
		//
		IObservableList itemsTypeComboObserveWidget = WidgetProperties.items().observe(typeCombo);
		IObservableList allTypesModelObserveList = BeanProperties.list("allTypes").observe(model);
		bindingContext.bindList(itemsTypeComboObserveWidget, allTypesModelObserveList, null, null);
		//
		IObservableValue observeSelectionDomainComboObserveWidget = WidgetProperties.selection().observe(domainCombo);
		IObservableValue selectedDomainModelObserveValue = BeanProperties.value("selectedDomain").observe(model);
		bindingContext.bindValue(observeSelectionDomainComboObserveWidget, selectedDomainModelObserveValue, null, null);
		//
		IObservableValue observeSelectionTypeComboObserveWidget = WidgetProperties.selection().observe(typeCombo);
		IObservableValue selectedTypeModelObserveValue = BeanProperties.value("selectedType").observe(model);
		bindingContext.bindValue(observeSelectionTypeComboObserveWidget, selectedTypeModelObserveValue, null, null);
		//
		IObservableFactory treeObservableFactory = BeansObservables.listFactory(Realm.getDefault(), "allAttributes", CounterPanelModel.class);
		TreeStructureAdvisor treeAdvisor = new CounterTreeAdvisor();
		ObservableListTreeContentProvider toto = new CounterTreeContentprovider(treeObservableFactory, treeAdvisor);
		treeViewer.setLabelProvider(new CounterTreeLabelProvider());
		treeViewer.setContentProvider(toto);
		//
		treeViewer.setInput(model);
		//
		IObservableSet observeCheckedElementsTreeViewer = ViewerProperties.checkedElements(Object.class).observe(treeViewer);
		IObservableSet selectedattribueModelObserveSet = BeanProperties.set("selectedAttributes").observe(model);
		bindingContext.bindSet(observeCheckedElementsTreeViewer, selectedattribueModelObserveSet, null, null);
		//
		IObservableList itemsTemplateComboObserveWidget = WidgetProperties.items().observe(templateCombo);
		IObservableList allTemplatesModelObserveList = BeanProperties.list("allTemplates").observe(model);
		bindingContext.bindList(itemsTemplateComboObserveWidget, allTemplatesModelObserveList, null, null);
		//
		IObservableValue observeSelectionTemplateComboObserveWidget = WidgetProperties.selection().observe(templateCombo);
		IObservableValue selectedTemplateModelObserveValue = BeanProperties.value("selectedTemplate").observe(model);
		bindingContext.bindValue(observeSelectionTemplateComboObserveWidget, selectedTemplateModelObserveValue, null, null);
		//
		return bindingContext;
	}
}
