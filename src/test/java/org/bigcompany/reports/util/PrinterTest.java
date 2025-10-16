package org.bigcompany.reports.util;

import org.junit.jupiter.api.*;
import java.io.*;
import java.util.*;

class PrinterTest {
    static class Employee {
        int id;
        String name;
        Integer age;
        Employee(int id, String name, Integer age) {
            this.id = id;
            this.name = name;
            this.age = age;
        }
    }
    static class Empty {}

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private PrintStream originalOut;
    private PrintStream originalErr;

    @BeforeEach
    void setUpStreams() {
        originalOut = System.out;
        originalErr = System.err;
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
        outContent.reset();
        errContent.reset();
    }

    @Test
    void testPrintTableWithValidList() {
        List<Employee> employees = Arrays.asList(
            new Employee(1, "Alice", 30),
            new Employee(2, "Bob", 25),
            new Employee(3, "Charlie", null)
        );
        Printer.printTable(employees, "Employees");
        String output = outContent.toString();
        Assertions.assertTrue(output.contains("Table : Employees"));
        Assertions.assertTrue(output.contains("id"));
        Assertions.assertTrue(output.contains("name"));
        Assertions.assertTrue(output.contains("age"));
        Assertions.assertTrue(output.contains("Alice"));
        Assertions.assertTrue(output.contains("null"));
    }

    @Test
    void testPrintTableWithEmptyList() {
        Printer.printTable(Collections.emptyList(), "Empty");
        String err = errContent.toString();
        Assertions.assertTrue(err.contains("(empty list)"));
    }

    @Test
    void testPrintTableWithNullList() {
        Printer.printTable(null, "NullList");
        String err = errContent.toString();
        Assertions.assertTrue(err.contains("(empty list)"));
    }

    @Test
    void testPrintTableWithNullElement() {
        List<Employee> employees = Arrays.asList(null, null);
        // Should throw NullPointerException
        Assertions.assertThrows(NullPointerException.class, () -> {
            Printer.printTable(employees, "NullElements");
        });
    }

    @Test
    void testPrintTableWithPojoWithNoFields() {
        List<Empty> empties = Arrays.asList(new Empty(), new Empty());
        Printer.printTable(empties, "EmptyPojo");
        String output = outContent.toString();
        Assertions.assertTrue(output.contains("Table : EmptyPojo"));
    }

    @Test
    void testPrintTableWithSalaryProfile() {
        org.bigcompany.reports.dto.SalaryProfile p1 = new org.bigcompany.reports.dto.SalaryProfile();
        p1.setManagerId("M1");
        p1.setManagerName("John");
        p1.setSalary(50000.0);
        p1.setDeviation(1000.0);
        p1.setRange("A");

        org.bigcompany.reports.dto.SalaryProfile p2 = new org.bigcompany.reports.dto.SalaryProfile();
        p2.setManagerId("M2");
        p2.setManagerName("Jane");
        p2.setSalary(60000.0);
        p2.setDeviation(2000.0);
        p2.setRange("B");

        List<org.bigcompany.reports.dto.SalaryProfile> profiles = Arrays.asList(p1, p2);
        Printer.printTable(profiles, "SalaryProfiles");
        String output = outContent.toString();
        Assertions.assertTrue(output.contains("Table : SalaryProfiles"));
        Assertions.assertTrue(output.contains("id"));
        Assertions.assertTrue(output.contains("name"));
        Assertions.assertTrue(output.contains("salary"));
        Assertions.assertTrue(output.contains("deviation"));
        Assertions.assertTrue(output.contains("range"));
        Assertions.assertTrue(output.contains("John"));
        Assertions.assertTrue(output.contains("Jane"));
        Assertions.assertTrue(output.contains("50000.0"));
        Assertions.assertTrue(output.contains("60000.0"));
        Assertions.assertTrue(output.contains("A"));
        Assertions.assertTrue(output.contains("B"));
    }

    @Test
    void testPrintTableWithSalaryProfileNulls() {
        org.bigcompany.reports.dto.SalaryProfile p1 = new org.bigcompany.reports.dto.SalaryProfile();
        // All fields left as default/null
        List<org.bigcompany.reports.dto.SalaryProfile> profiles = Arrays.asList(p1);
        Printer.printTable(profiles, "SalaryProfilesNulls");
        String output = outContent.toString();
        Assertions.assertTrue(output.contains("Table : SalaryProfilesNulls"));
        Assertions.assertTrue(output.contains("null"));
        Assertions.assertTrue(output.contains("0.0"));
    }
}