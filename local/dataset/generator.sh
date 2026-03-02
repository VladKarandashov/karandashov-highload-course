#!/bin/bash

# честное спасибо нейронке за этот скрипт

# Использование:
# ./generator.sh [total_records] [batch_size] [output_file]
#
# По умолчанию:
# total_records = 1000000
# batch_size = 1000
# output_file = dataset.sql

# INSERT INTO monolith_social_network_service.users ("id", "passwordHash", "firstName", "lastName", "birthDate", "biography", "city") VALUES
# ('%s', '%s', '%s', '%s', '%s', '%s', '%s'),
# ...
# ('%s', '%s', '%s', '%s', '%s', '%s', '%s')
# ;

# id рандомный UUID
# password = $2a$12$EmApDyjH9k0aKRbvgiCTWenO4ahmirg7ubb2gXihHe.yiO.rv8Uvy (это хэш от "test-password")
# firstName = random из файла firstnames.txt
# lastName = random из файла lastnames.txt
# birthDate = random из файла dates.txt
# biography = firstName + " " + lastName + " " + " " + birthDate
# city = random из файла cities.txt

DIR="$(cd "$(dirname "$0")" && pwd)"
CITIES_FILE="$DIR/cities.txt"
DATES_FILE="$DIR/dates.txt"
FIRSTNAMES_FILE="$DIR/firstnames.txt"
LASTNAMES_FILE="$DIR/lastnames.txt"

TOTAL_RECORDS=${1:-1000000}
BATCH_SIZE=${2:-1000}
OUTPUT_FILE=${3:-$DIR/dataset.sql}

# Проверяем наличие файлов
for f in "$CITIES_FILE" "$DATES_FILE" "$FIRSTNAMES_FILE" "$LASTNAMES_FILE"; do
    if [[ ! -f "$f" ]]; then
        echo "Error: File $f not found" >&2
        exit 1
    fi
done

echo "Generating $TOTAL_RECORDS records (batch size: $BATCH_SIZE) into $OUTPUT_FILE..." >&2

awk -v total="$TOTAL_RECORDS" -v batch="$BATCH_SIZE" \
    -v cities_file="$CITIES_FILE" \
    -v dates_file="$DATES_FILE" \
    -v firstnames_file="$FIRSTNAMES_FILE" \
    -v lastnames_file="$LASTNAMES_FILE" '
function escape(s) {
    gsub(/'"'"'/, "'"'"''"'"'", s)
    return s
}
function uuid(  h, i, res) {
    h = "0123456789abcdef"
    res = ""
    for (i = 1; i <= 32; i++) {
        res = res substr(h, int(rand() * 16) + 1, 1)
        if (i == 8 || i == 12 || i == 16 || i == 20) res = res "-"
    }
    return res
}
BEGIN {
    srand()
    pwHash = "$2a$12$EmApDyjH9k0aKRbvgiCTWenO4ahmirg7ubb2gXihHe.yiO.rv8Uvy"
    
    # Загружаем файлы
    while ((getline line < cities_file) > 0) cities[nCities++] = line
    close(cities_file)
    while ((getline line < dates_file) > 0) dates[nDates++] = line
    close(dates_file)
    while ((getline line < firstnames_file) > 0) firstnames[nFirst++] = line
    close(firstnames_file)
    while ((getline line < lastnames_file) > 0) lastnames[nLast++] = line
    close(lastnames_file)

    if (nCities == 0 || nDates == 0 || nFirst == 0 || nLast == 0) {
        print "Error: Failed to read data files" > "/dev/stderr"
        exit 1
    }

    for (i = 0; i < total; i++) {
        if (i % batch == 0) {
            print "INSERT INTO monolith_social_network_service.users (\"id\", \"passwordHash\", \"firstName\", \"lastName\", \"birthDate\", \"biography\", \"city\") VALUES"
        }
        
        fName = firstnames[int(rand() * nFirst)]
        lName = lastnames[int(rand() * nLast)]
        bDate = dates[int(rand() * nDates)]
        city = cities[int(rand() * nCities)]
        
        # Экранируем одинарные кавычки
        efName = escape(fName)
        elName = escape(lName)
        ecity = escape(city)
        biography = efName " " elName "  " bDate
        
        printf "('\''%s'\'', '\''%s'\'', '\''%s'\'', '\''%s'\'', '\''%s'\'', '\''%s'\'', '\''%s'\'')%s\n", \
               uuid(), pwHash, efName, elName, bDate, biography, ecity, \
               (((i + 1) % batch == 0 || (i + 1) == total) ? ";" : ",")
    }
}
' > "$OUTPUT_FILE"

echo "Generation complete: $OUTPUT_FILE" >&2

