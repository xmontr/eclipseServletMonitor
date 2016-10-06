package remotejmxconnection.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import remotejmxconnection.RemoteJmxConnectionBean;
import remotejmxconnection.RemoteJmxConnectionManager;

public class RemoveConnectionHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if(selection instanceof IStructuredSelection){
			
			RemoteJmxConnectionBean el =(RemoteJmxConnectionBean)((IStructuredSelection) selection).getFirstElement();
			RemoteJmxConnectionManager.getInstance().removeConnection(el);
		}
		return null;
	}

}
