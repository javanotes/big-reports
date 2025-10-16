package org.bigcompany.reports;

import org.bigcompany.reports.service.PersonService;
import org.bigcompany.reports.util.Printer;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	PersonService service = new PersonService("employee_200.txt");
		// get managers having underpaid or overpaid salary as compared to the average reportees
		// underpaid = less than 20%; overpaid = more than 50%
		var salaryProfiles = service.getManagerSalaryProfile(0.2, 0.5);
		Printer.printTable(salaryProfiles, "Manager Salary Profile");
		// get employees having 4 or more line of reporting till CEO
		var lineProfiles = service.getHighReportingLines(4);
		Printer.printTable(lineProfiles, "Reporting Line Profile");
    }
}
