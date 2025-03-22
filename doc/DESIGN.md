# Cell Society Design Final
### 3
- Kyaira Boughton (keb125)
- Jessica Chen (jc939)
- Calvin Chen (clc162)


## Team Roles and Responsibilities

 * Kyaira Boughton (keb125)
   * All configuration related tasks such as the features under configuration and creating xml documents for testing.

 * Jessica Chen (jc939)
   * Simulation related features as well as providing API to provide dynamic functionality for some of the dynamic updates.

 * Calvin Chen (clc162)
   * View related features.

## Design goals

#### What Features are Easy to Add
* The features that were made easy to add are new types of simulations, adding parameters and states to existing simulations, and adding new neighborhood/edge/shape types.

## High-level Design
From a high level design our classes were split into model and view with model further being split into simulation and util (configuration). 

The view was responsible for calling XmlUtils to store and load configuration files. For loading configuration files, the XmlUtils return an XmlData object, the view uses that to extract metadata information and create a new Simulation object. Then this simulation class's logic is run on user input (i.e. start) and the view updates the state accordingly based on the simulations returned values.

The simulation class can be seen as simulation uses values from the XmlData object to initialize a Grid with the given cell shapes and grid topology. Additionally, using reflection it creates a Rule with the parameters from the xml document which are encapsulated into the Parameters class. The Grid creates, stores, and updates the Cell's neighbor objects, these cells are also passed in the Rule they follow for update logic.

## Assumptions that Affect the Design
* Some assumptions that affected the design that made some of the additional features a bit haphazard was the assumption parameter values could always be represented by a String -> Double mapping and that cell states were the same for all simulations of the same type. These features were added to bits of the model portion (dynamic state handling, and the generic parameters), but they were not fully finished.
* Additionally some assumptions about the simulations were: 
  * Sand and Water, you traverse left to right, top to bottom
    * Sand always replaces air before it tries water (even if water directly below and air to the sides)
    * Sand tries going down and then either SE or SW
    * Water tries going down then either E or W
  * Segregation, only move to adjacent empty (could do any empty by passing the grid into the rule simulation, but we just chose adjacent empty)
  * For the langton's loop variety, used a static map as assumed those are just the rules/not modifiable they just are. Additionally if the different neighborhoods were in affect, assumed these would just break/did not need to handle them.
  * There's assumptions about Darwin, but since its not fully implemented it, just not going to mention it, same with the assumptions I made for indexing and interpreting toroidol and mirror, there were assumptions about how it would be displayed and thus how the math was calculated, since it was not finished this is the mention of it. Again, also not mentioning how assumptions were made about directions with these new types as again not done.


## Significant differences from Original Plan
* Don't think there were that many differences from the original plan as our original plan was pretty abstract, perhaps the biggest one was how we ended up handling cell states with the ints instead of the enums.

## How to Add Features
* For Darwin, can follow how to add simulation, but for the most part it was just finishing up how to implement the move rules and logic.
```
In model/util/SimulationTypes, add a new enum of the Simulation type, if it should have dynamic states, and (since we never got topology in the XML, if the default should be vonNeumann or Moore).

If the states are static, create a new CellStateHandler extending CellStateHandler and add this to the handlerMap in CellStateFactory.

Then create a new SimTypeCell and new SimTypeRule in their respective folders. For functionality, just override the abstract methods with the behavior (ex. if everythign just is 1 after the first iteration, override Rule's @apply to return 1), for a normal cell, just override getSelf with this and getMaxState with its max state. (Since the validation method uses an overriden getMaxState, I couldn't figure out how to validate the initial constructor state using that method so there's an extra step of in the Cell constructor also validating that passed in state after setting it, just for precaution so maybe add that in.)

Then in XmlUtils update simTypeFromString with whatever type strings you want to convert to your SimType enum and also the maxFromSimType which returns the max state of a simulation type.

Lastly for view, create a new SimTypeCellView that extendsCellView with a default color map value.
```

* For all of the grid topologies / dynamic implement of all of the grid topologies, it included adding to the XML fields for grid topologies and thus a button on a front end to call the updateGridTopology method in simulation. 
* For stepping forward and back, also just needed a front end view button to calkl simulation.step() and simulation.stepBack()
* For RPS again the handling of how to add a simulation as there was dynamic state handling.
* For dynamically updating non double simulation parameters for game of life, there is something that shows that on the unmerged branch.
* For simulation styles, add that to the color configuration we have in the current XML document, and also apply the same handling of configured vs default from the collors to grid properties / seeing grid or not.
* Number of iterations just needed something on the front end to display getTotalIterations, same thing with CellInformation as the information could be got from getState and getStateLength needed the view display to show this information on hover.
* CellStates needed the front end to when displaying the cell states make a map of what  cell is in the current state and make a graph of that.
