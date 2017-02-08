# Presentation
This repository contains shared classes and extension points related to the Eclipse UI presentation such as SWT, JFace, and EMF editors.

## Master-detail form and viewer

Master detail form and viewer are intended to be used in generated EMF editors to add a form for editing the currently selected object on the right of the model tree. This approach is different
from the EMF Forms based editor in the following ways:

* The model tree is the original tree with all context menus preserved.
* The details form can contain custom UI, not necessarily rendered by EMF Forms.


### Integration

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

The default behavior is to use [EMF Forms](https://www.eclipse.org/ecp/emfforms/). EMF Forms provide rich and highly customizable UI. 
If, for some reason, the EMF Forms rendered UI is not a good fit, 
an implementation of ``org.nasdanika.presentation.EObjectRenderer`` can be registered with ``eobject_renderer``
extension point to customize UI rendering.    

## API Documentation

* http://www.nasdanika.org/products/presentation/apidocs/org.nasdanika.presentation/apidocs/

## P2 Repository

* ``http://www.nasdanika.org/products/presentation/repository``
* [Archived](http://www.nasdanika.org/products/presentation/org.nasdanika.presentation.repository-0.1.0-SNAPSHOT.zip)
 
## How to contribute

As an open source project we use the [Fork and Pull Model](https://help.github.com/articles/about-collaborative-development-models/).
You can find more information about collaborative development at GitHub in this article - [Collaborating with issues and pull requests](https://help.github.com/categories/collaborating-with-issues-and-pull-requests).

When you contribute code, please make sure that the changes are clearly identifiable. In particular, avoid making non-functional changes in the code which you do not touch, 
e.g. auto-formatting of an entire compilation unit. 

## Resources

* [Window Builder](https://eclipse.org/windowbuilder/)
* [JFace Data Binding Tutorial](http://www.vogella.com/tutorials/EclipseDataBinding/article.html)
* [JFace Data Binding and EMF Tutorial](http://www.vogella.com/tutorials/EclipseDataBindingEMF/article.html)
* [EMFEditProperties](http://download.eclipse.org/modeling/emf/emf/javadoc/2.7.0/org/eclipse/emf/databinding/edit/EMFEditProperties.html)

