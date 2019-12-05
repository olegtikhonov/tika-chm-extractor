
FROM openjdk:8u232-jdk

RUN set -x && apt-get update && apt-get install -y && rm -rf /var/lib/apt/lists/*

ENV JAR_FILE=tika-server.jar

COPY tika-server/target/tika-server-1.23.jar tika-server-1.23.jar

# port 5656, accepts all outside connections
CMD ["java","-jar","tika-server-1.23.jar", "--port", "5656", "--host", "0.0.0.0"]
