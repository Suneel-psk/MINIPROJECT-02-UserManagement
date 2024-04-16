package in.psk.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.psk.entitys.StateEntity;

public interface StateRepository extends JpaRepository<StateEntity,Integer> {
	
	//another way
	@Query(value="select *from state_master where country_id=:cid",nativeQuery=true)
	public List<StateEntity> getStates(Integer cid);

}
