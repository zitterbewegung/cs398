#!/bin/sh
java -cp /home/r2q2/Desktop/cs398/cs398:/home/r2q2/Desktop/cs398/cs398/compute.jar -Djava.rmiserver.codebase=compute.jar -Djava.rmiserver.hostname=localhost -Djava.security.policy=server.policy engine.ComputeEngine
