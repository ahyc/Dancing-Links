# Knuth's Dancing Links Algorithm
Sudoku solver using the [Dancing Links algorithm](https://www.ocf.berkeley.edu/~jchu/publicportal/sudoku/0011047.pdf)  
## How to run code:
1. Compile the Sudoku Solver package with
```java
javac SudokuSolver/*.java
```
2. Compile the `sudoku.java` file with
```java
javac sudoku.java
```
3. Run the sudoku solver with
```java
java sudoku
```

## Notes
- To run the code on a new grid, create a new `.txt` file and place in the `SudokuGrids` folder. The `.txt` file should replace *all blank cells* with **'0'**
- To run the code on a specific grid, add the file name when running the code: e.g.
`java sudoku sudoku1.txt


