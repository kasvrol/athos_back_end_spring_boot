#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE DATABASE athos_evento_db;
    CREATE DATABASE athos_campeonato_db;
    CREATE DATABASE athos_pagamento_db;
EOSQL