package in.psk.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import in.psk.bindingsDto.LoginDto;
import in.psk.bindingsDto.RegisterDto;
import in.psk.bindingsDto.ResetPwdDto;
import in.psk.bindingsDto.UserDto;
import in.psk.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userServ;

	@GetMapping("/register")
	public String registerPage(Model model) {
		model.addAttribute("registerDto", new RegisterDto());
		Map<Integer, String> countriesMap = userServ.getCountries();
		model.addAttribute("countries", countriesMap);
		return "registerView";
	}

	// this below two methods is used to load the drop down values
	// it is used to load states based on cId
	@GetMapping("/states/{cid}")
	@ResponseBody
	public Map<Integer, String> getStates(@PathVariable("cid") Integer cId) {

		return userServ.getStates(cId);
	}

	// it is used to load cities based on sId
	@GetMapping("/cities/{sid}")
	@ResponseBody
	public Map<Integer, String> getCities(@PathVariable("sid") Integer sId) {

		return userServ.getCities(sId);
	}

	// it is used for userRegistration
	@PostMapping("/register")
	public String register(RegisterDto regDto, Model model) {
		UserDto user = userServ.getuser(regDto.getEmail());
		if (user != null) {
			model.addAttribute("emsg", "Duplicate Email");
			return "registerView";
		}
		boolean registerUser = userServ.registerUser(regDto);
		if (registerUser) {
			model.addAttribute("msg", "Successfully Registered");
		} else {
			model.addAttribute("emsg", "Registered Failed");
		}
		Map<Integer, String> countriesMap = userServ.getCountries();
		model.addAttribute("countries", countriesMap);
		return "registerView";
	}

	// to get login page
	@GetMapping("/login")
	public String loginPage(Model model) {
		model.addAttribute("loginDto", new LoginDto());
		return "index";
	}

	@PostMapping("/login")
	public String login(LoginDto loginDto, Model model) {
	    UserDto user = userServ.getUserByCredintials(loginDto);
	    if (user == null) {
	        model.addAttribute("emsg", "Invalid Credentials");
	        return "index";
	    }

	    if ("YES".equals(user.getUpdatePwd())) {
	        // Password is updated, go to dashboard
	        return "redirect:/dashboard";
	    } else {
	        // Setting email below to display email in UI page
	        ResetPwdDto resetPwdDto = new ResetPwdDto();
	        resetPwdDto.setEmail(user.getEmail());
	        
	        model.addAttribute("resetPwdDto", resetPwdDto); // Corrected attribute name
	        return "resetPwdView";
	    }
	}


	// to get resetPage
	@PostMapping("/resetPwd")
	public String resetPassword(ResetPwdDto resetDto,Model model)
	{
		//we can do this new pwd and confirm pwd check in UI side  or java side aslo but prefer in UI page
		if(!resetDto.getNewPwd().equals(resetDto.getConfirmPwd())) {
			model.addAttribute("emsg", "NewPassword and Confirm Password Must be Same");
			return "resetPwdView";
		}
		//first it will check new pwd in the above if statment it it true the only  it will go to below 
		//if statement to check oldpwd
		
		UserDto user=userServ.getuser(resetDto.getEmail());
		if(user.getPassword().equals(resetDto.getOldPwd()))
		{
			boolean resetPwd=userServ.resetPassword(resetDto);
			if(resetPwd)
			{
				return "redirect:dashboard";
			}else {
			model.addAttribute("msg", "Password Update Failed");
			return "resetPwdView";
			}	
		}else{
			model.addAttribute("smsg", "Given Old Pwd is wrong");
			return "resetPwdView";
		}
	}

	@GetMapping("/dashboard")
	public String Dashboard(Model model) {
		String quote = userServ.getQoutes();
		model.addAttribute("msg", quote);
		return "dashboardView";
	}

	@GetMapping("/logout")
	public String logout(Model model) {
		return "redirect:/login";
	}
}
