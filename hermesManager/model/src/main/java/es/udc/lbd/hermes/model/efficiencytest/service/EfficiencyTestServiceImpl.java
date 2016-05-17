package es.udc.lbd.hermes.model.efficiencytest.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.hermes.model.efficiencytest.EfficiencyTest;
import es.udc.lbd.hermes.model.efficiencytest.dao.EfficiencyTestDao;

@Service("efficiencyTestService")
@Transactional
public class EfficiencyTestServiceImpl implements EfficiencyTestService {

	@Autowired
	private EfficiencyTestDao efficiencyTestDao;

	public void create(String eventType, Long eventSize, Calendar time, Long parseTime, Long totalTime, Boolean result) {
		EfficiencyTest efficiencyTest = new EfficiencyTest(eventType, eventSize, time, parseTime, totalTime, result);
		efficiencyTestDao.create(efficiencyTest);
	}
}
