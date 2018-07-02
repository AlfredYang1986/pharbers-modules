target_ip=192.168.100.176
target_port=28102
db_names=('pharbers-max-client' 'pharbers-max-market' 'pharbers-max-data' 'pharbers-max-aggregation')

for db in ${db_names[*]};
do
    mongorestore --host ${target_ip} --port ${target_port} --db $db ../target/dump/$db
done;