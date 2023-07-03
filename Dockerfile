FROM openjdk:19
EXPOSE 8087
ADD target/user-management-1.jar user-management-1.jar
ENTRYPOINT ["java","-jar","user-management-1.jar"]