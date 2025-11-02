# Smart City Scheduling - Assignment 4

## Overview
This project implements graph algorithms for the "Smart City / Smart Campus Scheduling" scenario, combining:
1. **Strongly Connected Components (SCC)** using Tarjan's algorithm
2. **Topological Ordering** using Kahn's algorithm
3. **Shortest Paths in DAGs** with path reconstruction

## Project Structure
smart-city-scheduling/

├── src/

│ ├── main/java/

│ │ ├── Main.java # Main driver program

│ │ └── graph/

│ │ ├── common/ # Common utilities

│ │ │ ├── Graph.java # Graph data structure

│ │ │ ├── Edge.java # Edge representation

│ │ │ ├── Metrics.java # Metrics interface

│ │ │ ├── MetricsImpl.java # Metrics implementation

│ │ │ └── GraphLoader.java # JSON loader

│ │ ├── scc/ # SCC algorithms

│ │ │ ├── TarjanSCC.java # Tarjan's algorithm

│ │ │ ├── SCCResult.java # SCC result container

│ │ │ └── CondensationGraph.java

│ │ ├── topo/ # Topological sort

│ │ │ ├── TopologicalSort.java # Kahn's algorithm

│ │ │ └── DFSTopologicalSort.java

│ │ └── dagsp/ # DAG shortest paths

│ │ ├── DAGShortestPath.java

│ │ └── PathResult.java

│ └── test/java/graph/ # JUnit tests

│ ├── scc/TarjanSCCTest.java

│ ├── topo/TopologicalSortTest.java

│ └── dagsp/DAGShortestPathTest.java

├── output/ # Test datasets

│ ├── small_.json # Small (6-10 nodes)

│ ├── medium_.json # Medium (10-20 nodes)

│ └── large_*.json # Large (20-50 nodes)

├── pom.xml # Maven configuration

└── README.md # This file



## Algorithms Implemented

### 1. Tarjan's SCC Algorithm
- **Time Complexity**: O(V + E)
- **Space Complexity**: O(V)
- Finds all strongly connected components in a directed graph
- Uses DFS with discovery times and low-link values

### 2. Topological Sort (Kahn's Algorithm)
- **Time Complexity**: O(V + E)
- Uses in-degree tracking and queue-based processing
- Returns `null` if graph contains cycles

### 3. DAG Shortest/Longest Paths
- **Time Complexity**: O(V + E)
- Processes vertices in topological order
- Supports both shortest and longest path computation
- Includes path reconstruction

## Building and Running

### Prerequisites
- Java 24 or higher
- Maven 3.6+

### Build
mvn clean compile

### Run Tests
mvn test

### Run Main Program
mvn exec:java -Dexec.mainClass="Main"


## Datasets

### Small (6-10 nodes)
- `small_cyclic_1.json`: 8 nodes, contains cycles
- `small_dag_1.json`: 7 nodes, pure DAG
- `small_cyclic_2.json`: 10 nodes, multiple cycles

### Medium (10-20 nodes)
- `medium_mixed_1.json`: 15 nodes, mixed structure
- `medium_mixed_2.json`: 18 nodes, several SCCs
- `medium_dag_1.json`: 20 nodes, sparse DAG

### Large (20-50 nodes)
- `large_sparse_1.json`: 30 nodes, sparse edges
- `large_dense_1.json`: 40 nodes, denser connectivity
- `large_dag_1.json`: 50 nodes, large DAG

## Weight Model
This implementation uses **edge weights** as specified in the `weight_model` field of the JSON files.

## Instrumentation
All algorithms collect the following metrics:
- **Execution time** (nanoseconds and milliseconds)
- **Operation counters**:
    - SCC: DFS visits, edge traversals
    - Topological Sort: Queue pushes/pops
    - DAG-SP: Relaxation operations

## Testing
Comprehensive JUnit 5 tests cover:
- Simple cycles
- Pure DAGs
- Multiple SCCs
- Disconnected graphs
- Edge cases (single vertex, empty graph)
- Path reconstruction