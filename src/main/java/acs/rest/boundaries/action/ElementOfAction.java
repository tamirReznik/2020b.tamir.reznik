package acs.rest.boundaries.action;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import acs.rest.boundaries.element.ElementIdBoundary;

@Embeddable
public class ElementOfAction {
	ElementIdBoundary element;

	public ElementOfAction(ElementIdBoundary element) {
		super();
		this.element = element;
	}

	public ElementOfAction() {

	}
	@Embedded
	public ElementIdBoundary getElement() {
		return element;
	}

	public void setElement(ElementIdBoundary element) {
		this.element = element;
	}
}
