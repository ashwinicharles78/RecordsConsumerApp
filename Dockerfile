FROM public.ecr.aws/amazoncorretto/amazoncorretto:17-al2-native-jdk

COPY target/ConsumerApplication-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8082

CMD ["java", "-jar", "app.jar"]
