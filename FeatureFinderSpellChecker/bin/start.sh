#!/bin/bash
path="$( dirname "$( which "$0" )" )"
echo $path
nohup java -Dserver.properties=$path/serverurls.properties -Dserver.port=8040 -jar /home/vboxuser/featurefinder/FeatureFinderSpellChecker/bin/FeatureFinderSpellChecker-1.1.jar > /home/vboxuser/featurefinder/FeatureFinderSpellChecker/bin/log.txt 2>&1 &
echo $! > /home/vboxuser/featurefinder/FeatureFinderSpellChecker/bin/pid.file