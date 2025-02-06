# Collections API Lab Discussion
### Jessica Chen, Calvin Chen, Kyaira Boughton
### TEAM 3



## In your experience using these collections, are they hard or easy to use?
- Easy to use.


## In your experience using these collections, do you feel mistakes are easy to avoid?
- Yes, for the most part and they give errors when you do something wrong (over indexing).

## What methods are common to all collections (except Maps)?
- size and iterator


## What methods are common to all Deques?
- addFirst, addLast, removeFirst, removeLast, getFirst and getLast


## What is the purpose of each interface implemented by LinkedList?
- List and deque
- If you frequently add elements to the beginning of the List or iterate over the List to delete elements from its interior, you should consider using LinkedList.
- The Deque interface supports insertion, removal and retrieval of elements at both ends. The LinkedList implementation is more flexible than the ArrayDeque implementation. LinkedList implements all optional list operations. null elements are allowed in the LinkedList implementation but not in the ArrayDeque implementation.


## How many different implementations are there for a Set?
- 3

## What is the purpose of each superclass of PriorityQueue?
- To make sure you can easily use things logically without needing to use different method names. Like priority queue and queue have similar method names for pop, so it makes it easier to use

## What is the purpose of the Collections utility class?
- it provides a general framework that makes it easier to implement new collection type objects other than the general implementations like Set and List they give you.

## API Characterics applied to Collections API

* Easy to learn
  * they are many subclasses but few superclasses so method names are standarized   

* Encourages extension
  * it allows for different implemenations of things such as sets and lists and linked list 

* Leads to readable code
  * uses similar methods with names of methods that make sense 

* Hard to misuse
  * throws errors when there are misuse