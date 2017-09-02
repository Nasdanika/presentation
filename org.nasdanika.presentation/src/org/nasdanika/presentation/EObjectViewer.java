package org.nasdanika.presentation;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

/**
 * Viewer which uses EObjectRenderer in a scrolled form to display an EObject
 * which is part of selection/input.
 * @author Pavel Vlasov
 *
 */
public abstract class EObjectViewer extends Viewer {
	
	private Composite elementFormComposite;	
	private final FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	private EditingDomain editingDomain;
	protected EObject data;
	private ScrolledForm formComposite;

	protected EObjectViewer(Composite parent, int style, EditingDomain editingDomain) {
		elementFormComposite = new Composite(parent, style);
		elementFormComposite.setLayout(new FillLayout());
		elementFormComposite.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				toolkit.dispose();
			}
		});
		toolkit.adapt(elementFormComposite);
		toolkit.paintBordersFor(elementFormComposite);
		this.editingDomain = editingDomain;		
	}
	
	protected void setData(EObject newData) {
		if (newData != data) {
			data = newData;
			
			if (formComposite != null) {
				formComposite.dispose();
			}
	
			formComposite = toolkit.createScrolledForm(elementFormComposite); 
			
			Label titleLabel = new Label(formComposite.getForm().getHead(), SWT.FILL);
			titleLabel.setText("Kaka");
			
			formComposite.getBody().setLayout(new GridLayout());
			
			if (data != null) {				
				try {
					EObjectRenderer.Util.render(formComposite.getBody(), data, editingDomain);
				} catch (Exception e) {
					Label errorLabel = new Label(formComposite.getBody(), SWT.NONE);
					toolkit.adapt(errorLabel, true, true);
					errorLabel.setText("Error rendering UI: "+e);
					e.printStackTrace();
				}
				elementFormComposite.layout();
			}
		}		
	}

	@Override
	public Control getControl() {
		return elementFormComposite;
	}
	
	protected abstract EObject getDataFromInput(Object input);
	
	protected abstract EObject getDataFromSelection(ISelection selection);

	protected Object input;
	protected ISelection selection;
	
	@Override
	public Object getInput() {
		return input;
	}

	@Override
	public ISelection getSelection() {
		return selection;
	}

	/**
	 * NOP - responsibility of the rendered UI to bind itself to data
	 */
	@Override
	public void refresh() {
		
	}

	@Override
	public void setInput(Object input) {
		setData(getDataFromInput(input));
		this.input = input;
	}

	@Override
	public void setSelection(ISelection selection, boolean reveal) {
		setData(getDataFromSelection(selection));
		this.selection = selection;
	}

}
