package org.bigcompany.reports.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bigcompany.reports.dto.LineProfile;
import org.bigcompany.reports.dto.SalaryProfile;

public class PersonService {

	private PersonDao personDao;
	
	public PersonService(String personDaoFile) {
		personDao = new PersonDao(personDaoFile);
		personDao.open();
	}
	
	public List<SalaryProfile> getManagerSalaryProfile(double underpaidDeviation, double overpaidDeviation) {
		Map<String, List<Map<String, String>>> mgrs = personDao.getIndexByMgr();
		Map<String, Map<String, String>> emps = personDao.getIndexByEmp();
		
		// exclude CEO!
		List<SalaryProfile> profiles = mgrs.keySet().stream().filter(id -> !PersonDao.ROOT_MGR.equals(id)).map(id -> {
			var empRec = emps.get(id);
			// salary of manager
			double sal = (double) personDao.get(empRec, PersonDao.FLD_SAL).get();
			// avg salary of reportees
			double avg = personDao.avgSalary(mgrs.get(id));
			SalaryProfile profile = new SalaryProfile();
			profile.setManagerId(id);
			profile.setManagerName((String) personDao.get(empRec, PersonDao.FLD_FNAME).get()  +" "+ (String) personDao.get(empRec, PersonDao.FLD_LNAME).get());
			profile.setSalary(sal);
			profile.setDeviation((sal-avg)/avg);
			
			if(profile.getDeviation() >= overpaidDeviation) {
				profile.setDeviation(sal-avg);
				profile.setRange("OVERPAID");
			}
			else if(profile.getDeviation() < underpaidDeviation) {
				profile.setDeviation(sal-avg);
				profile.setRange("UNDERPAID");
			}
			return profile;
		})
		.filter(p -> p.getRange() != null)		
		.collect(Collectors.toList());
		
		//System.out.println(profiles);
		return profiles;
		
	}
	
	public List<LineProfile> getHighReportingLines(int len) {
		Map<String, Map<String, String>> emps = personDao.getIndexByEmp();
		var lineProfiles = personDao.getIndexByEmp().keySet().stream().map(id -> {
			int line = personDao.reportingLine(id);
			var empRec = emps.get(id);
			String name = (String) personDao.get(empRec, PersonDao.FLD_FNAME).get()  +" "+ (String) personDao.get(empRec, PersonDao.FLD_LNAME).get();
			return new LineProfile(id, name, line);
		}).filter(a -> a.getLine() >= len)
		.collect(Collectors.toList());
		
		//System.out.println(lineProfiles);
		return lineProfiles;
	}
	
}
