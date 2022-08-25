package com.keduit.project.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.keduit.project.model.BookingVo;
import com.keduit.project.model.PriceVo;
public interface BookingRepository extends JpaRepository<BookingVo,Integer>{
	
	@Query(value = "select bookingDate from booking where btype=? group by bookingDate having count(bookingDate) > 2 ",nativeQuery = true)
	public ArrayList<String> findDisabledDays(String btype); 

	@Query(value = "select * from booking where bno=? ",nativeQuery = true)
	public Optional<BookingVo> updateByBno(Long bno);
	
	public Page<BookingVo> findByBookingDateIsBefore(LocalDate now, Pageable pageable);
	
	@Query(value = "select * from Booking where (username LIKE %:id% or name LIKE %:uname% or btype LIKE %:btype% "
			+ "or themeName LIKE %:themeName%)"
			+ "and bookingDate >= :now  ", nativeQuery = true)
	public Page<BookingVo> findBySearch(@Param("now")LocalDate now,
										@Param("id") String username,
										@Param("uname") String name,
										@Param("btype") String btype,
										@Param("themeName") String themeName,
										Pageable pageable);
	
	@Query(value = "select * from Booking where (username LIKE %:id% or name LIKE %:uname% or btype LIKE %:btype% "
			+ "or themeName LIKE %:themeName%)"
			+ "and bookingDate < :now  ", nativeQuery = true)
	public Page<BookingVo> findPastBySearch(@Param("now")LocalDate now,
			@Param("id") String username,
			@Param("uname") String name,
			@Param("btype") String btype,
			@Param("themeName") String themeName,
			Pageable pageable);
	
	public Page<BookingVo> findByBookingDateGreaterThanEqual(LocalDate now, Pageable pageable);

	public BookingVo findByBno(Long bno);
	
}
