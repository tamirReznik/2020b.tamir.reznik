package acs.logic;

import java.util.List;

import acs.rest.boundaries.ElementBoundary;

public class ElementServiceImplementation implements ElementService {

	@Override
	public ElementBoundary create(String managerDomain, String managerEmail, ElementBoundary element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ElementBoundary update(String managerDomain, String managerEmail, String elementDomain, String elementId,
			ElementBoundary update) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ElementBoundary> getAll(String userDomain, String userEmail) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ElementBoundary getSpecificElement(String userDomain, String userEmail, String elemantDomain,
			String elementId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAllElements(String adminDomain, String adminEmail) {
		// TODO Auto-generated method stub
		
	}

}
