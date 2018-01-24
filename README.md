# Distributed Data Analytics
## Solutions for the hands-on sessions on Akka and Spark
### 1. Akka
The [task](https://hpi.de/fileadmin/user_upload/fachgebiete/naumann/lehre/WS2017/DDA/4_Hands-on_Akka_Actor_Programming.pdf) was to crack hashes and find longest substrings.
The [solution](https://github.com/WGierke/distributed_data_analytics/blob/master/akka-cracka/solution.csv) can be obtained using the final [jar](https://github.com/WGierke/distributed_data_analytics/files/1564077/v1.1.zip) by executing  
`java -jar akka-cracka.jar --path path/to/students.csv`

### 2. Spark
The [task](https://hpi.de/fileadmin/user_upload/fachgebiete/naumann/lehre/WS2017/DDA/10_Hands-on_Spark_Batch_Processing.pdf) was to perform Inclusion Dependency Discovery using Spark.
The solution can be obtained using the final [jar](https://github.com/WGierke/distributed_data_analytics/releases/download/v2.0/fINDer.jar) by executing  
`java -jar fINDer.jar --path path/to/TPCH --cores NUMBER_OF_CORES`
