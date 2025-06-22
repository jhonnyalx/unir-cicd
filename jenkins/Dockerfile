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
    software-properties-common \
    make

# Instalar Docker
RUN curl -fsSL https://get.docker.com -o get-docker.sh && \
    sh get-docker.sh

# Agregar usuario jenkins al grupo docker y ajustar permisos
RUN groupadd -f docker && \
    usermod -aG docker jenkins && \
    chown root:docker /var/run/docker.sock && \
    chmod 660 /var/run/docker.sock || true

# Limpiar cache de apt
RUN apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Cambiar a usuario jenkins para instalar plugins
USER jenkins

# Instalar plugins de Jenkins necesarios
RUN jenkins-plugin-cli --plugins "htmlpublisher:1.31"

# Exponer el puerto de Jenkins
EXPOSE 8080

# Exponer el puerto para el agente JNLP
EXPOSE 50000
