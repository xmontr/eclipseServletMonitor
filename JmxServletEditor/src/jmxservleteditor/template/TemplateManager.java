package jmxservleteditor.template;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jmxservleteditor.Activator;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.XMLMemento;





public class TemplateManager {
	
	private static final String TAG_SYSTEM = "system";

	private static final String TAG_TEMPLATES = "templates";

	private static final String TAG_TEMPLATE = "template";

	private static final String TAG_NAME = "name";

	private static final String TAG_TYPE = "type";
	
	
	private static final String TAG_DOMAIN = "domain";

	private static final String TAG_ATT = "attribut";

	private static final String TAG_ATTNAME = "attName";

	private static TemplateManager instance = null;
	
	private ILog logger;

	/***
	 * 
	 *  template defined by user
	 */
	private ArrayList<Template> templates;
	
	
	
	
	public static TemplateManager getInstance(){
		
		if(instance ==null)
		{
			
			synchronized (TemplateManager.class){
				if(instance == null) instance= new TemplateManager();
				
			}
			
			
		}
		
		
		return instance;
	}
	
	
	public TemplateManager(){
		templates = new ArrayList<Template>();
		
		logger=Activator.getDefault().getLog();
		
	}
	
	
	private File getStoreFile(){
		
		File ret=Activator.getDefault().getStateLocation().append("templates.xml").toFile();
		
		if(!ret.exists())
			try {
				ret.createNewFile();				
				XMLMemento memento = XMLMemento.createWriteRoot(TAG_TEMPLATES);
				FileWriter writer=new FileWriter(ret);
				memento.save(writer);
				
				
			} catch (IOException e) {
				log("unable to create persistent file", IStatus.ERROR, e);
				e.printStackTrace();
			}
		
		return ret;
		
	}	
	
	public void addTemplate( Template newtemp){
		templates.add(newtemp);
		
	}
	
	public void saveTemplates(){
		if(templates.size() == 0) return;
		XMLMemento memento = XMLMemento.createWriteRoot(TAG_TEMPLATES);
		saveTemplates(memento);
		FileWriter writer =null;
		try {
			writer=new FileWriter(getStoreFile());
			memento.save(writer);
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log(e.getMessage(), IStatus.ERROR, e);
		}
			finally {
				
				if(writer !=null){
					
					try {
						writer.close();
					} catch (IOException e) {
						log(e.getMessage(), IStatus.ERROR, e);
					}
					
				}
			}	
		
		
	}
	
	
	private void saveTemplates(XMLMemento memento) {
		for (Template template : templates) {
			
			if( !template.isSystem()){
			IMemento child = memento.createChild(TAG_TEMPLATE);
			child.putString(TAG_NAME, template.getName());
			child.putString(TAG_TYPE,template.getType() );
			child.putString(TAG_DOMAIN, template.getDomain());
			child.putBoolean(TAG_SYSTEM, false);
			List<String> listatt = template.getAttributesName();
			for (String att : listatt) {
				IMemento subchild=child.createChild(TAG_ATT);
				subchild.putString(TAG_ATTNAME,att );
				
			}
			
			
		}
		}
	}
	
	
	public void loadFromPreferences() throws IOException, WorkbenchException {
		String surl = jmxservleteditor.Activator.getDefault().getPreferenceStore().getString("url");	
		Reader reader = null;
		if(surl != null){
		URL url = new URL(surl);
		
		jmxservleteditor.Activator.getDefault().log("loading system template from " + surl, IStatus.INFO, null);
		if( url.getProtocol().equalsIgnoreCase("file")){
			
			InputStream in = new FileInputStream(new File(url.getFile())) ;
			
			reader = new InputStreamReader(  in);
			
			
			
			
		}else{
		
		 reader = new InputStreamReader(  url.openStream());		
		}
		
		 loadTemplates(XMLMemento.createReadRoot(reader));
		}
		
	}
	
	
	
	public void loadTemplates(){
		FileReader reader=null;
		templates = new ArrayList<Template>();
		try {
			reader=new FileReader(getStoreFile());
			
			
			
			
			loadTemplates(XMLMemento.createReadRoot(reader));		
			loadFromPreferences();
		} catch (FileNotFoundException e) {
			log(e.getMessage(), IStatus.ERROR, e);
			
		} catch (Exception e) {
			log(e.getMessage(), IStatus.ERROR, e);
		}
		
		
		
	}
	
	
	private void loadTemplates(XMLMemento memento) {
		IMemento[] list = memento.getChildren(TAG_TEMPLATE);
		for (IMemento iMemento : list) {
			Template template = new Template();
			template.setSystem(iMemento.getBoolean(TAG_SYSTEM));
			template.setName(iMemento.getString(TAG_NAME));
			template.setType(iMemento.getString(TAG_TYPE));
			template.setDomain(iMemento.getString(TAG_DOMAIN));
			IMemento[] listatt = iMemento.getChildren(TAG_ATT);
			List<String> listAtt = new ArrayList<String>();
			for (IMemento iMemento2 : listatt) {
				listAtt.add(iMemento2.getString(TAG_ATTNAME));
			}
			template.setAttributesName(listAtt);			
			templates.add(template);
			
		}
		
	}	
	
	

	
	
	
	public void log ( String message, int severity, Throwable ex){
		IStatus status = new Status(severity, Activator.PLUGIN_ID, message,ex);
		logger.log(status );
		
		
	}
	
	public List<Template> getTemplates( ){
		loadTemplates();
		return templates;
		
	}
	
	
	public List<String> getTemplateNames(){
		loadTemplates();
		List<String> ret = new ArrayList<String>();
		for (Template template : templates) {
			ret.add(template.getName());
					
		}
	return(ret);	
		
	}
	
	
	public List<Template> getTemplates(String type ){
		loadTemplates();
		List<Template> ret = new ArrayList<Template>();
		for (Template template : templates) {
			
			if(template.getType().equals(type)){
				ret.add(template);
			}
			
			
		}
		
		
		return ret;
		
	}


	public List<String> getTemplateNames(String selectedType, String selectedDomain) {
		loadTemplates();
		List<String> ret = new ArrayList<String>();
		for (Template template : templates) {
			
			if(template.getType().equals(selectedType) && template.getDomain().equals(selectedDomain)){
				ret.add(template.getName());
			}
			
			
		}
		
		
		return ret;
	}
	
	
	public Template getTemplateByName( String name){
		loadTemplates();
		Template ret = null;
		for (Template template : templates) {
			if(template.getName().equals(name)){
				ret=template;
				break;
			}
		}
		
		
		
		return ret;
		
		
	}


	public Map<String, List<Template>> getTemplatesGroupedByType() {
		loadTemplates();
		Map<String, List<Template>> ret = new HashMap<String, List<Template>>();
		
		for (Template template : templates) {
			
			String newtype = template.getType();
			String domain = template.getDomain();
			StringBuffer sb = new StringBuffer();
			sb.append(domain).append(":").append(newtype);
			List<Template> laliste = ret.get(sb.toString());
			if(laliste == null){
				laliste= new ArrayList<Template>();
				ret.put(sb.toString(), laliste);
				
			}
			laliste.add(template);
			
		}		
		return ret;
	}


	public void removeTemplate(Template el) {
		boolean ret = templates.remove(el);
		
		if(ret == true){
			saveTemplates();
			log(" succesfully removing template " + el.getName(), IStatus.INFO, null);
		}
		
	}
	
	
	

	
	
	
	
	

}
