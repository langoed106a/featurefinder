#!/bin/bash
path="$( dirname "$( which "$0" )" )"
echo $path
nohup java -Dserver.properties=$path/serverurls.properties -Dserver.port=8030 -jar /home/vboxuser/featurefinder/FeatureFinderDatabase/bin/FeatureFinderDatabase-1.0.jar > /home/vboxuser/featurefinder/FeatureFinderDatabase/bin/log.txt 2>&1 &
echo $! > /home/vboxuser/featurefinder/FeatureFinderDatabase/bin/pid.file