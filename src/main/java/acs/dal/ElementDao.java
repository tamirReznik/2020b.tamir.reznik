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
		
	// SELECT ... FROM ELEMENTS WHERE ACTIVE=?
		public List<ElementEntity> findAllByActive(
				@Param("active") boolean active, 
				Pageable pageable);	

}
