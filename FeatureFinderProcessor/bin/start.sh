#!/bin/bash
path="$( dirname "$( which "$0" )" )"
echo $path
nohup java -Dserver.properties=$path/serverurls.properties -Dserver.port=8090 -jar /home/vboxuser/featurefinder/FeatureFinderProcessor/bin/FeatureFinderProcessor-1.1.jar > /home/vboxuser/featurefinder/FeatureFinderProcessor/bin/log.txt 2>&1 &
echo $! > /home/vboxuser/featurefinder/FeatureFinderProcessor/bin/pid.file