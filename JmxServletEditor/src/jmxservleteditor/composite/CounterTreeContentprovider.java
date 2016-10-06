package jmxservleteditor.composite;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.masterdetail.IObservableFactory;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.viewers.ObservableListTreeContentProvider;
import org.eclipse.jface.databinding.viewers.TreeStructureAdvisor;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import remotejmxconnection.RemoteJmxConnectionManager;
import cec.monitor.type.Attribut;
import cec.monitor.type.Counter;

public class CounterTreeContentprovider extends  ObservableListTreeContentProvider implements IChangeListener {
	
	private List<Counter> allcounter;
	private CounterPanelModel model;
	private IObservableFactory factory;
	private Viewer theviewer;

	
		public CounterTreeContentprovider(IObservableFactory listFactory,
			TreeStructureAdvisor structureAdvisor) {
		super(listFactory, structureAdvisor);
		this.factory=listFactory;

		
	}


	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		theviewer = viewer;
		model = (CounterPanelModel)newInput;
		if(model != null)
			{
			IObservable toto = factory.createObservable(model);
			toto.addChangeListener(this);
			
			allcounter =model.getAllAttributes();}
	}

	@Override
	public Object[] getElements(Object inputElement) {
		
		return  allcounter != null? allcounter.toArray() : new Object[] {};
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		Counter ct;
		Object[] ret = new Object[] {};
		if(parentElement instanceof Counter){
			 ct = (Counter)parentElement;
			 ret = ct.getAttributArray();
			
		}	
		Comparator<? super Attribut> comp = new Comparator<Attribut>() {
			@Override
			public int compare(Attribut o1, Attribut o2) {
				// TODO Auto-generated method stub
				return o1.getJmxAttribut().compareTo(o2.getJmxAttribut());
			}
			
		};
		Arrays.sort((Attribut[])ret,comp);
		return ret;
	}
	@Override
	public Object getParent(Object element) {
		Object ret=null;
		if(element instanceof Counter){
		ret= null;
		}
		if(element instanceof Attribut){
			Attribut theatt = (Attribut) element;
		for (Counter counter : allcounter) {
			Attribut[] atarray = counter.getAttributArray();
			for (int i = 0; i < atarray.length; i++) {
				if(theatt == atarray[i]){
					ret= counter;break;
				}
			}
			
		}	
			
		}
		return ret;
	}

	@Override
	public boolean hasChildren(Object element) {
		Counter ct;
		boolean ret = false;
		if(element instanceof Counter){
			 ct = (Counter)element;
			ret= ct.getAttributArray().length >= 0 ?  true :  false;
			
		}		
		return ret;
	}


	@Override
	public void handleChange(ChangeEvent event) {
	
		allcounter =model.getAllAttributes();
		theviewer.refresh();
		
	}

}
