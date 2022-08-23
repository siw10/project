package com.keduit.project.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.keduit.project.config.auth.PrincipalDetails;
import com.keduit.project.model.BookingVo;
import com.keduit.project.model.PriceVo;
import com.keduit.project.model.RoleType;
import com.keduit.project.model.User;
import com.keduit.project.repository.BookingRepository;
import com.keduit.project.repository.PriceRepository;
import com.keduit.project.repository.UserRepository;
import com.keduit.project.service.BookingService;
import com.keduit.project.service.UserService;

@Controller	// View를 리턴
public class UserController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BookingRepository bookingRepository;
	
	@Autowired
	private PriceRepository priceRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BookingService bookingService;
	
	@Autowired
    private AuthenticationManager authenticationManager;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	//localhost:8080/
	//localhost:8080/
	@GetMapping({"","/"})
	public String index(Model model, @AuthenticationPrincipal PrincipalDetails principalDetail) {
		model.addAttribute("user", principalDetail.getUser());
		
		return "index";	//src/main/resources/templates/index.mustache
	}
	
	@GetMapping("/user")
	public String user(Model model, @AuthenticationPrincipal PrincipalDetails principalDetail) {
		
		model.addAttribute("user", principalDetail.getUser());
		return "user";
	}
	
	@GetMapping("/admin")
	public @ResponseBody String admin() {
		return "admin";
	}
	
	@GetMapping("/manager")
	public @ResponseBody String manager() {
		return "manager";
	}
	
	// 스프링시큐리티 해당주소를 낚아챔
	@GetMapping("/loginForm")
	public String loginForm() {
		return "loginForm";
	}
	
	@GetMapping("/joinForm")
	public String joinForm() {
		return "joinForm";
	}
	
	
	
    @GetMapping("/updateForm")
    public String updateForm(Model model, @AuthenticationPrincipal PrincipalDetails principalDetail) {
        model.addAttribute("user", principalDetail.getUser());
        return "updateForm";
    }
	

    @GetMapping("/reservation/photo/{url}")
    public String photoReservationPage(@PathVariable String url,Model model,@AuthenticationPrincipal PrincipalDetails principalDetail) {
    	ArrayList<String> disabledDayList = bookingService.findDisabledDays("포토");	
    	
    	List<PriceVo> priceVo = priceRepository.findAll();
    	model.addAttribute("user", principalDetail.getUser());
    	model.addAttribute("disabledDayList",disabledDayList);
    	model.addAttribute("price",priceVo.get(0));
		
    	return "reservation/photo/"+url;
    }
    @GetMapping("/reservation/spaSwim/{url}")
    public String spaPoolReservationPage(@PathVariable String url,Model model,@AuthenticationPrincipal PrincipalDetails principalDetail) {
    	String btype;
    	if(url.equals("spareserPage")) {
    		btype="스파";
    	}else {
    		btype="수영장";
    	}
    	ArrayList<String> disabledDayList = bookingService.findDisabledDays(btype);	
    	
    	List<PriceVo> priceVo = priceRepository.findAll();
    	model.addAttribute("user", principalDetail.getUser());
    	model.addAttribute("disabledDayList",disabledDayList);
    	model.addAttribute("price",priceVo.get(0));
    	return "reservation/spaSwim/"+url;
    }

    @GetMapping("/deleteForm")
    public String deleteForm() {
    	return "deleteForm";
    }
	
   

}
