#!/bin/bash
path="$( dirname "$( which "$0" )" )"
echo $path
nohup java -Dserver.properties=$path/serverurls.properties -Dserver.port=8060 -jar /home/vboxuser/featurefinder/FeatureFinderIngestor/bin/FeatureFinderIngestor-1.1.jar > /home/vboxuser/featurefinder/FeatureFinderIngestor/bin/log.txt 2>&1 &
echo $! > /home/vboxuser/featurefinder/FeatureFinderIngestor/bin/pid.file