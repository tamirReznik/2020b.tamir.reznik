package acs.dal;

import org.springframework.data.repository.CrudRepository;
import acs.data.ElementEntity;
import acs.data.ElementIdEntity;
import acs.rest.boundaries.element.ElementBoundary;
import acs.rest.boundaries.element.ElementIdBoundary;

//Create Read Update Delete - CRUD
public interface ElementDao extends CrudRepository<ElementEntity, ElementIdEntity> {

}
