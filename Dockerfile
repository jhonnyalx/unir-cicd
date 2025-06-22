FROM python:3.6-slim

RUN mkdir -p /opt/calc /opt/calc/results

WORKDIR /opt/calc

COPY pyproject.toml requirements.txt ./
COPY app ./app
COPY test ./test
RUN pip install -r requirements.txt
