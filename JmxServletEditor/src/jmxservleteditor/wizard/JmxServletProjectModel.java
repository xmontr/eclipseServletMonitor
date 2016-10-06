package jmxservleteditor.wizard;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class JmxServletProjectModel {
	
	private String projectName ;
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		propertyChangeSupport.firePropertyChange("projectName", this.projectName,
				this.projectName = projectName);
		
	}
	
	
	public void addPropertyChangeListener(String propertyName,
		      PropertyChangeListener listener) {
		    propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
		  }

		  public void removePropertyChangeListener(PropertyChangeListener listener) {
		    propertyChangeSupport.removePropertyChangeListener(listener);
		  }
	

}
