# Cell Society Checklist For Final

### 3

- Kyaira Boughton (keb125)
- Jessica Chen (jc939)
- Calvin Chen (clc162)

## Design
*to lazy to add right now*

## Change

### Simulations

- [ ] Darwin
    - [ ] @Jessica state transition rules
    - [ ] @Ky 2 new species programs and 5 configuration files that run successfully to test this
      simulation
    - *(the 2 new species programs might be Jessica's stuff I not sure)*

### Configuration

- [ ] Simulation Styles: allow at least three different ways for simulation to be styled
    - [ ] @Ky name of colors or image files for cell states
    - [ ] @Ky grid properties (edge policy, cell shape, neighbor arrangement)
    - [ ] @Ky another one of your choice (outlines, language,....)

### Grid Topology

*Jessica would like these*

- [ ] Toroidal
    - [ ] @Jessica wrapping edges, edges wrap around horizontally and vertically
    - [ ] @Ky 1 configuration file
- [ ] Mirror
    - [ ] @Jessica mirrored edges, edges act as mirrors reflected as many of cells adjacent to the
      edge as needed to make a full neighborhood
    - [ ] @Ky 1 configuration file

### Cell Neighborhoods

*Jessica would like these*

- Von Neuman
    - [x] @Jessica, support neighbors that are directly adjacent to it grid setup (its adjacentgrid)
    - [ ] @Jessica make thigns actually use this based on the config file
    - [ ] @Ky 1 configuration file
- Extended Moore Neighborhood
    - [ ] @Jessica, support moore neighborhood 2 cells away
    - [ ] @Jessica change your like check if neighbor in direction function to instead of being -1/1
      if its <0 and >0
    - [ ] @Ky 1 configuration file
      *NOTE! langton's loops won't work with these neighborhoods but such is life*

### Cell Shape

*again jessica would like these, feel free to disagree calvin*

- [ ] Hexagonal
    - [ ] @Jessica support this cell shape for all neighborhoods and edges x.x
    - [ ] @Ky 1 configuration file
    - [ ] @Calvin
- [ ] Triangular
    - [ ] @Jessica support this cell shape for all neighborhoods and edges x.x
    - [ ] @Ky 1 configuration file
    - [ ] @Calvin

### GUI
*honestly these seem like the 3 easiest for you calvin lol, except for the cell shape, feel free to choose diferently with custom or undo*
- [ ] Grid Edges
  - [ ] @Calvin allow users to change the grid edge policy of a CURRENTLY LOADED SIMULATION
  - [ ] @Jessica allow grid edges to be dynamically updated 
- [ ] Cell Neighborhood
  - [ ] @Calvin allow users to change the grid neighbor of a CURRENTLY LOADED SIMULATION
  - [ ] @Jessica allow grid neighbors to be dynamically updated
- [ ] Cell Shape
  - [ ] @Calvin allow users to change the cell shape of a CURRENTLY LOADED SIMULATION
  - [ ] @Jessica allow cell shape to be dynamically updated

### Different View
- [ ] Number of Iterations
  - [ ] @Jessica store the number of iterations of step
  - [ ] @Calvin display number that is count of number of iterations run so far

*calvin choose your 3 favorites*
- [ ] @Calvin display info about individual cell when user hover / click on it
- [ ] @Calvin histogram showing population of cell states over time
- [ ] @Calvin show graph of change in population totals since the last step
- [ ] @Calvin mini grid so user can see entire grid
- [ ] @Calvin zoom in or out of the display

## General and Robust

### Simulations

- [ ] Generalized Conway's Game of Life
    - [x] @Jessica state transition rules support list for born and survive
    - [ ] @Ky parse B/S and S/B variations in the XML
    - [ ] @Ky 3 configuration files
- [ ] Falling Sand/Water
    - [x] @Jessica state transition rules
    - [ ] @Ky 3 different configuration files
- [ ] Rock Paper Scissors
    - [x] @Jessica state transition rules
    - [ ] @Ky 3 different configuration files
- [ ] Langton's Loop
    - [x] @Jessica state transition rules
    - [ ] @Ky 3 different configuration files
- [ ] ChouReggie2 Langton's Loop
    - [x] @Jessica state transition rules
    - [ ] @Ky 3 different configuration files
- [ ] Petelka's Langton's Loop
    - [x] @Jessica state transition rules
    - [ ] @Ky 3 different configuration files

### XML Configuration Error Handling

- [ ] Input Missing Parameters
    - [ ] @Ky display error message to user if config fire missing metadata
    - [ ] @Ky 1 config file
- [ ] Invalid Value check
    - [ ] @Ky display error message to user if invalid value (non existent simulation, bad XML
      tag...)
    - [ ] @Ky 1 config file
- [ ] Invalid Cell State Check
    - [ ] @Ky display error message to user if invalid cell state
    - [ ] @Ky 1 config file
- [ ] Grid Bounds Check
    - [ ] @Ky display error message to user if numcells don't align with the grid's bound
    - [ ] @Ky 1 config file
- [ ] File Format Validation
    - [ ] @Ky display error message to user if empty or badly formatted or nonXML file
    - [ ] @Ky 1 config file

### XML Configuration Initialization

- [ ] Random Configuration by Total States
    - [ ] @Ky allow random assignment of cells to specified number of states
    - [ ] @Ky 2 config file
- [ ] Random Configuration by Probability Distribution
    - [ ] @Ky allow random assignment of cells to specified proportion of states
    - [ ] @Ky 2 config file

### Configuration Preferences

- [ ] App Preferences
    - [ ] @Ky implement file storage options to allow users to save and load default preferences
    - [ ] @Calvin allow users to do this, and use these unless being overriden

### GUI

- [ ] @Calvin splash screen at start that shows name, language, color theme, and start new
  simulation
- [ ] @Calvin allow customizes of language used for UI, can be configured once in teh splash
  screen (at least 2 languages)
- [ ] @Calvin allow users to select from different UI themes
- [ ] Error Reporting
    - [ ] @Jessica simulation errors use resource properties
    - [ ] @Ky xml and factory errors use resource properties
    - [ ] @Calvin catch and display errors thrown
- [ ] Cell State Colors Customization
    - [ ] @Calvin allow customizations of colors of cell state
    - [ ] @Ky colors should be an option provided in the XML config file
- [ ] @Calvin allow users to run multiple simulations concurrently
- [ ] Step Simulation
    - [x] @Jessica create stepBack() and store the x last seen states
    - [ ] @Ky add the number of states to save as a parameter
    - [ ] @Calvin provide button for step and stepBack()

### Dynamic Updates

- [ ] Simulation Parameters
    - [ ] @Jessica refactor parameters so can get all keys, and pass in for each key new values
    - [ ] @Calvin allow users to change configuration of parameters of a currently loaded simulation
- [ ] @Calvin allow user to toggle on and off cell outlines of a grid
- [ ] @Calvin allow users to flip the grid, just a view status

## Basic and Test

### Simulations

- [ ] Conway's Game of Life
    - [x] @Jessica state transition rules
    - [ ] @Ky 5 different config files, of 5 simulations 2 configurations where active cells are on
      the edge and reached an expected state
- [ ] Percolation
    - [x] @Jessica state transition rules
    - [ ] @Ky 3 different config files, of 3 simulations 1 configurations where active cells are on
      the edge and reached an expected state
- [ ] Spreading of Fire
    - [x] @Jessica state transition rules
    - [ ] @Ky 5 different config files, of 5 simulations 2 configurations where active cells are on
      the edge and reached an expected state
- [ ] Schellings Model of Segregation
    - [x] @Jessica state transition rules
    - [ ] @Ky 5 different config files, of 5 simulations 2 configurations where active cells are on
      the edge and reached an expected state
- [ ] Wa-Tor World
    - [x] @Jessica state transition rules
    - [ ] @Ky 8 different config files, of 8 simulations 3 configurations where active cells are on
      the edge and reached an expected state
- [ ] @Ky create 3 simulation configuration files for Game of Life that replicate well known
  patterns

### XML Configuration

- [ ] @ky implement XML based configuration simulation
    - contains simulation type, title, authors, description, grid dimensions, initial states,
      specific simulation parameters

### GUI

- Cell Grid View
    - [ ] @Calvin when simulation loaded from config, state of simulation displayed in Grid View
    - [x] @Jessica create a function for Calvin to step through simulation and get state of cells
- [ ] @Calvin when simulation is loaded display simulation metadata as well as state colors and
  parameter values
- [ ] @Calvin allow user to load new simulation from simulation file
- [ ] @Calvin when user presses start simulation should execute steps at a rate
- [ ] @Calvin when user presses pause simulation should stop advancing step and display last step's
  state
- [ ] @Calvin provide mechanism to slow down or speed up rate of simulation steps
- [ ] Save Simulation State as XML
    - [ ] @Calvin provide mechanism to save current state of the simulation
    - [ ] @Ky provide way to save current simulation as an XML