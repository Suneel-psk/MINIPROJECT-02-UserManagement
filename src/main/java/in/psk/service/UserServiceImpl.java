package in.psk.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import in.psk.bindingsDto.LoginDto;
import in.psk.bindingsDto.QuoteDto;
import in.psk.bindingsDto.RegisterDto;
import in.psk.bindingsDto.ResetPwdDto;
import in.psk.bindingsDto.UserDto;
import in.psk.entitys.CityEntity;
import in.psk.entitys.CountryEntity;
import in.psk.entitys.StateEntity;
import in.psk.entitys.UserEntity;
import in.psk.repo.CityRepository;
import in.psk.repo.CountryRepository;
import in.psk.repo.StateRepository;
import in.psk.repo.UserRepository;
import in.psk.utils.EmailUtils;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private CountryRepository countryRepo;
	@Autowired
	private StateRepository stateRepo;
	@Autowired
	private CityRepository cityRepo;
	@Autowired
	private EmailUtils emailUtils;

	private QuoteDto[] quotations = null;
	@Override
	public Map<Integer, String> getCountries() {
		Map<Integer, String> countryMap = new HashMap<>();
		List<CountryEntity> allCountrys = countryRepo.findAll();
		System.out.println(allCountrys);
		allCountrys.forEach(c -> {
			countryMap.put(c.getCountryId(), c.getCountryName());
		});
		
		return countryMap;
	}

	@Override
	public Map<Integer, String> getStates(Integer cId) {
		// why iam write the countryEntity means in stateEntity countryId directly dont
		// have it have
		// like realtionship when we have realtionship means we have to write
		// countryEntity and set countryid
		Map<Integer, String> stateMap = new HashMap<>();
		/*
		 * CountryEntity country=new CountryEntity(); country.setCountryId(cId);
		 * StateEntity state=new StateEntity(); state.setCountryEnt(country);
		 * Example<StateEntity> of=Example.of(state); List<StateEntity>
		 * stateList=stateRepo.findAll(of);
		 * stateList.forEach(c->{stateMap.put(c.getStateId(),c.getStateName());
		 * 
		 * });
		 */

		List<StateEntity> stateList = stateRepo.getStates(cId);
		stateList.forEach(c -> {
			stateMap.put(c.getStateId(), c.getStateName());
		});
		return stateMap;

	}

	@Override
	public Map<Integer, String> getCities(Integer sId) {
		Map<Integer, String> cityMap = new HashMap<>();
		List<CityEntity> cityList = cityRepo.getCities(sId);
		cityList.forEach(c -> {
			cityMap.put(c.getCityId(), c.getCityName());
		});
		return cityMap;
	}

	@Override
	public UserDto getuser(String email) {
		UserEntity userEntity = userRepo.findByEmail(email);
		UserDto dto = new UserDto();
		// To copy One Object Data To Another Object we have Two Wyas
		// 1.Using BeanUtils-->It is available in spring
		/* BeanUtils.copyProperties(userEntity, dto); */
		// 2.UsingModelMapper -it is a third pary dependnecy
		if(userEntity==null) {
			return null;
		}
		ModelMapper mapper = new ModelMapper();
		UserDto map = mapper.map(userEntity, UserDto.class);

		return map;
	}

	@Override
	public boolean registerUser(RegisterDto regDto) {
		ModelMapper mapper = new ModelMapper();
		UserEntity userEntity = mapper.map(regDto, UserEntity.class);
		CountryEntity country = countryRepo.findById(regDto.getCountryId()).orElseThrow();
		StateEntity state = stateRepo.findById(regDto.getStateId()).orElseThrow();
		CityEntity city = cityRepo.findById(regDto.getCityId()).orElseThrow();
		userEntity.setCountry(country);
		userEntity.setState(state);
		userEntity.setCity(city);
		userEntity.setPassword(generateRandomPwd());
		userEntity.setUpdatePwd("NO");
		UserEntity savedEntity = userRepo.save(userEntity);
		// sending password to registeremail
		String subject = "User Registeration";
		String body = "Your temporary password id :" + userEntity.getPassword();
		emailUtils.sendEmail(regDto.getEmail(), subject, body);
		return savedEntity.getUserId() != null;
	}

	@Override
	public UserDto getUser(LoginDto loginDto) {

		UserEntity user = userRepo.findByEmailAndPassword(loginDto.getEmail(), loginDto.getPassword());
		if (user == null) {
			return null;
		} else {
			ModelMapper mapper = new ModelMapper();
			UserDto userDto = mapper.map(user, UserDto.class);
			return userDto;
		}
	}

	@Override
	public boolean resetPassword(ResetPwdDto resetDto) {
		UserEntity userEntity = userRepo.findByEmailAndPassword(resetDto.getEmail(), resetDto.getOldPwd());
		if (userEntity != null) {
			userEntity.setPassword(resetDto.getNewPwd());
			userEntity.setUpdatePwd("YES");
			userRepo.save(userEntity);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String getQoutes() {
		if(quotations==null) {
		String url = "https://type.fit/api/quotes";
		RestTemplate rt = new RestTemplate();
		ResponseEntity<String> forEntity = rt.getForEntity(url, String.class);
		String responseBody = forEntity.getBody();
		ObjectMapper mapper = new ObjectMapper();

		try {
			quotations = mapper.readValue(responseBody, QuoteDto[].class);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}

		Random rand = new Random();
		int index = rand.nextInt(quotations.length - 1);

		return quotations[index].getText();
	}

	private static String generateRandomPwd() {
		String aToZ = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		Random rand = new Random();
		StringBuilder res = new StringBuilder();
		for (int i = 0; i < 5; i++) {
			int randIndex = rand.nextInt(aToZ.length());
			res.append(aToZ.charAt(randIndex));
		}
		String randomPwd = res.toString();
		return randomPwd;
	}

}
