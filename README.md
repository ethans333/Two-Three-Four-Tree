# Two Three Four 🌲

Two Three Four tree implementation in Java. 

| Operation  | Runtime |
| ------------- | ------------- |
| Add  | $O(\log{n})$  |
| Find  | $O(\log{n})$  |
| Delete  | $O(\log{n})$  |

### Test Harness Output
```sql
CASE:      100 integers,       20 finds,       10 removals.  Generating...
  TwoFourTree add:       1ms  find:       0ms  del:       0ms  (        9 missing) find:      1ms  (Should be         9 missing)  
  TreeSet     add:       0ms  find:       0ms  del:       0ms  (        9 missing) find:      1ms  (Should be         9 missing)  
CASE:    1,000 integers,      200 finds,      100 removals.  Generating...
  TwoFourTree add:       1ms  find:       0ms  del:       1ms  (       91 missing) find:      0ms  (Should be        91 missing)  
  TreeSet     add:       1ms  find:       0ms  del:       0ms  (       91 missing) find:      0ms  (Should be        91 missing)  
CASE:   10,000 integers,    2,000 finds,    1,000 removals.  Generating...
  TwoFourTree add:       3ms  find:       0ms  del:       2ms  (      910 missing) find:      1ms  (Should be       910 missing)  
  TreeSet     add:       5ms  find:       1ms  del:       2ms  (      910 missing) find:      0ms  (Should be       910 missing)  
CASE:  100,000 integers,   20,000 finds,   10,000 removals.  Generating...
  TwoFourTree add:      39ms  find:       5ms  del:       9ms  (    9,063 missing) find:      7ms  (Should be     9,063 missing)  
  TreeSet     add:      12ms  find:       5ms  del:       5ms  (    9,063 missing) find:      5ms  (Should be     9,063 missing)  
CASE: 1,000,000 integers,  200,000 finds,  100,000 removals.  Generating...
  TwoFourTree add:     273ms  find:      58ms  del:      73ms  (   90,610 missing) find:     49ms  (Should be    90,610 missing)  
  TreeSet     add:     247ms  find:      42ms  del:      26ms  (   90,610 missing) find:     40ms  (Should be    90,610 missing)  
CASE: 10,000,000 integers, 2,000,000 finds, 1,000,000 removals.  Generating...
  TwoFourTree add:   2,662ms  find:     398ms  del:     634ms  (  905,936 missing) find:    441ms  (Should be   905,936 missing)  
  TreeSet     add:   2,713ms  find:     466ms  del:     263ms  (  905,936 missing) find:    492ms  (Should be   905,936 missing)  
```

