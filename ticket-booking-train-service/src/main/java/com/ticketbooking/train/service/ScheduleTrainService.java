package com.ticketbooking.train.service;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.ticketbooking.train.entity.ScheduleTrain;

public interface ScheduleTrainService {
	
	ScheduleTrain scheduleTrain(ScheduleTrain train);
	List<ScheduleTrain> getByTrainId(int trainId);
	ScheduleTrain getByScheduleTrainId(int scheduleTrainId);
	ScheduleTrain getByScheduleTrainId(String scheduleTrainId);   // ref id
	List<ScheduleTrain> getByScheduleTimeBetweenAndStatus(String startDate, String endDate, int status);
	List<ScheduleTrain> getByScheduleTimeGreaterThanEqualAndStatus(String startDate, int status);
	List<ScheduleTrain> getByScheduleTimeGreaterThanEqual(String startDate);
	List<ScheduleTrain> getAllScheduledTrain();
	List<ScheduleTrain> getByScheduleTimeGreaterThanEqualAndStatusAndTrainId(String startDate, int status, int trainId);
	List<ScheduleTrain> getByScheduleTimeBetweenAndStatusAndTrainId(String startDate, String endDate , int status, int trainId);
	List<ScheduleTrain> getByScheduleTimeGreaterThanEqualAndTrainIdInAndStatus(String scheduleTime, List<Integer> trainIds, int status);
	List<ScheduleTrain> getByScheduleTimeBetweenAndStatusAndTrainIdIn(String startDate, String endDate , int status, List<Integer> trainIds);

}
