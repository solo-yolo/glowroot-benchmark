FROM azul/zulu-openjdk

ENV MAVEN_MAJOR_VERSION 3
ENV MAVEN_VERSION 3.3.9
ENV GLOWROOT_VERSION 0.9.1
ENV NEWRELIC_VERSION 4.10.0

# build and install benchmark
COPY pom.xml /workspace/
COPY src /workspace/src/

# install curl, git(?), unzip, mvn
RUN apt-get update \
  && apt-get -y install curl git unzip \
  && curl http://archive.apache.org/dist/maven/maven-$MAVEN_MAJOR_VERSION/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz | tar xz -C /usr/share \
  && mv /usr/share/apache-maven-$MAVEN_VERSION /usr/share/maven \
  && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn \
  && rm -r /var/lib/apt/lists/*

# install glowroot
RUN curl -L https://github.com/glowroot/glowroot/releases/download/v$GLOWROOT_VERSION/glowroot-$GLOWROOT_VERSION-dist.zip > glowroot-dist.zip \
  && unzip glowroot-dist.zip \
  && rm glowroot-dist.zip

# install newrelic
RUN mkdir newrelic \
  && curl -L http://central.maven.org/maven2/com/newrelic/agent/java/newrelic-agent/$NEWRELIC_VERSION/newrelic-agent-$NEWRELIC_VERSION.jar > newrelic/newrelic.jar
COPY newrelic.yml newrelic/

RUN mkdir stackify
COPY /usr/local/stackify/stackify-java-apm/stackify-java-apm.jar stackify/

RUN (cd workspace && mvn package) \
  && cp workspace/target/benchmarks.jar /

EXPOSE 8080
EXPOSE 4000

COPY example.yml /
COPY docker-entrypoint.sh /

ENTRYPOINT ["/docker-entrypoint.sh"]
