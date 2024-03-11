FROM openjdk:8-jre
MAINTAINER Patrick Dunne <pjdunne@featurefinder.app>
ADD ./bin/FeatureFinderDatabase-1.0.jar FeatureFinderDatabase-1.0.jar
ADD ./bin/serverurls.properties serverurls.properties
ADD ./bin/documentstore.db documentstore.db
MKDIR tmp
EXPOSE 8030
ENTRYPOINT ["sh", "-c", "java -Dserver.properties=serverurls.properties -Dserver.port=8030 -jar FeatureFinderDatabasie-1.0.jar"]
