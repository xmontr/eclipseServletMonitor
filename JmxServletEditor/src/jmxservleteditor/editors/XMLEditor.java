package jmxservleteditor.editors;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.editors.text.TextEditor;

public class XMLEditor extends TextEditor {

	private ColorManager colorManager;

	public XMLEditor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new XMLConfiguration(colorManager));
		setDocumentProvider(new XMLDocumentProvider());
	}
	

	
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}
	
	
	@Override
	public IEditorInput getEditorInput() {
		// TODO Auto-generated method stub
		return super.getEditorInput();
	}

}
