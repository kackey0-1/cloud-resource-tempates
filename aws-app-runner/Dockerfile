FROM public.ecr.aws/bitnami/java:17.0.3-1
VOLUME /tmp
ADD build/libs/aws-app-runner-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 80
RUN apt-get update
RUN apt-get -y install awscli
ENTRYPOINT env spring.datasource.password=$(aws ssm get-parameter --name /database/password --with-decrypt --region $AWS_REGION | grep Value | cut -d '"' -f4) java -Djava.security.egd=file:/dev/./urandom -jar /app.jar

