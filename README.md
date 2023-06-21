# MapTool

A simple command line program to read Minecraft's map.dat files.

## Usage
To start, use `java -jar MapTool.jar` and enter the absolute path to your server's main world's `data` folder.
For repeated use, you can specify the data folder using the --datafolder command line argument.

## Commands
| Command       | Description                                                                            |
|---------------|----------------------------------------------------------------------------------------|
| lookup \<id>  | Displays some basic information about a map, including it's dimension and coordinates. |
| view \<id>    | Shows the map as it would look in-game using ANSI escape codes.                        | 
| export \<id>  | Exports the given map as a PNG file.                                                   |
| similar \<id> | Lists other map IDs that have the same center coordinates.                             |