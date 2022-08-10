FROM eclipse-temurin:17-alpine as build
WORKDIR /workspace/app

COPY gradle gradle
COPY build.gradle settings.gradle gradlew ./
COPY src/main/java src/main/java
COPY src/test src/test

RUN ./gradlew build -x test
RUN mkdir -p build/libs/dependency && (cd build/libs/dependency; jar -xf ../*.jar)

COPY src/main/resources/application.properties build/libs/dependency/BOOT-INF/classes/

FROM eclipse-temurin:17-jre-alpine
ARG DEPENDENCY=/workspace/app/build/libs/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib/ /app/lib/
COPY --from=build ${DEPENDENCY}/META-INF/ /app/META-INF/
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes/ /app/

ENTRYPOINT ["java","-cp","app:app/lib/*","com.eatnow.cart.CartApplication"]