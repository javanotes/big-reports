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
		var objects = service.getManagerSalaryProfile(0.2, 0.5);
		Printer.printTable(objects, "Manager Salary Profile");
		var objects2 = service.getHighReportingLines(4);
		Printer.printTable(objects2, "Reporting Line Profile");
    }
}
