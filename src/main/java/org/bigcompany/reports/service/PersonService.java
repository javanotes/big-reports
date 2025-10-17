package org.bigcompany.reports.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bigcompany.reports.dto.LineProfile;
import org.bigcompany.reports.dto.SalaryProfile;

/**
 * Service class for reporting use cases.
 */
public class PersonService {

	private PersonDao personDao;

	/**
	 * constructor
	 * @param personDaoFile employee data file
	 */
	public PersonService(String personDaoFile) {
		personDao = new PersonDao(personDaoFile);
		personDao.open();
	}

	/**
	 * Given a range of lower and upper bound of deviations based on average reportee salary, get underpaid and overpaid
	 * managers from the employee data.
	 * @param underpaidDeviation lower bound
	 * @param overpaidDeviation upper bound
	 * @return list of employee
	 */
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
			profile.setSalary(halfUp(sal));
			profile.setReporteeAvg(halfUp(avg));
			profile.setDeviation((sal-avg)/avg);
			if(profile.getDeviation() >= overpaidDeviation) {
				profile.setRange("OVERPAID");
			}
			else if(profile.getDeviation() < underpaidDeviation) {
				profile.setRange("UNDERPAID");
			}
			profile.setDeviation(halfUp( sal-avg));
			return profile;
		})
		.filter(p -> p.getRange() != null)		
		.collect(Collectors.toList());
		
		//System.out.println(profiles);
		return profiles;
		
	}

	private static double halfUp(double d){
		return BigDecimal.valueOf(d).setScale(2, RoundingMode.HALF_UP).doubleValue();
	}
	/**
	 * Given a number, get all employees having greater or equal reporting line.
	 * @param len reporting line length
	 * @return list of employee
	 */
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
