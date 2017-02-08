package org.nasdanika.presentation;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.ui.view.swt.ECPSWTViewRenderer;
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
	
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		if (!event.getSelection().isEmpty()) {
			if (formComposite != null) {
				formComposite.dispose();
			}
	
			formComposite = toolkit.createScrolledForm(elementFormComposite); 
			formComposite.getBody().setLayout(new GridLayout());
			
			if (event.getSelection() instanceof StructuredSelection
					&& ((StructuredSelection) event.getSelection()).size() == 1
					&& ((StructuredSelection) event.getSelection()).getFirstElement() instanceof EObject) {
				
				try {
					EObject eObject = (EObject) ((StructuredSelection) event.getSelection()).getFirstElement();
					
					for (IConfigurationElement ce: Platform.getExtensionRegistry().getConfigurationElementsFor("org.nasdanika.presentation.eobject_renderer")) {
						// TODO renderers cache to improve performance?
						if ("eobject_renderer".equals(ce.getName()) 
								&& eObject.eClass().getName().equals(ce.getAttribute("eclass_name"))
								&& eObject.eClass().getEPackage().getNsURI().equals(ce.getAttribute("epackage_ns_uri"))) {
							((EObjectRenderer) ce.createExecutableExtension("renderer_class_name")).render(formComposite.getBody(), eObject, editingDomain);
							return;
						}
					}		
					
					ECPSWTViewRenderer.INSTANCE.render(formComposite.getBody(), eObject);
				} catch (Exception e) {
					Label errorLabel = new Label(formComposite.getBody(), SWT.NONE);
					toolkit.adapt(errorLabel, true, true);
					errorLabel.setText("Error rendering UI: "+e);
					
				}
			}
			elementFormComposite.layout();
		}
	}
	
}
