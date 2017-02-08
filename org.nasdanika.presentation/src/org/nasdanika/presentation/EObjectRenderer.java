package org.nasdanika.presentation;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.widgets.Composite;

/**
 * EObject renderer extensions shall implement this interface.
 * @author Pavel
 *
 */
public interface EObjectRenderer {
	
	/**
	 * Renders UI for editing EObject data.
	 * @param parent Parent composite.
	 * @param eObject {@link EObject} to render UI for.
	 * @param editingDomain Editing domain to possibly use in setting data bindings. 
	 * @throws Exception
	 */
	void render(Composite parent, EObject eObject, EditingDomain editingDomain) throws Exception;

}
