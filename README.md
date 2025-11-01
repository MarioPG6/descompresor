#  Descompresor Huffman en Java  
**Descompresión de archivos mediante un árbol binario en preorden**

Este proyecto implementa la descompresión de archivos con estructura `.pkz` utilizando el algoritmo de Huffman. El árbol de Huffman es reconstruido a partir de la cabecera del archivo comprimido, permitiendo la decodificación eficiente del contenido original.

---

## Características

- Reconstrucción del árbol de Huffman desde los metadatos del archivo  
- Decodificación mediante rutas binarias almacenadas en una tabla hash  
- Soporte para lectura de datos binarios  
- Generación del archivo descomprimido de forma automática  
- Validado con archivos multimedia y texto  

---

## Estructura del archivo `.pkz`

| Sección | Descripción |
|--------|-------------|
| Byte inicial | Número de hojas (NF) |
| Datos del árbol | Preorden: nodos internos (0) y hojas (1 + valor ASCII) |
| BS | Cantidad de bits por bloque |
| Datos comprimidos | Flujo binario decodificable |

---

## Funcionamiento del descompresor

1. Se lee NF para calcular la longitud del árbol
2. Se reconstruye el árbol de Huffman mientras se llena la tabla hash
3. Se agrupan los datos comprimidos según `BS`
4. Se decodifican los bits utilizando rutas binarias del árbol
5. Se genera el archivo de salida descomprimido

---

## Ejecución

### Requisitos

- Java 8 o superior

### Ejemplo de uso

```java
public static void main(String[] args) {
    Descompresor descompresor = new Descompresor();
    descompresor.readPkz("data/archivo.pkz");
}

