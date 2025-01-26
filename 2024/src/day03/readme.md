## Thought process part 1
1. Pass for all the string
2. Make a function that revise the pattern, could be a regex?
3. The valid string has the next size:
   1. 8: mul(1,1)
   2. 9: mul(12,1) or mul(1,12)
   3. 10: mul(123,1) or mul(12,12) or mul(1,123)
   4. 11: mul(123,12) or mul(12,123)
   5. 12: mul(123,123)
4. Need a function that make the operation and return the result in a long variable

`^mul\(\d{1,3},\d{1,3}\)$`

## Thought process part 2

1. Need that it give me the position where it found the operation
2. Find do() and don't() with their position

## Improvements for the next ones
1. Use regex capturing groups
2. Try to use more functional coding because I relay a lot in imperative code (Use .map)