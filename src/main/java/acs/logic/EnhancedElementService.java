package acs.logic;

import acs.rest.boundaries.element.ElementBoundary;
import acs.rest.boundaries.element.ElementIdBoundary;

public interface EnhancedElementService extends ElementService{
	//
	public void bindExistingElementToAnExsitingChildElement(ElementIdBoundary idBoundary);
	public ElementBoundary[] getAllChildrenOfAnExsitingElement();
	public ElementBoundary[] getAnArrayWithElementParent();
}
