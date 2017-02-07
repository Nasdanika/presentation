# Presentation
This repository contains shared classes and extension points related to the Eclipse UI presentation such as SWT, JFace, and EMF editors.

## Master-detail form and viewer

Master detail form and viewer are intended to be used in generated EMF editors to add a form for editing the currently selected object on the right of the model tree. This approach is different
from the EMF Forms based editor in the following ways:

* The model tree is the original tree with all context menus preserved.
* The form can be a custom form, not necessarily an EMF Form, once the extension point is implemented (see below).


### Integration

In the generated editor modify ``createPages()`` method, "selection tree view" section by replacing the TreeViewer with MasterDetailViewer in ViewerPane.

Example - [StoryEditor.java](https://github.com/Nasdanika/story/blob/master/org.nasdanika.story.editor/src/org/nasdanika/story/presentation/StoryEditor.java#L976):

```java
// Create a page for the selection tree view.
//
{
	ViewerPane viewerPane =
			new ViewerPane(getSite().getPage(), StoryEditor.this) {
				@Override
				public Viewer createViewer(Composite composite) {
					MasterDetailForm masterDetailForm = new MasterDetailForm(composite, SWT.NONE);
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


## API Documentation

* http://www.nasdanika.org/products/presentation/apidocs/org.nasdanika.presentation/apidocs/)

## P2 Repository

* ``http://www.nasdanika.org/products/presentation/repository``
* [Archived](http://www.nasdanika.org/products/presentation/org.nasdanika.presentation.repository-0.1.0-SNAPSHOT.zip)
 
## Roadmap

* Extension point to specify UI provider for EClass. This extension point may be used when EMF forms customizations are not enough or when it is easier to create UI using, say [Window Builder](https://eclipse.org/windowbuilder/) and EMF Data Bindings than to perform EMF Forms customizations.

## How to contribute

As an open source project we use the [Fork and Pull Model](https://help.github.com/articles/about-collaborative-development-models/).
You can find more information about collaborative development at GitHub in this article - [Collaborating with issues and pull requests](https://help.github.com/categories/collaborating-with-issues-and-pull-requests).

When you contribute code, please make sure that the changes are clearly identifiable. In particular, avoid making non-functional changes in the code which you do not touch, 
e.g. auto-formatting of an entire compilation unit. 


