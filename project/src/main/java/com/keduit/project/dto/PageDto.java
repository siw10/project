package com.keduit.project.dto;

import org.springframework.data.domain.Page;

import lombok.Data;

@Data
public class PageDto<T> {
	private int startPage;
	private int endPage;
	public PageDto(Page<T> list) {
		int tempMinPage = 1;
		if(list.getTotalPages() != 0) {
			tempMinPage = list.getTotalPages();
		}
		this.endPage = Math.min(tempMinPage, (int)(Math.ceil((list.getPageable().getPageNumber()+1)/10.0) *10)) ;
		this.startPage = Math.max(1, endPage - 9);
		
	}
	
}
