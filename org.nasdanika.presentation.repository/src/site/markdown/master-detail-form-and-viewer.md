# Master-detail form and viewer

Master detail form and viewer are intended to be used in generated EMF editors to add a form for editing the currently selected object on the right of the model tree. This approach is different
from the EMF Forms based editor in the following ways:

* The model tree is the original tree with all context menus preserved.
* The details form can contain custom UI, not necessarily rendered by [EMF Forms](https://www.eclipse.org/ecp/emfforms/).


## Integration

In the generated editor modify ``createPages()`` method, "selection tree view" section by replacing the TreeViewer with MasterDetailViewer in ViewerPane.

Example - [StoryEditor.java](https://github.com/Nasdanika/story/blob/master/org.nasdanika.story.editor/src/org/nasdanika/story/presentation/StoryEditor.java#L993):

```java
// Create a page for the selection tree view.
//
{
	ViewerPane viewerPane =
			new ViewerPane(getSite().getPage(), StoryEditor.this) {
				@Override
				public Viewer createViewer(Composite composite) {
					MasterDetailForm masterDetailForm = new MasterDetailForm(composite, SWT.NONE, editingDomain);
					return new MasterDetailViewer(masterDetailForm);
				}
				@Override
				public void requestActivation() {
					super.requestActivation();
					setCurrentViewerPane(this);
				}
			};
	viewerPane.createControl(getContainer());

	selectionViewer = ((MasterDetailViewer) viewerPane.getViewer()).getTreeViewer();
}
```

## Details rendering

The default behavior is to use EMF Forms. EMF Forms provide rich and highly customizable UI. 
If, for some reason, the EMF Forms rendered UI is not a good fit, 
an implementation of [``org.nasdanika.presentation.EObjectRenderer``](apidocs/index.html?org/nasdanika/presentation/EObjectRenderer.html) can be registered with ``eobject_renderer``
extension point to customize UI rendering. 

### Known issues

When editing the form the UI thinks that the focus is in the tree editor and as such Ctrl-C/Ctrl-V do not work properly in the forms.
If you need to use the clipboard, leverage the old-good properties view.

### Some issues with EMF Forms

* [TransactionalEditingDomain](http://download.eclipse.org/modeling/emf/transaction/javadoc/1.1.1/org/eclipse/emf/transaction/TransactionalEditingDomain.html) is not fully supported.
* The view model editor doesn't seem to recognize inherited structural features, at least during an attempt to create TableControl.
