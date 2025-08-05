# Estágio 1: Build da aplicação com Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copia o pom.xml primeiro para aproveitar o cache do Docker
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o resto do código-fonte e compila o projeto
COPY src ./src
RUN mvn package -DskipTests

# Estágio 2: Criação da imagem final com o JRE
FROM eclipse-temurin:17-jre-focal
WORKDIR /app

# Copia o .jar gerado no estágio de build
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta que a aplicação vai usar dentro do contêiner
EXPOSE 8080

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]