FROM europe-north1-docker.pkg.dev/cgr-nav/pull-through/nav.no/jre:openjdk-25

#ENV PATH=/var/run/secrets/db2-license/db2jcc_license_cisuz/db2jcc_license_cisuz.jar:$PATH
#ENV PATH=/var/run/secrets/db2-license/db2jcc_license_cu/:$PATH
#ENV PATH=/var/run/secrets/db2-license/db2jcc_license_cisuz.jar:$PATH

WORKDIR /app

COPY build/install/app/ /app/

ENTRYPOINT ["java", "-cp", "/app/lib/*", "no.nav.sikkerhetstjenesten.loggkamelproxy.LoggkamelProxyKt"]