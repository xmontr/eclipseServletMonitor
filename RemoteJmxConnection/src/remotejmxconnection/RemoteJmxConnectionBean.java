package remotejmxconnection;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXServiceURL;

public class RemoteJmxConnectionBean {
	
	
	private String host;
	
	private int port;
	
	private MBeanServerConnection activeConnection;
	
	private JMXConnector connector;
	
	private String user;
	
	private String password ;
	
	

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public JMXConnector getConnector() {
		return connector;
	}

	public void setConnector(JMXConnector connector) {
		this.connector = connector;
	}

	public MBeanServerConnection getActiveConnection() {
		return activeConnection;
	}

	public void setActiveConnection(MBeanServerConnection activeConnection) {
		this.activeConnection = activeConnection;
	}

	private RemoteConnectiontype type;

	public RemoteConnectiontype getType() {
		return type;
	}

	public void setType(RemoteConnectiontype type) {
		this.type = type;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	

}
