FROM openjdk:19
EXPOSE 8087
ADD target/user_management.jar user_management.jar
ENTRYPOINT ["java","-jar","user_management.jar"]