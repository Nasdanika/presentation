package org.nasdanika.presentation;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

public class MasterDetailForm extends Composite implements ISelectionChangedListener {

	private final FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	private Tree tree;
	private Composite elementFormComposite;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 * @param site 
	 * @param new AdapterFactoryContentProvider(adapterFactory) 
	 */
	public MasterDetailForm(Composite parent, int style, EditingDomain editingDomain) {		
		super(parent, style);
		this.editingDomain = editingDomain;
		addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				toolkit.dispose();
			}
		});
		toolkit.adapt(this);
		toolkit.paintBordersFor(this);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Form form = toolkit.createForm(this);
		Composite body = form.getBody();
		toolkit.decorateFormHeading(form);
		toolkit.paintBordersFor(body);
		form.setLayout(new GridLayout(1, false));
		form.getBody().setLayout(new FillLayout(SWT.HORIZONTAL));
		
						
		SashForm sections = new SashForm(form.getBody(), SWT.NONE);
		sections.setLayout(new GridLayout(2, false));
		toolkit.paintBordersFor(sections);
		sections.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_GRAY));
		
		tree = new Tree(sections, SWT.MULTI);
		
		elementFormComposite = new Composite(sections, SWT.NONE);
		elementFormComposite.setLayout(new FillLayout());		
		
		sections.setWeights(new int[] {1, 2});
	}
	
	public Tree getTree() {
		return tree;
	}
	
	private ScrolledForm formComposite;
	private EditingDomain editingDomain;
	private EObject currentSelection;
	
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		if (!event.getSelection().isEmpty()
				&& event.getSelection() instanceof StructuredSelection
				&& ((StructuredSelection) event.getSelection()).size() == 1
				&& ((StructuredSelection) event.getSelection()).getFirstElement() instanceof EObject) {
			
			EObject eObject = (EObject) ((StructuredSelection) event.getSelection()).getFirstElement();
			if (currentSelection != eObject) {
				currentSelection = eObject;
				
				if (formComposite != null) {
					formComposite.dispose();
				}
		
				formComposite = toolkit.createScrolledForm(elementFormComposite); 
				formComposite.getBody().setLayout(new GridLayout());
				
				try {
					EObjectRenderer.Util.render(formComposite.getBody(), eObject, editingDomain);
				} catch (Exception e) {
					Label errorLabel = new Label(formComposite.getBody(), SWT.NONE);
					toolkit.adapt(errorLabel, true, true);
					errorLabel.setText("Error rendering UI: "+e);
					
				}
				elementFormComposite.layout();
			}
		} else {
			// Can't handle - just clear 
			if (formComposite != null) {
				formComposite.dispose();
			}
	
			formComposite = toolkit.createScrolledForm(elementFormComposite); 
			formComposite.getBody().setLayout(new GridLayout());			
		}
	}
	
}
