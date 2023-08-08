FROM openjdk:8-jre
MAINTAINER Patrick Dunne <pjdunne@featurefinder.app>
ADD ./target/FeatureFinderInterface-1.1.jar FeatureFinderInterface-1.1.jar
ADD ./bin/serverurls.properties serverurls.properties
ADD ./bin/documentstore.db documentstore.db
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java -Dserver.port=8080 -jar FeatureFinderInterface-1.1.jar"]