#!/bin/bash

# usage: ./mdbconvert.sh accessfile.mdb mysqldatabasename localhost

TABLES=$(mdb-tables -1 $1)

MUSER="root"
MPASS="Collect%2017"
MDB="$2"
MHOST="$3"

MYSQL=$(which mysql)

for t in $TABLES
do
    $MYSQL -h $MHOST -u $MUSER -p$MPASS $MDB -e "DROP TABLE IF EXISTS $t"
done

mdb-schema $1 mysql | $MYSQL -h $MHOST -u $MUSER -p$MPASS $MDB

for t in $TABLES
do
    mdb-export -D '%Y-%m-%d %H:%M:%S' -I mysql $1 $t | $MYSQL -h $MHOST -u $MUSER -p$MPASS $MDB
done

