package in.psk.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.psk.entitys.CityEntity;

public interface CityRepository extends JpaRepository<CityEntity, Integer> {
	
	@Query(value="select *from CITY_MASTER where state_id=:sid",nativeQuery=true)
	public List<CityEntity> getCities(Integer sid);

}
