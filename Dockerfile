FROM ibmcom/db2express-c

# usage:
# docker build -t local/db2 .
# docker run --rm -it -p 50000:50000 -e DB2INST1_PASSWORD=db2inst1-pwd -e LICENSE=accept  local/db2 db2start

# mount init data
# docker run --rm -it -v $(pwd)/initdb.d:/initdb.d -p 50000:50000 -e DB2INST1_PASSWORD=db2inst1-pwd -e LICENSE=accept  local/db2 db2start

RUN mkdir /initdb.d

COPY docker-entrypoint.sh /entrypoint.sh
