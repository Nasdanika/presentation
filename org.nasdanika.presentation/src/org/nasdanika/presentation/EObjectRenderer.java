package org.nasdanika.presentation;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.ui.view.swt.ECPSWTViewRenderer;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.widgets.Composite;

/**
 * EObject renderer extensions shall implement this interface.
 * @author Pavel
 *
 */
public interface EObjectRenderer {
	
	/**
	 * Helper class.
	 * @author Pavel Vlasov
	 *
	 */
	class Util {
		
		/**
		 * Renders EObject using either a registered {@link EObjectRenderer} or EMF Forms {@link ECPSWTViewRenderer}, if there is no EObjectRenderer registration.
		 * @param parent
		 * @param eObject
		 * @param editingDomain
		 * @throws Exception
		 */
		public static void render(Composite parent, EObject eObject, EditingDomain editingDomain) throws Exception {
			for (IConfigurationElement ce: Platform.getExtensionRegistry().getConfigurationElementsFor("org.nasdanika.presentation.eobject_renderer")) {
				// TODO renderers cache to improve performance?
				if ("eobject_renderer".equals(ce.getName()) 
						&& eObject.eClass().getName().equals(ce.getAttribute("eclass_name"))
						&& eObject.eClass().getEPackage().getNsURI().equals(ce.getAttribute("epackage_ns_uri"))) {
					((EObjectRenderer) ce.createExecutableExtension("renderer_class_name")).render(parent, eObject, editingDomain);
					return;
				}
			}		
			ECPSWTViewRenderer.INSTANCE.render(parent, eObject);
		}
		
	}
	
	/**
	 * Renders UI for editing EObject data.
	 * @param parent Parent composite.
	 * @param eObject {@link EObject} to render UI for.
	 * @param editingDomain Editing domain to possibly use in setting data bindings. 
	 * @throws Exception
	 */
	void render(Composite parent, EObject eObject, EditingDomain editingDomain) throws Exception;
	
}
