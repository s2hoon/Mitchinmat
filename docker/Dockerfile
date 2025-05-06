FROM docker.elastic.co/elasticsearch/elasticsearch:7.9.0

# Nori 플러그인 설치
RUN elasticsearch-plugin install analysis-nori

# 사전 및 동의어 파일 복사
COPY dictionary.txt /usr/share/elasticsearch/config/analysis/dictionary.txt
COPY synonyms.txt /usr/share/elasticsearch/config/analysis/synonyms.txt
