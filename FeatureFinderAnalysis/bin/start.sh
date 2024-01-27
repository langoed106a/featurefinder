#!/bin/bash
path="$( dirname "$( which "$0" )" )"
echo $path
nohup java -Dserver.properties=$path/serverurls.properties -Dserver.port=8020 -jar /home/vboxuser/featurefinder/FeatureFinderAnalysis/bin/FeatureFinderAnalysis-1.0.jar > /home/vboxuser/featurefinder/FeatureFinderAnalysis/bin/log.txt 2>&1 &
echo $! > /home/vboxuser/featurefinder/FeatureFinderAnalysis/bin/pid.file