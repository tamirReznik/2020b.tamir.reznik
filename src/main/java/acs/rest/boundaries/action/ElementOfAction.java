package acs.rest.boundaries.action;

import acs.rest.boundaries.element.ElementIdBoundary;

public class ElementOfAction {
	ElementIdBoundary elementId;

	public ElementOfAction(ElementIdBoundary element) {
		super();
		this.elementId = element;
	}

	public ElementOfAction() {

	}

	public ElementIdBoundary getElement() {
		return elementId;
	}

	public void setElement(ElementIdBoundary element) {
		this.elementId = element;
	}
}
