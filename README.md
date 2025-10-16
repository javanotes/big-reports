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
