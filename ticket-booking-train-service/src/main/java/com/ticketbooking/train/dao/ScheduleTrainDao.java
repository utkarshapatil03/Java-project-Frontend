package com.ticketbooking.train.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ticketbooking.train.entity.ScheduleTrain;

@Repository
public interface ScheduleTrainDao extends JpaRepository<ScheduleTrain, Integer> {
	
	List<ScheduleTrain> findByTrainId(int trainId);
	ScheduleTrain findByScheduleTrainId(String scheduleTrainId);
	List<ScheduleTrain> findByScheduleTimeBetweenAndStatus(String startDate, String endDate, int status);
	List<ScheduleTrain> findByScheduleTimeGreaterThanEqualAndStatus(String startDate, int status);
	List<ScheduleTrain> findByScheduleTimeGreaterThanEqual(String startDate);
	List<ScheduleTrain> findByScheduleTimeGreaterThanEqualAndStatusAndTrainId(String startDate, int status, int trainId);
	List<ScheduleTrain> findByScheduleTimeBetweenAndStatusAndTrainId(String startDate, String endDate , int status, int trainId);
	@Query( "select st from ScheduleTrain st where scheduleTime >= :scheduleTime and trainId In (:trainIds) and status= :status")
	List<ScheduleTrain> findByScheduleTimeGreaterThanEqualAndTrainIdInAndStatus(@Param("scheduleTime") String scheduleTime, @Param("trainIds") List<Integer> trainIds, @Param("status") int status);
	List<ScheduleTrain> findByScheduleTimeBetweenAndStatusAndTrainIdIn(String startDate, String endDate, int status, List<Integer> trainIds);
}
