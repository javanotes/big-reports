package org.bigcompany.reports.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bigcompany.reports.file.FieldTyp;
import org.bigcompany.reports.file.ITable;
import org.bigcompany.reports.file.TableDao;
import org.bigcompany.reports.file.FieldTyp.DataTyp;
/**
 * This is a specific type of {@code TableDao}, which is opinionated towards its underlying file structure.
 * This class will bring out use case specific data structures.
 */
public class PersonDao extends TableDao{

	public PersonDao(String filePath) {
		super(ITable.newInstance(filePath));
		
	}

	// we are manually trying to do an object mapping
	// this can use introspection from a configuration
	// additionally a structural check can be kept
	
	static final FieldTyp FLD_EMP_ID = new FieldTyp("Id", DataTyp.Text);
	static final FieldTyp FLD_MGR_ID = new FieldTyp("managerId", DataTyp.Text);
	static final FieldTyp FLD_FNAME = new FieldTyp("firstName", DataTyp.Text);
	static final FieldTyp FLD_LNAME = new FieldTyp("lastName", DataTyp.Text);
	static final FieldTyp FLD_SAL = new FieldTyp("salary", DataTyp.Num);
	
	private Map<String, List<Map<String, String>>> indexByMgr;
	private Map<String, Map<String, String>> indexByEmp;
	
	// we are not concerned about concurrency for now. Else we would have a better threadsafe initialization
	private volatile boolean opened = false;
	static final String ROOT_MGR = "-";
	
	// this is a one shot (cached?) computation. for a true transactional system this strategy will change
	@Override
	protected void open() {
		if(opened)
			return;
		table.clear();
		table.setColumns(5);
		// validate that mandatory headers are present. Order does not matter
		table.load(FLD_EMP_ID, FLD_MGR_ID, FLD_FNAME, FLD_LNAME, FLD_SAL);
		 
		indexByMgr = getGroupedData(FLD_MGR_ID.key, ROOT_MGR);
		indexByEmp = getKeyedData(FLD_EMP_ID.key);
		opened = true;
	}
	public double avgSalary(List<Map<String, String>> rows) {
		open();
		return rows.stream().map(m -> get(m, FLD_SAL)).filter(Optional::isPresent).mapToDouble(o -> (double) o.get()).average().orElse(0.0);
	}
	// get with O(m) complexity. where m=line 
	public int reportingLine(String empId) {
		open();
		Optional<String> mgrId = Optional.of(empId);
		int c = 0;
		do {
			var emp = indexByEmp.get(mgrId.get());
			mgrId = get(emp, FLD_MGR_ID);
			if(mgrId.isPresent())
				c++;
			
		}while(mgrId.isPresent());
		
		return c;
	}
	public List<String> getManagerIds() {
		open();
		return new ArrayList<>(indexByMgr.keySet());
	}
	public void reload() {
		opened = false;
	}
	public Map<String, List<Map<String, String>>> getIndexByMgr() {
		return indexByMgr;
	}
	public Map<String, Map<String, String>> getIndexByEmp() {
		return indexByEmp;
	}
}
