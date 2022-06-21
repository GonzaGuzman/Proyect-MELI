# Proyect-MELI (MercadoLibre Challenge)

Este repositorio contiene el challenge técnico de MercadoLibre, creado por Gonzalo Guzmán.

Contacto: https://www.linkedin.com/in/gonzalo-guzman/

# Primeros pasos

Para poder correr el proyecto se debe contar con el IDE Android Studio

# Pre-requisitos

El proyecto cuenta con dependencias, las cuales se encuentran en el archivo build.gradle, la instalación de estas dependencias se realiza automaticamente por el IDE.

# Instalación

Pasos para la ejecución del proyecto.

1. Clonar el repositorio
   
2. git clone https://github.com/GonzaGuzman/Proyect-MELI.git

3. Abrir con el IDE Android Studio

# Arquitectura usada
* [MVP]

# Descripción de la aplicación

La aplicación funciona como un navegador de productos de MercadoLibre:

Posee una barra de busqueda compartida en sus tres pantallas la cual sirve para buscar productos en base a palabras clave (query) y presentar las diferentes listas de resultados en la pantalla de detalles.


La Pantalla pricipal ademas de la barra de busqueda, cuenta con una vista y detalles del ultimo producto visto (el cual esta guardado en base de datos local) que al presionarla dirige a la pantalla de detalles de Items con dicha informacion para ser expuesta al usuario en dicha pantalla.

tambien presenta un listado de todas las categorias disponibles que al seleccionar una deriva la lista de resultados para ser presentados en la pantalla de detalles. 


La pantalla de detalles esta compuesta por la barra de busqueda y presenta las listas obtenidas en busqueda (obtenida de la barra de buscador), seleccion de categoria (obtenida al seleccionar cualquier categoria de la pantalla principal) 
y lista de base de datos (obtenida al pulsar el botón "Ver historial de navegacion") de productos vistos. Donde al realizar una nueva busqueda esta se actualiza con los resultados o en caso de selleccionar algun producto 
envia la informacion a la pantalla de detalles de items

La pantalla de detalles de items presenta muestra los detalles de un producto. Los elementos de esta pantalla incluyen la condición del producto (Nuevo/Usado), las unidades vendidas (en algunos casos aproximada), el titulo del producto,
una de imágen del producto, el precio del producto y un botón que permite abrir el producto en la web oficial de MercadoLibre (o en la app si esta instalada).

# Estilo visual

La aplicación intenta imitar lo mas fielmente posible el estilo de MercadoLibre, utilizando mismos colores, misma estructura general, mismas proporciones, mismos elementos, etc.

# Librerías utilizadas

* Las librerías estándar (Kotlin, AppCompat y Material)
* Las librerías estándar de prueba (JUnit)
* Libreria de prueba Mockito, utilizada para realizar unit test generando creando objetos simulados
* La librería RxJava, utilizada para manejar las tareas asíncronas y concurrentes
* La librería Retrofit, utilizada para realizar solicitudes REST a los endpoints de la API de MercadoLibre
* La librería Coil, utilizada para manejar la carga de imágenes en las views
* La libreria Roon, utilizada para crear y manipular base de datos local

Como fundamento de la elección de las librerías no estándar, la librería de RxJava hace el trabajo en paralelo mucho mas simple que usar Observables, Threads y enviar eventos al Looper del hilo principal,
la librería Retrofit es ampliamente utilizada y recomendada por la comunidad de desarrolladores de Android, manejando cada aspecto de la comunicación con el servidor remoto, 
la librería Coil simplifica la descarga y el caché de imágenes remotas, así como el redimensionamiento de las mismas, evitando tener que descargar la imagen de forma manual y tener que decodificar la imagen usando BitmapFactory (o algún procedimiento similar) para cargar las imágenes con las dimensiones optimas,
y la libreria Room que facilita la creacion de database local y el trabajo con objetos SQLiteDatabase.

# Endpoints utilizados
* https://api.mercadolibre.com/sites/MLA/   El sitio utilizado por la aplicación es MLA (MercadoLibre Argentina) 
* **/categories**: Utilizado para obtener las categorías de MercadoLibre, donde *$site* es el sitio (MLA, lo cual es igual para los demás endpoints)
* **/search?category=*$category: Utilizado para obtener los productos mas relevantes de una categoría, donde *$category* es el ID de la categoría
* **/search?q=*$query: Utilizado para obtener los productos que corresponden a una query, donde *$query* es la palabra clave

# Testeo

* Fueron creadas pruebas unitarias para los 3 componentes lógicos principales de la aplicación (HomePresenter, DetailPresenter, ItemPresenter) con una cobertura del 100% en cada caso.

Todas las pruebas fueron ejecutadas efectivamente sin ningún fallo en las APIs mencionadas anteriormente.

# Notas:

* Si ocurre un error que afecta la experiencia del usuario, este se vera reflejado en forma de snackBar en la pantalla.

* Con respecto a los permisos de la aplicación, solo se requiere el permiso a Internet.
