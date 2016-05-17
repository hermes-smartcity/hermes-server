package es.udc.lbd.hermes.model.efficiencytest.service;

import java.util.Calendar;

public interface EfficiencyTestService {

	public void create(String eventType, Long eventSize, Calendar time, Long parseTime, Long totalTime, Boolean result);
}
