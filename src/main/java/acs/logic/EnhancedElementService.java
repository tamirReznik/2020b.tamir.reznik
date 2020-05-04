package acs.logic;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import acs.rest.boundaries.element.ElementBoundary;
import acs.rest.boundaries.element.ElementIdBoundary;

public interface EnhancedElementService extends ElementService {
	//
	public void bindExistingElementToAnExsitingChildElement(ElementIdBoundary idBoundary);

	public Set<ElementBoundary> getAllChildrenOfAnExsitingElement(String userDomain, String userEmail,
			String elementDomain, String elementId);

	public Collection<ElementBoundary> getAnArrayWithElementParent(String userDomain, String userEmail,
			String elementDomain, String elementId);

}
