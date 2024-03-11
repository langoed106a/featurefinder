FROM openjdk:8-jre
MAINTAINER Patrick Dunne <pjdunne@featurefinder.app>
ADD ./bin/FeatureFinderEnglish-1.0.jar FeatureFinderEnglish-1.0.jar
ADD ./bin/serverurls.properties serverurls.properties
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java -Dserver.properties=serverurls.properties -Dserver.port=8050 -jar FeatureFinderEnglish-1.0.jar"]
