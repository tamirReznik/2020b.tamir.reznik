package acs.rest.boundaries.action;

import acs.rest.boundaries.element.ElementIdBoundary;

public class ElementOfAction {
	public ElementOfAction(ElementIdBoundary element) {
		super();
		this.element = element;
	}

	ElementIdBoundary element;

	public ElementOfAction() {
		super();
	}

	public ElementIdBoundary getElement() {
		return element;
	}

	public void setElement(ElementIdBoundary element) {
		this.element = element;
	}
}
