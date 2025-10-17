#### Given an employee CSV file with headers, report use cases on employee dimensions. 
Internally the file content is loaded in memory as a collection of key value entries, with keys being the headers. This is the fundamental data structure
on which dimensional data is aggregated as per use case. The data provider (CSV file, in this case) and the data accessor layer is loosely coupled to keep provision for extension.

##### Class Diagram
```mermaid
  classDiagram
    ITable <|-- CsvTable
    TableDao --> ITable
    PersonDao --|> TableDao
    PersonDao .. CsvTable
    PersonDao -- FieldTyp
    PersonService --> PersonDao
    note for FieldTyp "field metadata config"
    note for ITable "tabular file data reader specs"
    note for CsvTable "specific ITable provider"
    
    class ITable{
        +setColumns()
        +load()
        +getLoadedData()
        +newInstance()$
    }
    class CsvTable{
        +setHeader()
        +setDelimiter()
       
    }
class TableDao{
  #getGroupedData()
  #getKeyedData()
}
class PersonDao{
  +getIndexByMgr()
  +getIndexByEmp()
}
class PersonService{
  +getManagerSalaryProfile()
  +getHighReportingLines()
}
class FieldTyp{
  -dataType
  -key
}
```

##### Sequence Diagram
```mermaid
sequenceDiagram
    participant Client
    participant PersonService
    participant PersonDao
    participant SalaryProfile
    participant LineProfile

    %% getManagerSalaryProfile
    Client->>PersonService: getManagerSalaryProfile(underpaidDeviation, overpaidDeviation)
    PersonService->>PersonDao: getIndexByMgr()
    PersonDao-->>PersonService: Map<String, List<Map<String, String>>>
    PersonService->>PersonDao: getIndexByEmp()
    PersonDao-->>PersonService: Map<String, Map<String, String>>
    loop for each manager (excluding CEO)
        PersonService->>PersonDao: get(empRec, FLD_SAL)
        PersonDao-->>PersonService: Optional<Object>
        PersonService->>PersonDao: avgSalary(reportees)
        PersonDao-->>PersonService: double
        PersonService->>SalaryProfile: new SalaryProfile()
        PersonService->>PersonDao: get(empRec, FLD_FNAME)
        PersonDao-->>PersonService: Optional<Object>
        PersonService->>PersonDao: get(empRec, FLD_LNAME)
        PersonDao-->>PersonService: Optional<Object>
        alt deviation >= overpaidDeviation
            PersonService->>SalaryProfile: setDeviation(sal-avg)
            PersonService->>SalaryProfile: setRange("OVERPAID")
        else deviation < underpaidDeviation
            PersonService->>SalaryProfile: setDeviation(sal-avg)
            PersonService->>SalaryProfile: setRange("UNDERPAID")
        end
    end
    PersonService-->>Client: List<SalaryProfile>

    %% getHighReportingLines
    Client->>PersonService: getHighReportingLines(len)
    PersonService->>PersonDao: getIndexByEmp()
    PersonDao-->>PersonService: Map<String, Map<String, String>>
    loop for each employee
        PersonService->>PersonDao: reportingLine(id)
        PersonDao-->>PersonService: int
        PersonService->>PersonDao: get(empRec, FLD_FNAME)
        PersonDao-->>PersonService: Optional<Object>
        PersonService->>PersonDao: get(empRec, FLD_LNAME)
        PersonDao-->>PersonService: Optional<Object>
        PersonService->>LineProfile: new LineProfile(id, name, line)
    end
    PersonService-->>Client: List<LineProfile>

```
