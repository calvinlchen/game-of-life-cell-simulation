# cell society

## Jessica Chen, Calvin Chen, Kyaira Boughton

## Project Overview
Cell Society is a JavaFX-based framework for running and exploring a variety of cellular automata simulations. It separates concerns between a model layer (simulation logic and configuration) and a view layer (rendering, controls, and file management), making it straightforward to add new rules or tweak visuals without tightly coupling code.

### Supported simulations
The simulator ships with multiple rule sets, ranging from classic to more experimental automata. Current options include Game of Life, Percolation, Fire, Segregation, WaTor predator-prey, Falling Sand, Rock–Paper–Scissors, Langton’s Loop variants, Chou/Reg dynamics, Petelka, and an in-progress Darwin simulation. Each type carries metadata about dynamic state support and default grid topology, which the model layer uses to initialize rules and grids.

### User interface highlights
The main window presents a zoomable, pannable grid alongside a control panel for common actions: play/pause, speed adjustments, clearing or reloading a simulation, random Game of Life generation, grid flips, parameter editing, and saving/loading configurations. Users can toggle gridlines, switch visual themes, and view a legend that maps cell states to colors, while an information area surfaces status messages during runs.

### Code structure
Simulation configuration is centralized through the `SimulationTypes` metadata and the flexible `Parameters`/`GenericParameters` classes, allowing rules to declare defaults and optional non-numeric inputs without proliferating subclasses. XML utilities handle loading and saving configurations for reuse. The view layer is composed of modular UI components (control panel, state legend, information box, zoomable grid) orchestrated by `UserView`, which manages animation timelines, file dialogs, and scene styling. This modularity keeps simulation logic isolated from presentation and encourages extension via reflection-friendly rule creation and reusable UI helpers.

### Timeline

 * Start Date: 1 / 28

 * Finish Date: 3 / 6

 * Hours Spent:
   * Jessica
     * Plan: 3 hr
     * Basic: 16 hr?
     * Robust: 24 hr?
     * Change: 60 hr?
     * Additional Doc's/Presentation: 12 hr?
     * some of this time was doing like things together so I guess in total: 100?
     * I have no sense of time though



### Attributions

 * Resources used for learning (including AI assistance)
   * Kyaira:
     * https://docs.oracle.com/javase/8/docs/api/javax/xml/parsers/DocumentBuilderFactory.html
     * https://docs.oracle.com/javase/8/docs/api/javax/xml/parsers/DocumentBuilder.html
     * https://docs.oracle.com/javase/8/docs/api///?org/w3c/dom/package-summary.html
       * On document builders ^^
 
 * Resources used directly (including AI assistance)
   * Kyaira:
     * https://stackoverflow.com/questions/428073/what-is-the-best-simplest-way-to-read-in-an-xml-file-in-java-application
       * reading an xml file ^^
   * Jessica:
     * Design patterns: https://refactoring.guru/design-patterns
     * Settign up logging: https://www.baeldung.com/java-system-out-println-vs-loggers
     * Generics: https://docs.oracle.com/javase/tutorial/java/generics/types.html
     * ChatGPT: generating java doc methods, helping with setting up reflections


### Running the Program

 * Main class:
   * found in `main/java/cellsociety/Main.java`

 * Data files needed: 
   * all found in `data`
     * this is where to find the XML projects ot the test the simulation
   * also need the files in `src/resources` 
     * this includes the logging configuration
     * resource properties for strings displayed and style guides for view

 * Format of XML files:
   <!-- Ky can you fill this part out -->

 * Interesting Information:
   * Interesting XML Files: 
   <!-- Ky can you do this -->

   * Key/Mouse inputs:
   <!-- Calvin can you do this -->


### Notes/Assumptions

 * Assumptions or Simplifications:
   * Files will all be located in the same location
   * For more detail about the simulation assumptions look into the design doc to avoid redundancy
    <!-- Jessica later link the specifics -->

### For Grading
 * Known Bugs:

 * Features implemented / Features unimplemented
   * Look into the checklist to see the detailed breakdown and how the project could support unimplemnted features

 * Noteworthy Features:
   * Look at wiki for some better docs especially for overview in structure I know they not done still :(


### Assignment Impressions
 * Jessica: I really liked the assignment itself as a whole, especially the iterational steps as we learned about the design patterns, I thought that was good (ex. each checkpoint there was a new simulation that either challenged or validated prior assumptions).
   * Hoever, one thing that could maybe be improved is like with some of the refactoring periods, even though we had two weeks to refactor, the times we learned the main thing that would help us refactor was in the second week so sometimes the first week kinda just felt silly.


