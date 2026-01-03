## Thought process part 1
1. create a function that parse the input
2. We're able to calculate the xor between two buttons
## Thought process part 2
1. 
## Problems
1. Part two seems to difficult because pressing a button no longer interchanges between two states
## Solution
1. 

## Learnings
1. Need to start by [reviewing BFS](https://www.youtube.com/watch?v=oDqjPvD54Ss)
2. Solve [Leet Code 752 Open the Lock](https://leetcode.com/problems/open-the-lock/description/)
3. Solve [Leet Code 127 Word Ladder](https://leetcode.com/problems/word-ladder/description/)
4. Let's hope that is enough to give me insights to solve part 2
### 5. BFS state-space explosion

BFS does not work because there are **too many states to explore**.

For example, consider the target joltage state:

```
{35, 43, 27, 43, 27, 34, 40}
```

The total number of possible states is the product of `(target[i] + 1)` for each voltage:

```
35 × 43 × 27 × 43 × 27 × 34 × 40 ≈ 64,161,039,600 states
```

Even under unrealistically optimistic assumptions:
- each state takes only **1 byte** (it doesn’t),
- the algorithm processes **1 million states per second**,

it would still take **~18 hours for a single machine**.

#### Why BFS dies long before reaching 64 billion states
- The **branching factor** equals the number of buttons
- Each state generates many children
- The BFS queue grows exponentially
- Memory usage explodes and the program crashes

---

### 6. BFS danger formula: `O(b^d)`

BFS time and space complexity can be approximated as:

```
O(b^d)
```

Where:
- **b (branching factor)** = number of children per node
- **d (depth)** = number of layers BFS must explore before reaching the goal

In this problem:
- The branching factor **b** is the number of buttons  
  (each button press generates a new state)
- For illustration, assume `b = 10`
- In the best-case scenario for `{35,43,27,43,27,34,40}`,  
  we still need roughly **40 button presses**, so `d ≈ 40`

Applying the formula:

```
10^40 states
```

This is completely infeasible.

Even the **absolute lower bound** of the state space:

```
64,161,039,600 ≈ 6 × 10^10
```

is far beyond what BFS can handle.

---

### BFS viability by state-space size

| State space size | BFS viability |
|------------------|---------------|
| ≤ 100k           | Safe          |
| ≤ 1M             | Probably OK   |
| ≤ 10M            | Risky         |
| ≥ 100M           | No            |
| ≥ 1B             | Impossible    |

7. Let's try A* :,(
8. A* Didn't work, my heuristic cost was to weak as many states have the same, like for example at the beginning almost all would have the maximum value of
the goal or that value minus 1
9. A* is good when the order matter, not like this case
10. hashCode in intArrays is to dangerous to use as a key for a map, please don't use it like I did in BFS
11. We'll try to learn from a [reddit approach](https://www.reddit.com/r/adventofcode/comments/1pk87hl/2025_day_10_part_2_bifurcate_your_way_to_victory/)
12. The goal is to reduce the number of joltages by half and the number of possible 
13. I need a stronger foundation on backtracking
14. 19659 and 19697 are too low :.(
15. 19810 was the right answer!!
16. What is important in DP problems is to define laws that don't allow me to process impossible states
