host=spark@192.168.100.137
dir=/home/spark/
target=$host:$dir
tmp=target/maxDriver
name=pharbers-max-0.1

mvn clean install -DskipTests
rm -rf $tmp
mkdir $tmp
cp -r pharbers_config $tmp
cp -r jar $tmp
cp -r target/lib $tmp
cp -r target/$name.jar $tmp
cp -r target/$name.jar $tmp/jar/
scp -r $tmp $target

