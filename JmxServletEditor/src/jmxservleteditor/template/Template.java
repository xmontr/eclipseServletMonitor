package jmxservleteditor.template;

import java.util.List;

public class Template {
	
	
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getAttributesName() {
		return attributesName;
	}

	public void setAttributesName(List<String> attributesName) {
		this.attributesName = attributesName;
	}
	
	
	private String domain;

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}


	private String name;
	
	private String type;
	
	private List<String> attributesName;
	
	
	private boolean isSystem ;

	public boolean isSystem() {
		return isSystem;
	}

	public void setSystem(boolean isSystem) {
		this.isSystem = isSystem;
	}

}
