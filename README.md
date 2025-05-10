# striim

## Steps 
1. clone the repo
2. mvn clean install
3. ExpenseManagerDriver is the main class which starts execution.

## Features Implementated
1. ExpenseCalculator is the major class which takes CurrencyProvider and InputSourceBase as constructor params. It has function calculateExpense(Properties, CurrencyCode) which returns the total expenses after parsing the given file in Properties. 
2. It uses Dependency injection to inject InputSource of desired type, so that ExpenseCalculator is not tightly coupled with it.
3. The InputSourceFactory provides InputSouceBase via createSource function corresponding to file type like XML, JSON, CSV.
4. The InputSourceBase#getExpenses(Properties) returns Iterator<ExpenseEntry> to handle streaming inputs for large file.   
5. XmlSourceExpenseParser is concreate implmentation of InputSourceBase which parses file and return  parseLargeXML
6. Handled error scenarios where input XML file having invalid expense entry where amount, currency or date is/are missing. In current implementation we are skipping such expenses.  
7. To handle large XML the validation and parsing is happening simulatanously. ExpenseHandler parse the <expense> while XMLValidator validate it again an XSD file.
8. Added Logback logger for easily swapping with other logger.


## Class Diagram 
<img width="1107" alt="Screenshot 2025-05-10 at 1 52 15 PM" src="https://github.com/user-attachments/assets/7f7c43a1-bcab-479e-aa26-bed1d0fc334e" />

## Sequence diagram 
![Screenshot 2025-05-10 at 1 03 02 PM](https://github.com/user-attachments/assets/47f3d557-e17c-4b8b-8117-d78f3d3fb2b5)
