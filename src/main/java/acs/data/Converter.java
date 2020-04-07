package acs.data;
import org.springframework.stereotype.Component;
import acs.rest.boundaries.ElementBoundary;
import acs.rest.boundaries.UserBoundary;

@Component
public class Converter {
	public ElementBoundary fromEntity(ElementEntity entity ) {
		ElementBoundary eb = new ElementBoundary(entity.getElementId(), entity.getType(), entity.getName(),entity.getActive(),entity.getTimeStamp(),
				entity.getLocation(),entity.getElemntAttributes(),entity.getCreateBy());
		return eb;
	}
	public ElementEntity toEntity(ElementBoundary boundary ) {
		ElementEntity eE = new ElementEntity(boundary.getElementId(), boundary.getType(), boundary.getName(),boundary.getActive(),boundary.getTimeStamp(),
				boundary.getLocation(),boundary.getElemntAttributes(),boundary.getCreateBy());
		return eE;
	}
	
	
	public UserBoundary fromEntity (UserEntity entity) {
		UserBoundary ub = new UserBoundary(entity.getUserId(), entity.getRole(), entity.getUsername(), entity.getAvatar());
		return ub;
	}
	
	public UserEntity toEntity (UserBoundary boundary) {
		UserEntity ue = new UserEntity(boundary.getUserId(), boundary.getRole(), boundary.getUsername(), boundary.getAvatar());
		return ue;
	}
}
