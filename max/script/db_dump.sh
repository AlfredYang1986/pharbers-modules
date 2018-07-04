target_ip=127.0.0.1
target_port=28102
db_names=('pharbers-max-client' 'pharbers-max-market' 'pharbers-max-data' 'pharbers-max-aggregation')

for db in ${db_names[*]};
do
    mongodump --host ${target_ip} --port ${target_port} --db $db -o ../target/dump/
done;