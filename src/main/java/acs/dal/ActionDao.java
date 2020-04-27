package acs.dal;

import org.springframework.data.repository.CrudRepository;

import acs.data.ActionEntity;

//Create Read Update Delete - CRUD
public interface ActionDao extends CrudRepository<ActionEntity, String>{
//	// SELECT ... FROM DUMMIES
//	public Iterable<DummyEntity> findAll();
//
//	// SELECT ... FROM DUMMIES WHERE ID = ?
//	public Optional<DummyEntity> findById (String id);
//
//	// SELECT -> UPDATE/INSERT (upsert)
//	public DummyEntity save(DummyEntity entity);
//
//	// DELETE FROM DUMMIES
//	public void deleteAll();
}
