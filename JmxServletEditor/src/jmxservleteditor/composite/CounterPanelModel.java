package jmxservleteditor.composite;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import jmxservleteditor.template.Template;
import jmxservleteditor.template.TemplateManager;

import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.set.ObservableSet;
import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.jface.viewers.TreeViewer;

import remotejmxconnection.RemoteJmxConnectionBean;
import remotejmxconnection.RemoteJmxConnectionManager;
import cec.monitor.type.Attribut;
import cec.monitor.type.Counter;

public class CounterPanelModel  {
	
	
	
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	
	private List<String> allDomains;
	private List<String> allTypes;
	private List<Counter>   allAttributes;
	private String selectedDomain;
	private String selectedType;
	private Set<Object>  selectedAttributes;
	private List<String> allTemplates;
	private String selectedTemplate;
	
	public List<String> getAllTemplates() {
		return allTemplates;
	}

	public void setAllTemplates(List<String> allTemplates) {
		List<String> old = this.allTemplates;
		this.allTemplates = allTemplates;
		propertyChangeSupport.firePropertyChange("allTemplates", old,this.allTemplates);

	}

	public String getSelectedTemplate() {
		return selectedTemplate;
	}

	public void setSelectedTemplate(String selectedTemplate) {
		propertyChangeSupport.firePropertyChange("selectedTemplate", this.selectedTemplate,
				this.selectedTemplate = selectedTemplate);

	}

	private RemoteJmxConnectionBean connection;
	
	public void addPropertyChangeListener(String propertyName,
		      PropertyChangeListener listener) {
		    propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
		  }

		  public void removePropertyChangeListener(PropertyChangeListener listener) {
		    propertyChangeSupport.removePropertyChangeListener(listener);
		  }
	
	public List<String> getAllDomains() {
		return allDomains;
	}

	public void setAllDomains(List<String> allDomains) {
		List<String> old = this.allDomains;
		this.allDomains = allDomains;
		propertyChangeSupport.firePropertyChange("allDomains", old,this.allDomains);
		
	}

	public List<String> getAllTypes() {
		return allTypes;
	}

	public void setAllTypes(List<String> allTypes) {
		propertyChangeSupport.firePropertyChange("allTypes", this.allTypes,
				this.allTypes = allTypes);
		
	}

	public List<Counter> getAllAttributes() {
		return allAttributes;
	}

	public void setAllAttributes(List<Counter> allAttributes) {
		propertyChangeSupport.firePropertyChange("allAttributes", this.allAttributes,
				this.allAttributes = allAttributes);
		
	}

	public String getSelectedDomain() {
		return selectedDomain;
	}

	public void setSelectedDomain(String selectedDomain) {
		propertyChangeSupport.firePropertyChange("selectedDomain", this.selectedDomain,
				this.selectedDomain = selectedDomain);
		List<String> at = RemoteJmxConnectionManager.getInstance().getAllTypes(connection,selectedDomain);
		setAllTypes(at);
		setSelectedType(null);
	}

	public String getSelectedType() {
		return selectedType;
	}

	public void setSelectedType(String selectedType) {
		propertyChangeSupport.firePropertyChange("selectedType", this.selectedType,
				this.selectedType = selectedType);
		if(selectedType != null){
			//les templates
			List<String> litemplatename = TemplateManager.getInstance().getTemplateNames(selectedType,selectedDomain);
			Collections.sort(litemplatename);
			setAllTemplates(litemplatename);
			
			
			
			//les counters
		List<Counter> at = RemoteJmxConnectionManager.getInstance().getAllCounter(connection, selectedDomain, selectedType);
		if(at.size()>0){
			Counter  ct =(Counter) at.get(0);
			Attribut[] temp = ct.getAttributArray();
		}
		
		setAllAttributes(at);
		
	}
	}

	public  Set<Object>  getSelectedAttributes() {
		return selectedAttributes;
	}

	public void setSelectedAttributes( Set<Object> selectedAttributes) {
		propertyChangeSupport.firePropertyChange("selectedAttributes", this.selectedAttributes,
				this.selectedAttributes = selectedAttributes);
		
	}

	public RemoteJmxConnectionBean getConnection() {
		return connection;
	}

	public void setConnection(RemoteJmxConnectionBean connection) {
		this.connection = connection;
		
		List<String> dms = RemoteJmxConnectionManager.getInstance().getAllDomains(connection);
		setAllDomains(dms );
	}


	
	public CounterPanelModel( ){	
		
		selectedAttributes = new HashSet<Object>();
		
	
	}
	
	
	/**
	 * 
	 * 
	 *   give the list of counter selected with the panel
	 * @return
	 */
	public List<Counter> getCounters(){
		ArrayList<Counter> ret = new ArrayList<Counter>();
		Iterator it = selectedAttributes.iterator();
		while (it.hasNext()) {
			Object object = (Object) it.next();
			if( object instanceof Attribut){
				addAttributeToListCounter(ret,(Attribut)object);				
			}	
			
		}		
		return ret;
	}

	
	/**
	 * 
	 *  add the attibute to corresponding counter in the list 
	 *  create the counter if necessary
	 * @param ret
	 * @param att
	 */
	private void addAttributeToListCounter(List<Counter> ret, Attribut att) {
		Counter existingParentCounter;
		String attName = att.getName();
		String  parentObjectName = attName.substring(attName.indexOf(':') + 1);
		 existingParentCounter = getCounterByObjectName(ret,parentObjectName);
		if(existingParentCounter == null){//nouveau counter
			existingParentCounter = copyCounter(parentObjectName);	
			ret.add(existingParentCounter);
		}
		Attribut newatt = existingParentCounter.addNewAttribut();
		newatt.setDescription(att.getDescription());
		newatt.setIsComposite(att.getIsComposite());
		newatt.setJmxAttribut(att.getJmxAttribut());
		newatt.setKeysArray(att.getKeysArray());
		newatt.setName(att.getName());
		
	}

	/**
	 * create a copy of the counter based on allAttribute list
	 * 
	 * @param parentObjectName
	 * @return
	 */
	
	private Counter copyCounter(String parentObjectName) {
		Counter ret =Counter.Factory.newInstance();
		Counter from =getCounterByObjectName(allAttributes, parentObjectName);
		ret.setJmxObjectName(from.getJmxObjectName());
		ret.setJmxType(from.getJmxType());
		
		return ret;
	}

	 /****
	  * 
	  *   get the counter in the list li that has the objectname parentObjectName
	  * @param li
	  * @param parentObjectName
	  * @return
	  */
	private Counter getCounterByObjectName(List<?> li,
			String parentObjectName) {
		Counter ret = null;
		Iterator it = li.iterator();
		while (it.hasNext()) {
			Object obj = it.next();
			if( obj instanceof Counter){ // selectedatttributes might contain counter and attribute 
				Counter current = (Counter)obj;
				
				if( current.getJmxObjectName().equals(parentObjectName)){
					ret = current;
					break;
					
				}
				
			}

			
		}
		return ret;
	}

	
	
	/***
	 *  build the list of selected attribute accoriding the array given
	 * 
	 * @param liatt
	 * @return
	 */
	public Set<Object> buildAttributeSelection(Attribut[] liatt) {
		HashSet<Object> ret = new HashSet<Object>();
		List<Counter> laliste = getAllAttributes();				
		for (Attribut att : liatt) {
			Iterator<Counter> it	= laliste.iterator();
			boolean attributefounded = false;
			while ( it.hasNext()  && !(attributefounded)  ) {
				Counter type = (Counter) it.next();
				Attribut[] posibleattt = type.getAttributArray();
				for (Attribut attribut : posibleattt) {
					if(  att.getName().equals(attribut.getName())){
						ret.add(attribut);
						attributefounded=true;
						break;
						
						
					}
					
				}
				
			}
			
			
			
		}		
		return ret;
	}

	public Object[] buildCounterSelection(Attribut[] liatt) {
		HashSet<Object> ret = new HashSet<Object>();
		List<Counter> laliste = getAllAttributes();	
		Iterator<Counter>  counteriterator =laliste.iterator();
		while (counteriterator.hasNext()) {
			Counter currentcounter = (Counter) counteriterator.next();
			if(  counterContainsAttributInlist(currentcounter,liatt)){
		
		ret.add(currentcounter);
			
			}	
		}
	return ret.toArray();
	}

	private boolean counterContainsAttributInlist(Counter currentcounter,
			Attribut[] liatt) {
		boolean ret =false;
		Attribut[] possibleList = currentcounter.getAttributArray();
		for (Attribut attribut : liatt) {
			if(ret==true){
				break;
			}
			for (Attribut att : possibleList) {
				if(att .getName().equals(attribut.getName())){
					ret=true;break;
					
				}
				
				
			}
			
		}
		
		
		return ret;
	}

	public void reloadTemplates() {
		List<String> litemplatename = TemplateManager.getInstance().getTemplateNames(selectedType,selectedDomain);
		setAllTemplates(litemplatename);
		
	}
	
	
	/***
	 *  build the list of selected attribute accoriding the array given
	 * 
	 * @param liatt
	 * @return
	 */
	public Set<Object> buildAttributeFromTemplate(Template tpl) {
		//Attribut[] liatt
		String typetemplate= tpl.getType();
		List<String> names = tpl.getAttributesName();
		HashSet<Object> ret = new HashSet<Object>();
		List<Counter> laliste = getAllAttributes();				
		for (String att : names) {
			Iterator<Counter> it	= laliste.iterator();
			
			while ( it.hasNext()    ) {
				Counter type = (Counter) it.next();
				if(type.getJmxType().equals( typetemplate )){				
				Attribut[] posibleattt = type.getAttributArray();
				for (Attribut attribut : posibleattt) {
					if(  att.equals(attribut.getJmxAttribut())){
						ret.add(attribut);
						
						break;
						
						
					}
					
				}
				
			}
				
			}
			
			
			
		}		
		return ret;
	}

	public Object[] buildCounterFromTemplate(Template theTemplate) {
		HashSet<Object> ret = new HashSet<Object>();
		List<Counter> laliste = getAllAttributes();
		List<String> liatt = theTemplate.getAttributesName();
		Iterator<Counter>  counteriterator =laliste.iterator();
		while (counteriterator.hasNext()) {
			Counter currentcounter = (Counter) counteriterator.next();
			if(  counterContainsJmxAttributInlist(currentcounter,liatt)){
		
		ret.add(currentcounter);
			
			}	
		}
	return ret.toArray();
	}

	private boolean counterContainsJmxAttributInlist(Counter currentcounter,
			List<String> liatt) {
		boolean ret =false;
		Attribut[] possibleList = currentcounter.getAttributArray();
		for (String attribut : liatt) {
			if(ret==true){
				break;
			}
			for (Attribut att : possibleList) {
				if(att.getJmxAttribut().equals(attribut)){
					ret=true;break;
					
				}
				
				
			}
			
		}
		
		
		return ret;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

