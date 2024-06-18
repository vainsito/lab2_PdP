## Lab 2 - Programación orientada a objetos

### Dependencias

Necesitan Java 17, tanto el JRE como el JDK. En Ubuntu, pueden instalarlo con:

```bash
apt install openjdk-17-jdk openjdk-17-jre
```
### Creacion del Cluster de Spark (escribir bien)
1) Empezamos con
``
./sbin/start-master.sh
``

2) Abrimos en un navegador
localhost:8080

3) Configuramos conf/spark-env.sh
``
SPARK_WORKER_CORES=2
``
``
SPARK_WORKER_INSTANCES=2
``
``
SPARK_WORKER_MEMORY=2g
``
*(ahora al hacer worker se hacen dos)*
*(dos cores y 2g memoria para que se repartan entre los dos)*

4) Creamos los workers
``
./start-worker.sh spark://darkaraus-System-Product-Name:7077  -m 1G -c 1
``
### Compilación y ejecución

- Para compilar el código ejecutamos `make`, lo cual crea todos los archivos compilados en el directorio `./bin`

- Para correr el código ejecutamos `make run ARGS="<flags>"` donde <flags> son las flags que corresponden a los args toma la función principal del software.

- `make clean` borra los archivos `.class` que se generan en la compilación

