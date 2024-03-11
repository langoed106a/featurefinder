FROM openjdk:8-jre
MAINTAINER Patrick Dunne <pjdunne@featurefinder.app>
ADD ./bin/FeatureFinderInterface-1.1.jar FeatureFinderInterface-1.1.jar
ADD ./bin/serverurls.properties serverurls.properties
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java -Dserver.properties=serverurls.properties -Dserver.port=8080 -jar FeatureFinderInterface-1.1.jar"]
