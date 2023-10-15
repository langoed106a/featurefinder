#!/bin/bash
path="$( dirname "$( which "$0" )" )"
nohup java -Dserver.properties=$path/serverurls.properties -Dserver.port=8050 -jar /home/vboxuser/featurefinder/FeatureFinderEnglish/bin/FeatureFinderEnglish-1.0.jar > /home/vboxuser/featurefinder/FeatureFinderEnglish/bin/log.txt 2>&1 &
echo $! > /home/vboxuser/featurefinder/FeatureFinderEnglish/bin/pid.file