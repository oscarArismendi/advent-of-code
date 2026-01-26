## Thought process part 1
1. Parse input
2. Store each shape as a list of (x,y) cells where # exists
3. Generate all rotations (0°, 90°, 180°, 270°)
4. Enumerate placements
   For one region:
   - For each shape s
   - For each orientation of s
   - For each position (x,y) where it fits in the region
   - Create one placement
5. Build Exact Cover rows
    - Every grid cell it occupies → cell constraint
    - One instance of shape s → shape-count constraint
6. Build Exact Cover columns
    - One column per grid cell (x,y)
    - ne column per required shape instance (e.g. shape 4 appears 3 times → 3 columns)
7. Implement Algorithm X (simple recursive version first)
   - choose column with fewest rows
   - try each row
   - remove conflicting rows
   - recurse
   - backtrack
   
## Thought process part 2


Create one placement
## Problems
1. Don't know how to represent the figures need to find how
2. Don't know an efficient way to place the figures

## Learnings
1. It appears that there's an algorithm called DLX(Dancing Links) that could be useful
2. [Standford lecture: Dancing links by Don Knuth](https://youtu.be/_cR9zDlvP88?si=iu_iwmG2MKsLO6FD)
3. [See how DLX is implemented in sudoku by Don Knuth](https://www.ocf.berkeley.edu/~jchu/publicportal/sudoku/sudoku.paper.html)
4. Algorithms are powerful, but remember to trim obvious answers