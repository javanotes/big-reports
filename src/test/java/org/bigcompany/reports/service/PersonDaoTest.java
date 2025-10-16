package org.bigcompany.reports.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PersonDaoTest {
    private PersonDao dao;
    private static final String TEST_FILE = "input.txt";

    @BeforeEach
    void setUp() {
        dao = new PersonDao(TEST_FILE);
        dao.reload();
    }

    @Test
    void testOpenAndIndexes() {
        dao.open();
        assertNotNull(dao.getIndexByMgr());
        assertNotNull(dao.getIndexByEmp());
        assertFalse(dao.getIndexByMgr().isEmpty());
        assertFalse(dao.getIndexByEmp().isEmpty());
    }

    @Test
    void testAvgSalary() {
        dao.open();
        List<Map<String, String>> rows = dao.getIndexByMgr().values().stream().findFirst().orElse(List.of());
        double avg = dao.avgSalary(rows);
        assertTrue(avg >= 0);
    }

    @Test
    void testReportingLine() {
        dao.open();
        String empId = dao.getIndexByEmp().keySet().stream().findFirst().orElse(null);
        assertNotNull(empId);
        int line = dao.reportingLine(empId);
        assertTrue(line >= 0);
    }

    @Test
    void testGetManagerIds() {
        List<String> mgrIds = dao.getManagerIds();
        assertNotNull(mgrIds);
        assertFalse(mgrIds.isEmpty());
    }

    @Test
    void testReload() {
        dao.open();
        dao.reload();
        // After reload, open should repopulate indexes
        dao.open();
        assertNotNull(dao.getIndexByMgr());
        assertNotNull(dao.getIndexByEmp());
    }
}
