package in.psk.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import in.psk.entitys.CountryEntity;

public interface CountryRepository extends JpaRepository<CountryEntity, Integer> {



}
