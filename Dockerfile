FROM amazoncorretto:17
LABEL authors="Piotrek"
ADD target/DietPlanner-0.0.1-SNAPSHOT.jar DietPlanner-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "DietPlanner-0.0.1-SNAPSHOT.jar"]