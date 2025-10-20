package org.bigcompany.reports.file;

import java.util.Map;

public abstract class AbstractTable implements ITable {
    /**
     * Row level validation framework, if enabled. This can be a delegated module.
     * @param row
     * @param fieldTyps
     * @return true, if valid
     */
    protected boolean validateRowData(Map<String, String> row, FieldTyp[] fieldTyps) {
        for (FieldTyp typ : fieldTyps){
            if(typ.isEnableTypeCheck() && row.containsKey(typ.key)){
                // delegate to validation framework
                // depending on strategy, it may simply log/throw
            }
        }
        return true;
    }
    @Override
    public void addRow(Map<String, String> row, FieldTyp[] fieldTyps){
        // validate metadata
        if(!row.isEmpty() && validateRowData(row, fieldTyps)){
            getLoadedData().add(row);
        }
    }
}
