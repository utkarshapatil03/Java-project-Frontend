package com.ticketbooking.train.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ticketbooking.train.dao.ScheduleTrainDao;
import com.ticketbooking.train.entity.ScheduleTrain;

@Service
public class ScheduleTrainServiceImpl implements ScheduleTrainService {

	@Autowired
	private ScheduleTrainDao scheduleTrainDao;

	@Override
	public ScheduleTrain scheduleTrain(ScheduleTrain train) {
		return scheduleTrainDao.save(train);
	}

	@Override
	public List<ScheduleTrain> getByTrainId(int trainId) {
		return scheduleTrainDao.findByTrainId(trainId);
	}

	@Override
	public ScheduleTrain getByScheduleTrainId(String scheduleTrainId) {
		// TODO Auto-generated method stub
		return scheduleTrainDao.findByScheduleTrainId(scheduleTrainId);
	}

	@Override
	public List<ScheduleTrain> getByScheduleTimeBetweenAndStatus(String startDate, String endDate, int status) {
		// TODO Auto-generated method stub
		return scheduleTrainDao.findByScheduleTimeBetweenAndStatus(startDate, endDate, status);
	}

	@Override
	public List<ScheduleTrain> getByScheduleTimeGreaterThanEqualAndStatus(String startDate, int status) {
		// TODO Auto-generated method stub
		return scheduleTrainDao.findByScheduleTimeGreaterThanEqualAndStatus(startDate, status);
	}

	@Override
	public List<ScheduleTrain> getByScheduleTimeGreaterThanEqual(String startDate) {
		// TODO Auto-generated method stub
		return scheduleTrainDao.findByScheduleTimeGreaterThanEqual(startDate);
	}

	@Override
	public List<ScheduleTrain> getAllScheduledTrain() {
		// TODO Auto-generated method stub
		return scheduleTrainDao.findAll();
	}

	@Override
	public List<ScheduleTrain> getByScheduleTimeGreaterThanEqualAndStatusAndTrainId(String startDate, int status,
			int trainId) {
		// TODO Auto-generated method stub
		return scheduleTrainDao.findByScheduleTimeGreaterThanEqualAndStatusAndTrainId(startDate, status, trainId);
	}

	@Override
	public List<ScheduleTrain> getByScheduleTimeBetweenAndStatusAndTrainId(String startDate, String endDate, int status,
			int trainId) {
		// TODO Auto-generated method stub
		return scheduleTrainDao.findByScheduleTimeBetweenAndStatusAndTrainId(startDate, endDate, status, trainId);
	}

	@Override
	public ScheduleTrain getByScheduleTrainId(int scheduleTrainId) {
		// TODO Auto-generated method stub
		return scheduleTrainDao.findById(scheduleTrainId).get();
	}

	@Override
	public List<ScheduleTrain> getByScheduleTimeGreaterThanEqualAndTrainIdInAndStatus(String scheduleTime,
			List<Integer> trainIds, int status) {
		return scheduleTrainDao.findByScheduleTimeGreaterThanEqualAndTrainIdInAndStatus(scheduleTime, trainIds, status);
	}

	@Override
	public List<ScheduleTrain> getByScheduleTimeBetweenAndStatusAndTrainIdIn(String startDate, String endDate,
			int status, List<Integer> trainIds) {
		// TODO Auto-generated method stub
		return scheduleTrainDao.findByScheduleTimeBetweenAndStatusAndTrainIdIn(startDate, endDate, status, trainIds);
	}

}
