package com.keduit.project.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.keduit.project.model.BookingVo;
import com.keduit.project.model.PriceVo;
import com.keduit.project.model.User;
public interface PriceRepository extends JpaRepository<PriceVo,Integer>{
	
	Optional<PriceVo> findByPno(int pno);
}
