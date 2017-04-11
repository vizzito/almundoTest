Solucion Propuesta: Producer/Consumer Pattern, implementado mediante una cola Thread-Safe (BlockingQueue)

Extras/Plus
- Dar alguna solución sobre qué pasa con una llamada cuando no hay ningún empleado libre.
En el caso de no tener ningun empleado libre, la solucion implementada esperara hasta que en la cola de 
operadores se libere al menos uno, ya uqe esta cola es quien tiene prioridad sobre las demas.
se podra observar el mensaje:

---busy operators---
---busy supervisors---
---busy directors---
waiting for operators queue.... call: 38::pool-3-thread-4

en este caso al querer agregar la llamada, todas las colas de empleados se encontraban ocupadas, con lo cual asignara la llamada
ni bien se desocupe un lugar en la cola de operadores.


- Dar alguna solución sobre qué pasa con una llamada cuando entran más de 10 llamadas concurrentes.
En caso que entren mas de 10 llamadas concurrentes, estas esperaran hasta que haya lugar en la cola de empleados para poder asignar la llamada

- Agregar los tests unitarios que se crean convenientes.
agregados en /scr/main/test.
El unico test que puede garantizar el resultado esperado es checkAllAssignedOperators, el resto de los test, al poder ser los resultados
aleatoreos, solo verifican que la cantidad de llamadas ejecutadas sean las correctas.

- Agregar documentación de código.
diagrama de clases y secuencia agregado en src/main/resources.


