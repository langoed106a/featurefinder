#!/bin/bash
path="$( dirname "$( which "$0" )" )"
echo $path
nohup java -Dserver.properties=$path/serverurls.properties -Dserver.port=8080 -jar /home/vboxuser/featurefinder/FeatureFinderInterface/bin/FeatureFinderInterface-1.1.jar > /home/vboxuser/featurefinder/FeatureFinderInterface/bin/log.txt 2>&1 &
echo $! > /home/vboxuser/featurefinder/FeatureFinderInterface/bin/pid.file