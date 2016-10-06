package jmxservleteditor.composite;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

import remotejmxconnection.RemoteJmxConnectionBean;
import remotejmxconnection.RemoteJmxConnectionManager;

public class ConnectionConfigModel {
	
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	
	public List<RemoteJmxConnectionBean> getConnections() {
		return connections;
	}

	public void setConnections(List<RemoteJmxConnectionBean> connections) {
		this.connections = connections;
	}

	public RemoteJmxConnectionBean getActiveConnection() {
		return activeConnection;
	}

	public void setActiveConnection(RemoteJmxConnectionBean activeConnection) {
		propertyChangeSupport.firePropertyChange("connectionModified", this.activeConnection,
				this.activeConnection = activeConnection);
		
	}

	private List<RemoteJmxConnectionBean> connections;
	
	private RemoteJmxConnectionBean activeConnection;
	
	
	
	public ConnectionConfigModel () {
		connections = RemoteJmxConnectionManager.getInstance().getConnections();
		
		
	}
	
	public void addPropertyChangeListener(String propertyName,
		      PropertyChangeListener listener) {
		    propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
		  }

		  public void removePropertyChangeListener(PropertyChangeListener listener) {
		    propertyChangeSupport.removePropertyChangeListener(listener);
		  }

}
