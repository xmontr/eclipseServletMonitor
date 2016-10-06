package jmxservleteditor.preferences;

import jmxservleteditor.Activator;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class JmxServletpreferenceInitializer  extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {


		

			 IPreferenceStore store = Activator.getDefault()

			 .getPreferenceStore(); store.setDefault(JmxServletpreference.URL, "http://158.167.25.48/jmxMonitoring/templates.xml");
		
	}

}
