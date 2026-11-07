# Visitor Pattern — Advanced File System

## Description

This project demonstrates the Visitor behavioral design pattern using an in-memory hierarchical file system.

The system supports files, directories, nested directories, and three independent operations.

## Technologies

- Java 17
- Maven
- JUnit 5

## Visitor Operations

1. TotalSizeVisitor — calculates the total size of files.
2. SearchVisitor — searches files by name and minimum size.
3. StatisticsVisitor — counts files, directories, and maximum tree depth.

## Project Structure

- Element — common interface for files and directories.
- FileNode — represents a file with a name and byte size.
- DirectoryNode — manages files and nested directories.
- Visitor — declares overloaded visit methods.
- FileSystemWalker — recursively traverses the file system.
- Main — demonstrates the application.
- FileSystemTest — contains eight automated tests.

## Design Decisions

FileSystemWalker owns the traversal process.

Each element accepts a visitor and calls the corresponding overloaded visit method.

Visitors can be reused because beginTraversal() resets their state before each operation.

Search results use root-relative paths and are sorted lexicographically.

The file system prevents negative sizes, duplicate sibling names, multiple parents, and cycles.

## How to Run

Requirements: Java JDK 17 and Apache Maven.

Run tests from the project root:

```bash
mvn test
```

Compile the application:

```bash
mvn compile
```

Run the application:

```bash
java -cp target/classes Main
```

## Expected Output

```text
Total size: 1600 bytes
Matching files: [documents/archive/old-report.pdf, documents/report.pdf]
Files: 4
Directories: 5
Maximum depth: 3
```

## Automated Tests

FileSystemTest contains eight tests:

- T1: Double dispatch
- T2: Nested total size
- T3: Search filter conditions
- T4: Sorted root-relative paths
- T5: Statistics and maximum depth
- T6: Empty structures
- T7: Repeated visitor executions
- T8: Tree integrity

Last verified result: 8 tests passed, 0 failures, 0 errors.

## Extensibility

New operations can be added by creating another Visitor implementation without modifying existing element classes.

Adding a new element type requires updating the Visitor interface and its concrete implementations.

## GitHub Repository

https://github.com/httpskwiksx/Visitor_pattern
