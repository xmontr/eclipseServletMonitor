package jmxservleteditor.nature;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jmxservleteditor.Activator;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;

import cec.monitor.type.MonitorConfigDocument;

public class JmxServletNature implements IProjectNature {
	
	public static final String NATUREID = "jmxremote.natureid";

	private static final String BUILDER_ID = "jmxservletEditor.builder.id";
	private IProject project;

	@Override
	public void configure() throws CoreException {
		StringBuilder sb =new StringBuilder();
		sb.append("Configuring JmxServletNature nature for project ").append(getProject());
		Activator.getDefault().log(sb.toString(), IStatus.INFO, null);
IProjectDescription desc = project.getDescription();
		
		
		
		desc.setComment("a project that contains the config file of jmx monitoring servlet");
		
		IFile configfile = project.getFile("configFile.xml");
	
		MonitorConfigDocument doc = MonitorConfigDocument.Factory.newInstance();		
		doc.addNewMonitorConfig();
		doc.xmlText();
		
		InputStream stream= new ByteArrayInputStream(doc.xmlText().getBytes());
		IProgressMonitor monitor = null;
		configfile.create(stream, false, monitor );
		
		//add builder
		// Look for builder already associated.
		final ICommand[] cmds = desc.getBuildSpec();
		for (int j = 0; j < cmds.length; j++) {
		if (cmds[j].getBuilderName().equals(BUILDER_ID)) {
		return;
		}
		}
		// Associate builder with project.
		final ICommand newCmd = desc.newCommand();
		newCmd.setBuilderName(BUILDER_ID);
		final List<ICommand> newCmds = new ArrayList<ICommand>();
		newCmds.addAll(Arrays.asList(cmds));
		newCmds.add(newCmd);
		desc.setBuildSpec((ICommand[]) newCmds
		.toArray(new ICommand[newCmds.size()]));
		project.setDescription(desc, null);
		
		
		
		
	}

	@Override
	public void deconfigure() throws CoreException {
		IProjectDescription description =project.getDescription();
		//remove configfile
		IFile configfile = project.getFile("configFile.xml");
		configfile.delete(true, null);
		
		
		// Look for builder.
		int index = -1;
		final ICommand[] cmds = description.getBuildSpec();
		for (int j = 0; j < cmds.length; j++) {
		if (cmds[j].getBuilderName().equals(BUILDER_ID)) {
		index = j;
		break;
		}
		}
		if (index == -1) {
		return;
		}
		// Remove builder from project.
		final List<ICommand> newCmds = new ArrayList<ICommand>();
		newCmds.addAll(Arrays.asList(cmds));
		newCmds.remove(index);
		description.setBuildSpec((ICommand[]) newCmds
		.toArray(new ICommand[newCmds.size()]));
		project.setDescription(description, null);
	}

	@Override
	public IProject getProject() {
		// TODO Auto-generated method stub
		return project;
	}

	@Override
	public void setProject(IProject project) {
		this.project = project;
		
		
	}

}
