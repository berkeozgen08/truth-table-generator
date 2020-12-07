Usage:  
```java
TruthTable truthTable = new TruthTable("e + d' * (a' + (b*c))");  
truthTable.printSolution();

TruthTable truthTable2 = new TruthTable("a + (b * c)'", 'a', 'b', 'c');
truthTable2.printSolution();
```