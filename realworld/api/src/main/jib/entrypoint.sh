#!/bin/sh

echo "The application will start in ${BEFORE_START_SLEEP}s..." && sleep ${BEFORE_START_SLEEP}
echo java ${JAVA_OPTS} -noverify -XX:+AlwaysPreTouch -Djava.security.egd=file:/dev/./urandom -cp /app/resources/:/app/classes/:/app/libs/* "com.msjc.cloud.Application"  "$@"
exec java ${JAVA_OPTS} -noverify -XX:+AlwaysPreTouch -Djava.security.egd=file:/dev/./urandom -cp /app/resources/:/app/classes/:/app/libs/* "com.msjc.cloud.Application"  "$@"
