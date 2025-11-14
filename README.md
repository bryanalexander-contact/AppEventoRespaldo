EventLive - App de Gestión de Eventos
Descripción del proyecto:

EventLive es una aplicación móvil desarrollada en Kotlin con Jetpack Compose, destinada a la gestión y visualización de eventos. La app permite a los usuarios registrarse, iniciar sesión, crear eventos, y consultar información detallada de cada evento, incluyendo nombre, ubicación, descripción, duración y una imagen representativa. La arquitectura se basa en MVVM utilizando Room para almacenamiento local y ViewModels para la gestión reactiva de los datos, lo que garantiza que la interfaz se mantenga sincronizada con la información en tiempo real.
El diseño de la app prioriza la usabilidad, con formularios validados en tiempo real, animaciones suaves y un tema visual consistente que mejora la experiencia del usuario. Además, se integran recursos nativos del dispositivo para la gestión de imágenes, asegurando un flujo seguro y funcional al guardar y mostrar fotos de los eventos.

Estudiantes Bryan Alexander, Danko Gonzalez, Dylan Reveco

Funcionalidades implementadas:

1- Registro y login de usuarios con validación de datos en tiempo real.

2- Creación y listado de eventos con detalles completos.

3- Gestión de imágenes asociadas a los eventos, guardadas de manera local en el dispositivo.

4- Navegación intuitiva entre pantallas mediante NavGraph.

5- Animaciones y transiciones para mejorar la experiencia de usuario.

6- Arquitectura modular que permite fácil mantenimiento y escalabilidad.

7- Uso de Room para persistencia local de usuarios y eventos.

8- Validaciones completas de campos en formularios de eventos y usuario.



Tecnologías y herramientas usadas por nosotros:

1- Kotlin y Jetpack Compose para la interfaz.

2- Room como base de datos local.

3- ViewModel y StateFlow / LiveData para gestión de estado.

4- Coil para carga de imágenes.

5- Material3 para diseño visual consistente.

6- Git y GitHub para control de versiones.


Pasos para ejecutar el proyecto:

Clonar el repositorio:

git clone https://github.com/bryanalexander-contact/AppEventoRespaldo.git

o descargarlo desde nuestro Github y abrirlo con Android Studio o tambien abrir Android Studio pero con new project desde repository https://github.com/bryanalexander-contact/AppEventoRespaldo.git
Abrir el proyecto en Android Studio.

Sincronizar dependencias y compilar el proyecto.

Ejecutar la app en un emulador o dispositivo físico.

Registrar un usuario y comenzar a crear y visualizar eventos
