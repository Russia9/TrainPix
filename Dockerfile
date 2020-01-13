FROM store/oracle/serverjre:8
LABEL maintainer="russia9@russia9.dev"
COPY ./target/trainpix-1.4-shaded.jar /trainpix/trainpix.jar
ENV LEVEL INFO
WORKDIR /trainpix
CMD java -jar trainpix.jar
