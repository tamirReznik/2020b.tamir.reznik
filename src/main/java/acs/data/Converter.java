package acs.data;
import org.springframework.stereotype.Component;
import acs.rest.boundaries.ElementBoundary;

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
}
