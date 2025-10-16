package org.bigcompany.reports.service;

import org.bigcompany.reports.dto.SalaryProfile;
import org.bigcompany.reports.dto.LineProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PersonServiceTest {
    private PersonService service;
    private static final String TEST_FILE = "input.txt";

    @BeforeEach
    void setUp() {
        service = new PersonService(TEST_FILE);
    }

    @Test
    void testGetOverpaidSalaryProfile() {
        double underpaidDeviation = -0.1;
        double overpaidDeviation = 0.1;
        List<SalaryProfile> profiles = service.getManagerSalaryProfile(underpaidDeviation, overpaidDeviation);
        assertNotNull(profiles);
        assertFalse(profiles.isEmpty());
        assertEquals(2, profiles.size()); // Expecting 2 overpaid managers in test data
        for (SalaryProfile profile : profiles) {
            assertNotNull(profile.getManagerId());
            assertNotNull(profile.getManagerName());
            assertTrue(profile.getRange().equals("OVERPAID") );
            
        }
    }
    
    @Test
    void testGetUnderpaidSalaryProfile() {
        double underpaidDeviation = 0.2;
        double overpaidDeviation = 0.5;
        List<SalaryProfile> profiles = service.getManagerSalaryProfile(underpaidDeviation, overpaidDeviation);
        assertNotNull(profiles);
        assertFalse(profiles.isEmpty());
        assertEquals(1, profiles.size()); // Expecting 1 underpaid manager in test data
        for (SalaryProfile profile : profiles) {
        	assertEquals("124", profile.getManagerId());
        	assertEquals(45000.00, profile.getSalary());
        	assertEquals(-5000.00, profile.getDeviation());
            assertTrue(profile.getRange().equals("UNDERPAID") );
            
        }
    }

    @Test
    void testGetHighReportingLines() {
        int threshold = 2;
        List<LineProfile> lines = service.getHighReportingLines(threshold);
        assertNotNull(lines);
        assertFalse(lines.isEmpty());
        for (LineProfile line : lines) {
            assertNotNull(line.getId());
            assertNotNull(line.getName());
            assertTrue(line.getLine() >= threshold);
        }
    }
}
