package in.psk.service;

import java.util.Map;

import in.psk.bindingsDto.LoginDto;
import in.psk.bindingsDto.RegisterDto;
import in.psk.bindingsDto.ResetPwdDto;
import in.psk.bindingsDto.UserDto;

public interface UserService {
	//To get all Countries
	public Map<Integer,String> getCountries();
	//To get all states based on countryId
	public Map<Integer,String> getStates(Integer cId);
	//To get all citys based on stateId
	public Map<Integer,String> getCities(Integer sId);
	//to check given emailId is duplicate or not
	public UserDto getuser(String email);
    //saveUserInfo
	public boolean registerUser(RegisterDto regDto);
	//CheckLoginCredintials
	public UserDto getUserByCredintials(LoginDto loginDto);
	//reset password
	public boolean resetPassword(ResetPwdDto resetDto);
	//To get Qoutes here we are going to do api call one application communicate with the another application
	public String getQoutes();
	
	
}
