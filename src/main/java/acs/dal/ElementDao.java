package acs.dal;

import org.springframework.data.repository.CrudRepository;

import acs.data.ActionEntity;

//Create Read Update Delete - CRUD
public interface ElementDao extends CrudRepository<ActionEntity, String> {

}
