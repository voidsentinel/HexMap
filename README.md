# HexMap
A small project to generate a map with hexagonal cells. 
The current goal is to generate a medieval capitalism-like game

## Area of work (Model) 
- map generation with height map generation and operation on terrain (Blur, Islandize, ...), 
along with operation on map (set temperature, humidity, soil fertility, biome...)
- place cities on "logical" positions for big and small, along with repartition

## Area of Work (View)
- map representation either as flat hexagon (minecraft-like), or slopped one 
(Endless Legend - like), with easy passage from one view to the other
- modification of the cell view (cell color depending on the data viewed, such as humidity)
