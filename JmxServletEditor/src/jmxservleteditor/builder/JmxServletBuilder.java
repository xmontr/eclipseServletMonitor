package jmxservleteditor.builder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import jmxservleteditor.Activator;

import org.apache.commons.io.IOUtils;
import org.apache.xmlbeans.XmlOptions;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import cec.monitor.type.MonitorConfigDocument;

public class JmxServletBuilder extends IncrementalProjectBuilder{
	
	private static final String DIRTEMP= "/configGenerator/";
	

	@Override
	protected IProject[] build(int kind, Map<String, String> args,
			IProgressMonitor monitor) throws CoreException {
		if (kind == IncrementalProjectBuilder.FULL_BUILD) {
            fullBuild(monitor);
         } else {
            IResourceDelta delta = getDelta(getProject());
            if (delta == null) {
               fullBuild(monitor);
            } else {
               incrementalBuild(delta, monitor);
            }
         }
   
		return null;
	}
	
	  private void fullBuild(IProgressMonitor monitor) {
		  
		Job j=  new Job(" Building JMX servlet") {
			
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				IStatus ret = Status.OK_STATUS;
		        try {
					createWarFile(getProject(),monitor);
				} catch (Exception e) {
					
					Activator.getDefault().log(e.getMessage(), IStatus.ERROR, e);
					ret= Status.CANCEL_STATUS;
				
				}
		        finally{ monitor.done();
		        Activator.getDefault().log("Built of jmxservlet ended", IStatus.INFO, null);
		        }
				return ret;
			}
		};
		  
		
		j.setUser(true);
		j.schedule();
		  
 
	        
	      }
	  
	  
	  private void incrementalBuild(IResourceDelta delta, 
		       IProgressMonitor monitor) {
		         System.out.println("incremental build on "+delta);
		         try {
		            delta.accept(new IResourceDeltaVisitor() {
		               public boolean visit(IResourceDelta delta) {
		                  System.out.println("changed: "+
		                   delta.getResource().getRawLocation());
		                  return true; // visit children too
		               }
		            });
		         } catch (CoreException e) {
		            e.printStackTrace();
		         }

}
	  
	 
	  
	  public File generateTempWarFile ( byte[]  doc, IProgressMonitor monitor) throws IOException, URISyntaxException {
		  
			InputStream is =Activator.getDefault().getWarTemplate(); 
			String zipEntryName="WEB-INF/configFile.xml";
			final File sysTempDir = new File(System.getProperty("java.io.tmpdir") + DIRTEMP);
			
			if(! sysTempDir.exists()){
			if( sysTempDir.mkdir() == true ){
				
				//theLogger.info("creation directory" + sysTempDir.getAbsolutePath());
			}
			else{
				throw new IOException("enable to create temporary directory for servlet generation :" + sysTempDir.getAbsolutePath( ));
				
			}
			}
			File tempFile = File.createTempFile("configFile", ".war",sysTempDir);
	        byte[] buf = new byte[1024];
	         
	        ZipInputStream zin = new ZipInputStream(is);
	        ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(tempFile));
	        ZipEntry entry = zin.getNextEntry();
	        while (entry != null) {
	            String name = entry.getName();
	            zout.putNextEntry(new ZipEntry(name));
	            // Transfer bytes from the ZIP file to the output file
	            int len;
	            while ((len = zin.read(buf)) > 0) {
	                zout.write(buf, 0, len);
	            }
			
	            entry = zin.getNextEntry();
	        }        
	        // add the configfile
			zout.putNextEntry(new ZipEntry(zipEntryName));			
	        zout.write(doc);
	        zout.closeEntry();
	        zout.close();
	        zin.close();
			
			
			return tempFile;  
	  }
	  
	  
	  public  void createWarFile( IProject project, IProgressMonitor monitor) throws IOException, CoreException, URISyntaxException{
		  IFile targetWarFile = project.getFile("j2eeMonitor.war");
		 IFile configfile = project.getFile("configFile.xml");
		 byte[] cf = IOUtils.toByteArray(configfile.getContents());

			//create the new war 
			File tempwarfile = generateTempWarFile(cf,monitor);
			//delete previous existing version
			if(targetWarFile.exists()){
				targetWarFile.delete(true,monitor);
			}
			targetWarFile.create(new FileInputStream(tempwarfile), true, monitor);
			targetWarFile.setDerived(true, null);
			
			
				
		}
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
}
