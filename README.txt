README.TXT
==========
This is Team 23's submission for CS6310's Project3: Heated Planet Simulation.

To build the code, run the following commands in bash:

	./build.sh

To run the code, either use ./run.sh or run from bin dir, ie,
   
	cd bin
	java -cp .:lib/h2-1.4.182.jar PlanetSim.Demo [-p #] [-g #] [-t #]

where:
  -p #: The precision of the data to be stored, in decimal digits after the 
        decimal point. The default is to use the number of digits storable in 
        a normalized float variable. The maximum is the number of digits 
        storable in a normalized double variable. The minimum is zero.
  -g #: The geographic accuracy (sampling rate) of the temperature data to be 
        stored, as an integer percentage of the number of grid cells saved 
        versus the number simulated. The default is 100%; that is, a value is 
        stored for each grid cell.
  -t #: The temporal accuracy of the temperature data to be stored, as an 
        integer percentage of the number of time periods saved versus the number 
        computed. The default is 100%; that is, all computed values should be 
        stored.
        
Alternatively use the provided run script:

	./run.sh
