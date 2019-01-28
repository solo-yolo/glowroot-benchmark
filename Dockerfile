FROM azul/zulu-openjdk

ARG stackify_license
ARG stackify_env=local

ENV MAVEN_MAJOR_VERSION 3
ENV MAVEN_VERSION 3.3.9
ENV GLOWROOT_VERSION 0.9.1
ENV NEWRELIC_VERSION 4.10.0

# build and install benchmark
COPY pom.xml /workspace/
COPY src /workspace/src/

# install curl, git(?), unzip, mvn, sudo (required by stackify installation script)
RUN apt-get update \
  && apt-get -y install curl git unzip sudo \
  && curl http://archive.apache.org/dist/maven/maven-$MAVEN_MAJOR_VERSION/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz | tar xz -C /usr/share \
  && mv /usr/share/apache-maven-$MAVEN_VERSION /usr/share/maven \
  && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn \
  && rm -r /var/lib/apt/lists/*

RUN curl -L https://s1.stackify.com/Account/AgentDownload/Linux > stackify.tar.gz \
  && tar -zxvf stackify.tar.gz stackify-agent-install-32bit \
  && (cd stackify-agent-install-32bit && ./agent-install.sh --key $stackify_license --environment $stackify_env) \
  && mkdir stackify \
  && ln -s /usr/local/stackify/stackify-java-apm/stackify-java-apm.jar stackify/stackify-java-apm.jar
  && rm stackify.tar.gz

# install glowroot
RUN curl -L https://github.com/glowroot/glowroot/releases/download/v$GLOWROOT_VERSION/glowroot-$GLOWROOT_VERSION-dist.zip > glowroot-dist.zip \
  && unzip glowroot-dist.zip \
  && rm glowroot-dist.zip

# install newrelic
RUN mkdir newrelic \
  && curl -L http://central.maven.org/maven2/com/newrelic/agent/java/newrelic-agent/$NEWRELIC_VERSION/newrelic-agent-$NEWRELIC_VERSION.jar > newrelic/newrelic.jar
COPY newrelic.yml newrelic/

RUN (cd workspace && mvn package) \
  && cp workspace/target/benchmarks.jar /

EXPOSE 8080
EXPOSE 4000

COPY example.yml /
COPY docker-entrypoint.sh /

ENTRYPOINT ["/docker-entrypoint.sh"]
