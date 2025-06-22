FROM jenkins/jenkins:lts-jdk17

USER root

# Variables de entorno
ENV DOCKER_GROUP_ID=999 \
    DEBIAN_FRONTEND=noninteractive \
    DOCKER_HOST=unix:///var/run/docker.sock \
    DOCKER_TLS_CERTDIR="" \
    JENKINS_HOME=/var/jenkins_home

# Instalar herramientas necesarias y Docker
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
        apt-transport-https \
        build-essential \
        ca-certificates \
        curl \
        gnupg \
        lsb-release \
        make \
        software-properties-common && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/* && \
    # Instalar Docker usando el script oficial
    curl -fsSL https://get.docker.com -o get-docker.sh && \
    sh get-docker.sh && \
    rm get-docker.sh && \
    # Configurar grupo docker y permisos
    groupmod -g ${DOCKER_GROUP_ID} docker || true && \
    usermod -aG docker jenkins && \
    # Asegurar que el socket de Docker tenga los permisos correctos
    mkdir -p /var/run && \
    touch /var/run/docker.sock && \
    chmod 666 /var/run/docker.sock

USER jenkins

# Instalar plugins de Jenkins
RUN jenkins-plugin-cli --plugins "htmlpublisher:1.31 docker-workflow:563.vd5d2e5c4007f"

# Verificación de salud mejorada
HEALTHCHECK --interval=30s --timeout=10s --retries=3 \
    CMD curl -f http://localhost:8080/login && \
        (docker info > /dev/null 2>&1 || exit 1) || exit 1

EXPOSE 8080 50000
