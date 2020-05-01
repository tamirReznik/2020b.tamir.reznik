package acs.rest.boundaries.action;

import javax.persistence.Embedded;

import acs.rest.boundaries.element.ElementIdBoundary;

public class ElementOfAction {
	ElementIdBoundary element;

	public ElementOfAction(ElementIdBoundary element) {
		super();
		this.element = element;
	}

	public ElementOfAction() {

	}

	public ElementIdBoundary getElement() {
		return element;
	}

	public void setElement(ElementIdBoundary element) {
		this.element = element;
	}
}
