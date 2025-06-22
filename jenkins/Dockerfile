FROM jenkins/jenkins:lts

USER root

# Instalar herramientas necesarias
RUN apt-get update && \
    apt-get install -y \
    apt-transport-https \
    ca-certificates \
    curl \
    gnupg \
    lsb-release \
    software-properties-common

# Instalar Docker
RUN curl -fsSL https://get.docker.com -o get-docker.sh && \
    sh get-docker.sh

# Agregar usuario jenkins al grupo docker
RUN usermod -aG docker jenkins

# Limpiar cache de apt
RUN apt-get clean && \
    rm -rf /var/lib/apt/lists/*

USER jenkins

# Exponer el puerto de Jenkins
EXPOSE 8080

# Exponer el puerto para el agente JNLP
EXPOSE 50000
