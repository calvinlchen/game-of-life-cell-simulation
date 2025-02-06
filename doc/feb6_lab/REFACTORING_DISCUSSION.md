# Refactoring Lab Discussion
### Jessica Chen, Calvin Chen, Kyaira Boughton
### TEAM 3


## Design Principles

* Open/Close
  * open for extension but closed for modification
  * everything that exist should not need to be modified, but if others want to do stuff with it (for example changing a language welcome) they should be able to extend it
  * this simplifies the amount of if statements for chosing options

* Liskov Substution
  * any subclass should be able to do anything the superclass can, thus if a sub classes replaces a superclasses nothing would break

### Current Abstractions

#### Cell 
* Current embodiment of principles
  * all subclasses implements all public methods the cell does
  * it is open to modification with how steps are calculated, because in some subclasses step is directly the next state while other ones like wator have additional logic
  * its default getters and setters are closed

* Improved embodiment of principles
  * positon could also be abstract in case position is not necessarily an int[] or given in that format


#### Rule
* Current embodiment of principles
  * all subclasses implement all public methods the rule does
  * it is open because all the different similauations have different ways to calculate new rules

* Improved embodiment of principles
  * allows rules to be modified like one specific parameter rather than passing in a whole new set of rules


#### Grid
* Current embodiment of principles
    * all subclasses implement all public methods the grid does

* Improved embodiment of principles
  * currently the grid stores the cells in a list of lists, but it could be done so its stored in the subclasses so they can have different ways to hold a grid  

### New Abstractions

#### Position
* Description
  * how to index cells (both position wise and also unique)

* How it supports making it easier to implement new features
  * when cell indexes are not clearly a grid


#### FileReader
* Description
  * file reader for different types of files

* How it supports making it easier to implement new features
  * makes it easier to read different types of files (ex. if the parameters were passed in from json than xml)


#### CellModel
* Description
  * Abstracts the image of the cell 

* How it supports making it easier to implement new features
  * it helps implements things so the cell doesn't necessarilty need to be a colored rectangle
