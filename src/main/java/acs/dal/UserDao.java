package acs.dal;

import org.springframework.data.repository.CrudRepository;

import acs.data.UserEntity;
import acs.data.UserIdEntity;


//Create Read Update Delete - CRUD
public interface UserDao extends CrudRepository<UserEntity, UserIdEntity>{

}
