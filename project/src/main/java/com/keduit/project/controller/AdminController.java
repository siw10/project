package com.keduit.project.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.keduit.project.config.auth.PrincipalDetails;
import com.keduit.project.dto.PageDto;
import com.keduit.project.model.BookingVo;
import com.keduit.project.model.PriceVo;
import com.keduit.project.model.RoleType;
import com.keduit.project.model.User;
import com.keduit.project.repository.BookingRepository;
import com.keduit.project.repository.PriceRepository;
import com.keduit.project.repository.UserRepository;
import com.keduit.project.service.AdminService;
import com.keduit.project.service.BookingService;
import com.keduit.project.service.UserService;

import org.springframework.data.domain.Sort;

@Controller	// View를 리턴
public class AdminController {

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
	private AdminService adminService;
	
	
	
    @GetMapping("/admin/adminPage")
    public String adminPage(Model model,@AuthenticationPrincipal PrincipalDetails principalDetail,
    		@PageableDefault(page = 0,size = 1, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable, 
    		@RequestParam(required=false, defaultValue="") String searchText) {
    	
    	
    	if(principalDetail.getUser().getRole() == RoleType.ROLE_ADMIN || principalDetail.getUser().getRole() == RoleType.ROLE_MANAGER) {
    		
			/* Page<User> userList = userRepository.findAll(pageable); */
    		Page<User> userList = userRepository.findByUsernameContainingOrNameContaining(searchText, searchText, pageable);
    		
    		PageDto<User> userPage = new PageDto<User>(userList);
    		
    		model.addAttribute("userList",userList);
    		model.addAttribute("startPage", userPage.getStartPage());
    		model.addAttribute("endPage", userPage.getEndPage());
    		
    		
    		return "admin/adminPage";
    	}
    	
    	return "redirect:/user";
    }
    
    @GetMapping("/admin/adminPage2")
    public String adminPage2(Model model,@AuthenticationPrincipal PrincipalDetails principalDetail,
    		@PageableDefault(page = 0, size = 2, sort = "bookingDate", direction = Sort.Direction.DESC) Pageable pageable,
    		@RequestParam(required=false, defaultValue="") String searchText) {
    	
    	
    	if(principalDetail.getUser().getRole() == RoleType.ROLE_ADMIN|| principalDetail.getUser().getRole() == RoleType.ROLE_MANAGER) {
    		
    		Page<BookingVo> bookingList = adminService.pastContentList(searchText,pageable);
    		
    		PageDto<BookingVo> bookingPage = new PageDto<BookingVo>(bookingList);
    		
    	
    		model.addAttribute("bookingList",bookingList);
    		model.addAttribute("startPage", bookingPage.getStartPage());
    		model.addAttribute("endPage", bookingPage.getEndPage());
    		
    		return "admin/adminPage2";
    	}
    	
    	return "redirect:/user";
    }
    
    @GetMapping("/admin/adminPage3")
    public String adminPage3(Model model,@AuthenticationPrincipal PrincipalDetails principalDetail,
    		@PageableDefault(page = 0, size = 2, sort = "bookingDate", direction = Sort.Direction.ASC) Pageable pageable,
    		@RequestParam(required=false, defaultValue="") String searchText) {
    	
    	
    	if(principalDetail.getUser().getRole() == RoleType.ROLE_ADMIN|| principalDetail.getUser().getRole() == RoleType.ROLE_MANAGER) {
    		
    		Page<BookingVo> bookingList = adminService.findBookingList(searchText,pageable);
    		
    		PageDto<BookingVo> bookingPage = new PageDto<BookingVo>(bookingList);
    		
    		System.out.print(bookingList.getTotalPages());
    		model.addAttribute("bookingList",bookingList);
    		model.addAttribute("startPage", bookingPage.getStartPage());
    		model.addAttribute("endPage", bookingPage.getEndPage());
    		
    		return "admin/adminPage3";
    	}
    	
    	return "redirect:/user";
    }
    
    @GetMapping("/admin/adminPage4")
    public String adminPage4(Model model,@AuthenticationPrincipal PrincipalDetails principalDetail) {
    	
    	
    	if(principalDetail.getUser().getRole() == RoleType.ROLE_ADMIN) {
    		
    		List<PriceVo> priceVo = priceRepository.findAll();
    		
    		model.addAttribute("priceDetail",priceVo.get(0));
    		
    		return "admin/adminPage4";
    	}
    	
    	return "admin/adminPage";
    }
    
    
    
    @PostMapping("/admin/roleModify/{username}")
    public String updateRole(@PathVariable("username") String username, User user,@AuthenticationPrincipal PrincipalDetails principalDetail) {
    	
    	if(principalDetail.getUser().getRole() == RoleType.ROLE_ADMIN) {
    		userService.userModify(user);
    		
    		return "redirect:/admin/adminPage";
    		
    	}else {
    		return "redirect:/admin/adminPage";
    	}

    }
    
    @PostMapping("/admin/bookingDetail/{bno}")
    public String bookingDeatial(@PathVariable("bno") Long bno, BookingVo bookingVo,@AuthenticationPrincipal PrincipalDetails principalDetail, Model model) {
    	
    	if(principalDetail.getUser().getRole() == RoleType.ROLE_ADMIN || principalDetail.getUser().getRole() == RoleType.ROLE_MANAGER) {
    		BookingVo bookingDetail = bookingRepository.findByBno(bno);
    		
    		ArrayList<String> disabledDayList = bookingService.findDisabledDays(bookingDetail.getBtype());	
    		model.addAttribute("disabledDayList",disabledDayList);
    		model.addAttribute("bookingDetail", bookingDetail);
    		
    		return "admin/bookingDetailPage";
    	}
    	
    	return "redirect:/user";
    }
    

}
