# Poker Face
The poker-face project runs as a command line tool and will print out various Poker hand ranks when given 5 card hands.

## Build
To build the project, clone the repo and run `mvn package`. This will create a runnable jar named `poker-face.jar` in the `target` directory.

## Run
Once built you can run the project with the command `java -jar target/poker-face.jar <<args>>` where `<<args>>` can be replaed with either:
1. 5 separate card strings or
2. A location of a file containing multiple poker hands