package acs.logic;

import java.util.List;

import acs.rest.boundaries.ElementBoundary;
import acs.rest.boundaries.UserBoundary;

public interface ElementService {

	public ElementBoundary createUser();
	public ElementBoundary update();
	public List<ElementBoundary> getAllUsers();
	public ElementBoundary getSpecificElements();
	public void deleteAllActions();
	
}
