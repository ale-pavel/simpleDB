# SimpleDB Homeworks
Homework assignments for [Data Base II course](http://www.dia.uniroma3.it/~atzeni/didattica/BD/20192020/BDIIindex.html) at Roma Tre University. 

Usage of simpleDB v2.10 (project by Edward Sciore, a simple DBMS for didactic purposes) 
http://www.cs.bc.edu/~sciore/simpledb/

## Assignment 1
Implement buffer replacement strategies: Clock, FIFO, LRU

Suggestions: 
  - Edit the method `chooseUnpinnedBuffer()` from `BasicBufferMgr` to choose the strategy

  - `BasicBufferMgr` will need to have a variable with istance type of `ChooseUnpinnedBufferStratregy`, 
    an interface which abstracts a family of substitution algorithms. 
      - The interface exposes the method `chooseUnpinnedBuffer()`
  
  - Define an interface `PinUnpinListener` with the methods:
    - pinned(Buffer b, ...)
    - unpinned(Buffer b)
  
  - The different strategies must implement `ChooseUnpinnedBufferStratregy` and `PinUnpinListener`

## Implemented Java classes (inside /src/simpledb/buffer/):
  - BasicBufferMgr
  - ChooseUnpinnedBufferStrategy
  - PinUnpinListener
  - ClockUnpinStrategy
  - FIFOUnpinStrategy
  - LRUUnpinStrategy
