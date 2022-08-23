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
import com.keduit.project.model.RoleType;
import com.keduit.project.model.User;
import com.keduit.project.repository.BookingRepository;
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
	
//    @GetMapping("/bookingPage")
//    public String reservationPage(Model model,@AuthenticationPrincipal PrincipalDetails principalDetail) {
//    	ArrayList<String> disabledDayList = bookingService.findDisabledDays();	
//    	
//    	for(int i=0; i<disabledDayList.size(); i++) {
//    		System.out.println("비활성화 날짜 : " + disabledDayList.get(i));
//    	}
//    	
//        model.addAttribute("user", principalDetail.getUser());
//        model.addAttribute("disabledDayList",disabledDayList);
//        return "/bookingPage";
//    }
    @GetMapping("/reservation/familyreserPage")
    public String reservationPage(Model model,@AuthenticationPrincipal PrincipalDetails principalDetail) {
    	ArrayList<String> disabledDayList = bookingService.findDisabledDays("포토");	
    	
    	model.addAttribute("user", principalDetail.getUser());
    	model.addAttribute("disabledDayList",disabledDayList);
    	return "reservation/familyreserPage";
    }
    
    @GetMapping("/deleteForm")
    public String deleteForm() {
    	return "deleteForm";
    }
	
   
    @GetMapping("/admin/adminPage")
    public String adminPage(Model model,@AuthenticationPrincipal PrincipalDetails principalDetail) {
    	
    	
    	if(principalDetail.getUser().getRole() == RoleType.ROLE_ADMIN) {
    		
    		List<User> userList = userRepository.findAll();
    		model.addAttribute("userList",userList);
    		
    		return "admin/adminPage";
    	}
    	
    	return "redirect:/user";
    }
    
    @GetMapping("/admin/adminPage2")
    public String adminPage2(Model model,@AuthenticationPrincipal PrincipalDetails principalDetail) {
    	
    	
    	if(principalDetail.getUser().getRole() == RoleType.ROLE_ADMIN) {
    		
    		LocalDate now = LocalDate.now();
    		List<BookingVo> bookingList = bookingRepository.findPastBooking(now);
    		
    		model.addAttribute("bookingList",bookingList);
    		
    		return "admin/adminPage2";
    	}
    	
    	return "redirect:/user";
    }
    @GetMapping("/admin/adminPage3")
    public String adminPage3(Model model,@AuthenticationPrincipal PrincipalDetails principalDetail) {
    	
    	
    	if(principalDetail.getUser().getRole() == RoleType.ROLE_ADMIN) {
    		
    		LocalDate now = LocalDate.now();
    		List<BookingVo> bookingList = bookingRepository.findBookingList(now);
    		
    		model.addAttribute("bookingList",bookingList);
    		
    		return "admin/adminPage3";
    	}
    	
    	return "redirect:/user";
    }
    
    
    
    @PostMapping("/admin/roleModify/{username}")
    public String updateRole(@PathVariable("username") String username, User user,@AuthenticationPrincipal PrincipalDetails principalDetail) {
    	
    	if(principalDetail.getUser().getRole() == RoleType.ROLE_ADMIN) {
    		userService.userModify(user);
    		
    		return "redirect:/admin/adminPage";
    	}

    	return "redirect:/user";
    }
    
    @PostMapping("/admin/bookingDetail/{bno}")
    public String bookingDeatial(@PathVariable("bno") Long bno, BookingVo bookingVo,@AuthenticationPrincipal PrincipalDetails principalDetail, Model model) {
    	
    	if(principalDetail.getUser().getRole() == RoleType.ROLE_ADMIN) {
    		BookingVo bookingDetail = bookingRepository.findByBno(bno);
    		
    		model.addAttribute("bookingDetail", bookingDetail);
    		
    		return "admin/bookingDetailPage";
    	}
    	
    	return "redirect:/user";
    }
}
