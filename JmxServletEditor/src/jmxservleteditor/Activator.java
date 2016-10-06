package jmxservleteditor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.jar.JarFile;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;



/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "JmxServletEditor"; //$NON-NLS-1$



	public static final String CONNECTION_IMG = "Network-Wifi-icon.gif";
	public static final String DESIGN_IMG = "Pencil-icon.png";
	public static final String SERVLET_PROJECT_IMG = "chart_curve.png";

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptorByPath(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	
	public static ImageDescriptor getImageDescriptorById(String id) {
		String path = "icons/"+id;
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	
	@Override
	protected void initializeImageRegistry(ImageRegistry registry) {
		Bundle bundle = Platform.getBundle(PLUGIN_ID);
		addImage(CONNECTION_IMG,registry,bundle);
		addImage(DESIGN_IMG,registry,bundle);
      
	}
	
	/***
	 * 
	 *  return the filename of the war template of servlet
	 * @return
	 * @throws IOException 
	 * @throws URISyntaxException 
	 */
	public InputStream getWarTemplate() throws URISyntaxException, IOException{
		InputStream ret;
		Bundle bundle = Platform.getBundle(PLUGIN_ID);
		IPath path = new Path("WarTemplate/J2eeMonitor.war");
		URL fileURL = FileLocator.find(bundle, path, null);
		URL resolvedUrl = FileLocator.resolve(fileURL);		
		
		
		URLConnection connection =resolvedUrl.openConnection();
		ret=connection.getInputStream();
		
	
		
		return (ret);
		
	}

	private void addImage(String connectionImg, ImageRegistry registry, Bundle bundle) {
		
		  IPath path = new Path("icons/"+connectionImg);
	        URL url = FileLocator.find(bundle, path, null);
	        ImageDescriptor desc = ImageDescriptor.createFromURL(url);
	        registry.put(connectionImg, desc);
		
	}
	
	public void log ( String message, int severity, Throwable ex){
		IStatus status = new Status(severity, Activator.PLUGIN_ID, message,ex);
		getLog().log(status );
		
		
	}
	
}
