package in.psk.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import in.psk.bindingsDto.LoginDto;
import in.psk.bindingsDto.UserDto;
import in.psk.entitys.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity,Integer> {

	
	public UserEntity findByEmail(String email);
	
	public UserEntity findByEmailAndPassword(String email,String password);
}
