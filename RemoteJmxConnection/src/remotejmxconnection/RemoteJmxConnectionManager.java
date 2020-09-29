package remotejmxconnection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.XMLMemento;
import org.eclipse.ui.part.ViewPart;

import cec.monitor.type.Attribut;
import cec.monitor.type.Counter;
import remotejmxconnection.view.RjmxConnectionView;

public class RemoteJmxConnectionManager {
	
	
	private static final String TAG_TYPE = "type";


	private static final String TAG_PORT = "port";


	private static final String TAG_HOST = "host";
	final String TAG_USER = "user";
	final String TAG_PASSWORD = "password";


	private static final String TAG_CONNECTION = "connection";


	private static final String TAG_CONNECTIONS = "connections";


	private static RemoteJmxConnectionManager instance = null;
	
	
	private List<RemoteJmxConnectionBean> connections;
	
	
	private ILog logger;


	private RjmxConnectionView theView;
	
	
	public static RemoteJmxConnectionManager getInstance(){
		
		if(instance ==null)
		{
			
			synchronized (RemoteJmxConnectionManager.class){
				if(instance == null) instance= new RemoteJmxConnectionManager();
				
			}
			
			
		}
		
		
		return instance;
	}
	
	
	public void setView(RjmxConnectionView v){
		this.theView=v;
		
		
	}
	
	
	public void refresh(){
		if(theView != null)
		 { theView.refresh(); }
		
	}
	
	
	private RemoteJmxConnectionManager(){
		connections = new ArrayList<RemoteJmxConnectionBean>();
		logger=Activator.getDefault().getLog();
		
				
		
	}
	
	public void log ( String message, int severity, Throwable ex){
		IStatus status = new Status(severity, Activator.PLUGIN_ID, message,ex);
		logger.log(status );
		
		
	}
	
	public void addConnection( RemoteJmxConnectionBean newconn){
		connections.add(newconn);
		
	}
	
	
	private File getStroreFile(){
		
		return Activator.getDefault().getStateLocation().append("remoteJmxConnection.xml").toFile();
		
	}
	
	public void saveConnections(){
		//if(connections.size() == 0) return;
		XMLMemento memento = XMLMemento.createWriteRoot(TAG_CONNECTIONS);
		saveConnections(memento);
		FileWriter writer =null;
		try {
			writer=new FileWriter(getStroreFile());
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





	private void saveConnections(XMLMemento memento) {
		for (RemoteJmxConnectionBean remoteJmxConnectionBean : connections) {
			IMemento child = memento.createChild(TAG_CONNECTION);
			child.putString(TAG_HOST, remoteJmxConnectionBean.getHost());
			child.putString(TAG_USER, remoteJmxConnectionBean.getUser());
			child.putString(TAG_PASSWORD, remoteJmxConnectionBean.getPassword());
			child.putInteger(TAG_PORT, remoteJmxConnectionBean.getPort());
			child.putString(TAG_TYPE,remoteJmxConnectionBean.getType().name() );
			
			
		}
		
	}
	
	
	public void loadConnections(){
		FileReader reader=null;
		connections = new ArrayList<RemoteJmxConnectionBean>();
		try {
			reader=new FileReader(getStroreFile());
			loadConnections(XMLMemento.createReadRoot(reader));			
		} catch (FileNotFoundException e) {
			//no file still created - ignore
			
		} catch (WorkbenchException e) {
			log(e.getMessage(), IStatus.ERROR, e);
		}
		
		
		
	}





	private void loadConnections(XMLMemento memento) {
		IMemento[] connect = memento.getChildren(TAG_CONNECTION);
		for (IMemento iMemento : connect) {
			RemoteJmxConnectionBean newconn = new RemoteJmxConnectionBean();
			newconn.setHost(iMemento.getString(TAG_HOST));
			newconn.setUser(iMemento.getString(TAG_USER));
			newconn.setPassword(iMemento.getString(TAG_PASSWORD));
			newconn.setPort(iMemento.getInteger(TAG_PORT));
			newconn.setType(   RemoteConnectiontype.valueOf(     iMemento.getString(TAG_TYPE)));
			connections.add(newconn);
			
		}
		
	}
	
	
	public List<RemoteJmxConnectionBean> getConnections( ){
		loadConnections();
		return connections;
		
	}
	
	
	
	public void  ActivateConnection( RemoteJmxConnectionBean theconn) throws RemoteJmxConnectionException{
	
		if(theconn.getActiveConnection()== null){		
			
			JMXServiceURL serviceurl;
			JMXConnector connector;
			MBeanServerConnection activeConnection;
			HashMap env = new HashMap();
			   String[] credentials = new String[] { theconn.getUser() , theconn.getPassword() }; 
			      env.put("jmx.remote.credentials", credentials);
			
			
			StringBuffer sb = new StringBuffer();
			sb.append("service:jmx:rmi:///jndi/rmi://");
			sb.append(theconn.getHost());
			sb.append(":");
			sb.append(theconn.getPort());
			sb.append("/jmxrmi");
			 try {
				serviceurl = new JMXServiceURL(sb.toString());
				 connector = JMXConnectorFactory.connect(serviceurl,env);
				 activeConnection = connector.getMBeanServerConnection();
					theconn.setConnector(connector);
					 
					theconn.setActiveConnection(activeConnection );
				
			} catch (Exception e) {
			throw new RemoteJmxConnectionException(e);
			}
			
							
			
		}
		
	
		
		
	}

	public RemoteJmxConnectionBean getConnection(String host, int port) {
		RemoteJmxConnectionBean ret = null;
		loadConnections();
		for (RemoteJmxConnectionBean remoteJmxConnectionBean : connections) {
			if(remoteJmxConnectionBean.getHost().equals(host) && remoteJmxConnectionBean.getPort() == port  )
			{
				ret = remoteJmxConnectionBean;
				
			}
		}
		return ret;
	}


	public List<String> getAllDomains(RemoteJmxConnectionBean connection) {
		ArrayList<String> ret=new ArrayList<String>();
		try {
			ActivateConnection(connection);
			String[] temp = connection.getActiveConnection().getDomains();
			for (String string : temp) {
				ret.add(string);
			}
			
		} catch (Exception e) {
			log(e.getMessage(),IStatus.ERROR,e);
		}
		Collections.sort(ret);
		
		return ret;
		
	}


	public List<String> getAllTypes(RemoteJmxConnectionBean connection,
			String selectedDomain) {
		ArrayList<String> ret = new ArrayList<String>();
		try {
			ActivateConnection(connection);
		
			ObjectName obj;
			obj = new ObjectName(selectedDomain + ":*");
			Set<ObjectName> set;
			set = connection.getActiveConnection().queryNames(obj, null);
			Iterator<ObjectName> it = set.iterator();
			while (it.hasNext()) {
				ObjectName object = (ObjectName) it.next();
				
				String type = object.getKeyProperty("Type");
				if(type== null)
					type=object.getKeyProperty("type");
				
				if (!ret.contains(type)) {
					ret.add(type);
				}
			}
			
		} catch (Exception e) {
			log(e.getMessage(),IStatus.ERROR,e);
			
		}
		
		Collections.sort(ret);
		return ret;
		
	}
	
	private String cleanString(String input) {
		String ret = null;
		ret = input.replaceAll("<p>", "").replaceAll("</p>", "")
				.replaceAll("<code>", "").replaceAll("</code>", "").replaceAll("\"", "'");
		return ret;

	}
	
	public static  String getPrettyName(ObjectName ob) {
		StringBuffer sb = new StringBuffer();
		Map<String, String> props = ob.getKeyPropertyList();
		for (Iterator iterator = props.keySet().iterator(); iterator.hasNext();) {
			String type = (String) iterator.next();
			sb.append(type);
			sb.append("=");
			sb.append(props.get(type));
			sb.append(",");
		}
		
		
	
		return sb.toString();
	}
	
	
	public List<Counter> getAllCounter( RemoteJmxConnectionBean conn, String domain, String type){
		
		MBeanServerConnection connection = null;
		ArrayList<Counter> ret = new ArrayList<Counter>();
		try {
			ActivateConnection(conn);
		
			connection = conn.getActiveConnection();

			
			
			StringBuffer sb = new StringBuffer(domain);
			sb.append(":Type=");
			sb.append(type);
			sb.append(",*");
			ObjectName name;			
			name = new ObjectName(sb.toString());
			Set<ObjectName> li = connection.queryNames(name, null);
			
			StringBuffer sb2 = new StringBuffer(domain);
			sb2.append(":type=");
			sb2.append(type);
			sb2.append(",*");
			ObjectName name2;			
			name2 = new ObjectName(sb2.toString());
			
			li.addAll(connection.queryNames(name2, null));
			Iterator<ObjectName> it = li.iterator();
			while (it.hasNext()) {
				ObjectName typ = (ObjectName) it.next();
Counter counter =(Counter) Counter.Factory.newInstance();				
				ret.add(counter);
				counter.setJmxType(type);
				counter.setJmxObjectName(typ.toString());
				
				
				
				MBeanInfo bi = connection.getMBeanInfo(typ);
				MBeanAttributeInfo[] attInfo = bi.getAttributes();
				for (int i = 0; i < attInfo.length; i++) {

					if (attInfo[i].getType().equals("java.lang.Integer")
							|| attInfo[i].getType().equals("java.lang.Long") 
							|| attInfo[i].getType().equals("long") 
							|| attInfo[i].getType().equals("int") 
							
							
							) {
						Attribut att = counter.addNewAttribut();
						att.setDescription(cleanString(attInfo[i]
								.getDescription()));

						att.setIsComposite(false);
						att.setJmxAttribut(attInfo[i].getName());
						att.setName(attInfo[i].getName() + ":"
								+ typ.toString());
								//+ getPrettyName(typ));
					}
				}
			}
		}catch (Exception e){
			
			log(e.getMessage(),IStatus.ERROR,e);
			
		}
	
 	
	return ret;
	}


	/***
	 * 
	 * close an active connection
	 * @param old
	 */
	
	public void release(RemoteJmxConnectionBean old) {
		try {
			old.getConnector().close();
			old.setActiveConnection(null);
			old.setConnector(null);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log(e.getMessage(), IStatus.ERROR, e);
		}
		
	}


	public void removeConnection(RemoteJmxConnectionBean el) {
		RemoteJmxConnectionBean toremove = getConnection(el.getHost(), el.getPort());
		//close extra ressources
		if(el.getConnector() != null){
			try {
				el.getConnector().close();
			} catch (IOException e) {
				log(e.getMessage(), IStatus.ERROR, e);
			}
			
		}
		//remove from list
		boolean isremoved = connections.remove(toremove);
		saveConnections();
		refresh();
		
	}

/***
 * 
 * 
 *  return whether the domain:type exists in the mbean tree
 * @param connection
 * @param domain
 * @param type
 * @return
 */
	public boolean isTypeAvailable(RemoteJmxConnectionBean connection,
			String domain, String type) {
		boolean ret = false;
		
		try {
			ActivateConnection(connection);
		
			ObjectName obj;
			obj = new ObjectName(domain + ":type=" + type+ ",*");
			Set<ObjectName> set;
			set = connection.getActiveConnection().queryNames(obj, null);
			Iterator<ObjectName> it = set.iterator();
			if(it.hasNext()) {
				
				ret = true;
			}
			
			obj = new ObjectName(domain + ":Type=" + type+ ",*");
			 
			set = connection.getActiveConnection().queryNames(obj, null);
			 it = set.iterator();
			if(it.hasNext()) {
				
				ret = true;
			}
			
			
			
			
		} catch (Exception e) {
			log(e.getMessage(),IStatus.ERROR,e);
		}
		return ret;


		
		
	}
}
