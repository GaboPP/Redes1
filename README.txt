## Development
Para ejecutar la tarea se debe tener una consola en este directorio para el servidor, escribir "make",
luego escribir "make run", para la conección de clientes hay que ir a la ruta "src/Cliente/" y escribir "make", luego
"make run" y se ejecutará el cliente.

## Hostname
En client.java hay una variable llamada hostname esta se debe reemplazar por el ip de la red en la que se ejecuta el servidor, 
en caso de ejecutar localmente se deja igual.

## Logs
Hay que tener especial cuidado al intentar loguearse; se debe escribir [usuario]/[contraseña] en caso contrario, 
lanzará un error, por que no se considera una palabra sin 'slach' como válida, las contraseñas y usuarios de acceso están en login.txt