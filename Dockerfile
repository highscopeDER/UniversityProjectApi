FROM gradle:latest AS cache

RUN mkdir /app
WORKDIR "/app"

EXPOSE 8080

# CMD ["sleep","3650d"]
