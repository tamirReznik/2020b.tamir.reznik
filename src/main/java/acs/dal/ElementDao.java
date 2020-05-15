package acs.dal;



import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import acs.data.ElementEntity;
import acs.data.ElementIdEntity;



//Create Read Update Delete - CRUD
public interface ElementDao extends PagingAndSortingRepository<ElementEntity, ElementIdEntity> {
 		//CrudRepository<ElementEntity, ElementIdEntity> 
	
	
	// SELECT ... FROM ELEMENT WHERE ORIGIN_ID=?
//	public List<ElementEntity> findAllByParent_id(@Param("elementId") ElementIdEntity elementId, Pageable pageable);
////
//	// SELECT ... FROM DUMMIES WHERE TYPE=?
//	public List<DummyEntity> findAllByType(
//			@Param("type") TypeEntityEnum type, 
//			Pageable pageable);	


}
