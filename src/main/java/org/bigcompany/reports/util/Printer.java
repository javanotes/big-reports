package org.bigcompany.reports.util;

import java.lang.reflect.Field;
import java.util.List;
/**
 * Utility to print a plain old java object in a tabular fashion. Does not handle nested objects.
 * Uses introspection to get field dimensions and considers value lengths to derive the spacing
 */
public class Printer {
	/**
	 * 
	 * @param pojoList
	 * @param name
	 */
    public static void printTable(List<?> pojoList, String name) {
        if (pojoList == null || pojoList.isEmpty()) {
            System.err.println("(empty list)");
            return;
        }
        Class<?> clazz = pojoList.get(0).getClass();
        Field[] fields = clazz.getDeclaredFields();
        int[] colWidths = new int[fields.length];
        String[] headers = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            headers[i] = fields[i].getName();
            colWidths[i] = headers[i].length();
        }
        // Calculate max width for each column
        // unfortunately that requires an additional parsing of the list
        for (Object obj : pojoList) {
            for (int i = 0; i < fields.length; i++) {
                try {
                    Object value = fields[i].get(obj);
                    int len = value == null ? 4 : value.toString().length();
                    if (len > colWidths[i]) colWidths[i] = len;
                } catch (IllegalAccessException e) {
                    // ignore
                }
            }
        }
        // Print header
        StringBuilder sb = new StringBuilder("\n");
        sb.append("Table : ").append(name);
        System.out.println(sb);
        // Print separator
        sb.setLength(0);
        for (int i = 0; i < headers.length; i++) {
            sb.append("=".repeat(colWidths[i]));
            if (i < headers.length - 1) sb.append("===");
        }
        System.out.println(sb);
        // Print separator
        sb.setLength(0);
        
        for (int i = 0; i < headers.length; i++) {
            sb.append(String.format("%-" + colWidths[i] + "s", headers[i]));
            if (i < headers.length - 1) sb.append(" | ");
        }
        System.out.println(sb);
        // Print separator
        sb.setLength(0);
        for (int i = 0; i < headers.length; i++) {
            sb.append("-".repeat(colWidths[i]));
            if (i < headers.length - 1) sb.append("-+-");
        }
        System.out.println(sb);
        // Print rows
        for (Object obj : pojoList) {
            sb.setLength(0);
            for (int i = 0; i < fields.length; i++) {
                try {
                    Object value = fields[i].get(obj);
                    sb.append(String.format("%-" + colWidths[i] + "s", value == null ? "null" : value.toString()));
                } catch (IllegalAccessException e) {
                    sb.append(String.format("%-" + colWidths[i] + "s", "err"));
                }
                if (i < fields.length - 1) sb.append(" | ");
            }
            System.out.println(sb);
        }
    }
}
