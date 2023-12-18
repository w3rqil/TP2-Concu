---

# Trabajo Práctico Final de Programación Concurrente 2023

Este repositorio contiene desarrollo e implementación del proyecto final de la materia Programación Concurrente 2023, el cual consiste en la implementación de un sistema doble procesamiento de imágenes utilizando redes de Petri y programación concurrente en Java.

## Descripción del Trabajo

El trabajo se basa en el desarrollo de un sistema de procesamiento de imágenes que utiliza una red de Petri para modelar el flujo de trabajo y un monitor de concurrencia para guiar la ejecución del sistema. El objetivo es implementar políticas de procesamiento que prioricen ciertos segmentos de la red y garantizar un balance equitativo en el procesamiento de imágenes.

## Contenido del Repositorio

- **Diagramas**: Contiene los diagramas de clases y de secuencia utilizados en la implementación del sistema.
- **Red de Petri**: Incluye la representación de la red de Petri utilizada para modelar el Sistema Doble de Procesamiento de imágenes.
- **Código Fuente**: Contiene el código fuente en Java que implementa el sistema, incluyendo el monitor de concurrencia, la red de Petri y las políticas de procesamiento.
- **Informe**: Documenta la solución implementada, explica el código y los criterios adoptados, y presenta los resultados obtenidos durante la ejecución del sistema.
- **Parser de expresiones regulares:**

## Requerimientos

El trabajo cumple con los siguientes requerimientos:

- Implementación de la red de Petri utilizando la herramienta PIPE y verificación de todas sus propiedades.
- Modelado del sistema con objetos en Java, haciendo uso de un monitor de concurrencia para guiar la ejecución de la red de Petri.
- Implementación de una política de procesamiento que priorice el segmento izquierdo en la etapa 3, corroborando que reciba el 80% de la carga.
- Realización de múltiples ejecuciones con 200 invariantes completados para demostrar la equidad de la política implementada en el balance de carga en los invariantes.
- Análisis de tiempos y cumplimiento de invariantes de plazas y transiciones en la red de Petri.

## Integrantes del Grupo

- Cabrera, Augusto Gabriel.
- Mansilla, Josías Leonel.
- Moroz, Esteban Mauricio.
- Pallardó, Agustín.
- Villar, Federico Ignacio.

## Fecha de Entrega

18 de diciembre de 2023.
