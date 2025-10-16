package org.bigcompany.reports.file;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CsvTableTest {
    
    String csvFile;

    @BeforeEach
    void setUp() throws IOException {
        csvFile = "missing.txt";
    }

    @Test
    void testLoadValidCsv() {
        CsvTable table = new CsvTable(5, csvFile.toString());
        table.setHeader(0);
        assertTrue(table.load());
        List<Map<String, String>> data = table.getLoadedData();
        assertEquals(2, data.size()); // Only valid rows loaded
        assertEquals("Alice", data.get(0).get("name"));
        assertEquals("Bob", data.get(1).get("name"));
    }

    @Test
    void testHeaderValidation() {
        CsvTable table = new CsvTable(5, csvFile.toString());
        table.setHeader(0);
        // Should not throw
        table.load();
        assertArrayEquals(new String[]{"id","name","age","city","country"}, table.getHeaders());
    }

    @Test
    void testColumnMismatch() throws IOException {
        CsvTable table = new CsvTable(2, csvFile.toString());
        table.setHeader(0);
        Exception ex = assertThrows(DataException.class, table::load);
        assertTrue(ex.getMessage().contains("Unexpected header count"));
    }

    @Test
    void testClear() {
        CsvTable table = new CsvTable(5, csvFile.toString());
        table.setHeader(0);
        table.load();
        assertFalse(table.getLoadedData().isEmpty());
        table.clear();
        assertNull(table.getLoadedData());
    }

    @Test
    void testGettersAndSetters() {
        CsvTable table = new CsvTable(5, csvFile.toString());
        table.setHeader(0);
        table.setDelimiter(",");
        table.setColumns(5);
        assertEquals(0, table.getHeader());
        assertEquals(",", table.getDelimiter());
        assertEquals(5, table.getColumns());
        assertEquals(csvFile.toString(), table.getFilePath());
    }
}
