package descompresor;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

class Nodo {
    int valor;
    Nodo izquierdo, derecho;
    Nodo padre;

    // Constructor para inicializar el nodo con un valor
    public Nodo(int valor) {
        this.valor = valor;
        this.izquierdo = null;
        this.derecho = null;
        this.padre = null;
    }

    // Método que verifica si el nodo es una hoja (no tiene hijos)
    public boolean esHoja() {
        return this.valor != -1; // Un nodo hoja tiene un valor diferente de -1
    }

    // Método que verifica si ambos hijos del nodo están ocupados (no son nulos)
    public boolean ambosHijosOcupados() {
        return this.izquierdo != null && this.derecho != null;
    }
}

public class Descompresor {

    // Método principal que lee el archivo comprimido y lo descomprime
    public void readPkz(String filePkz) {
        StringBuilder datosArbol = new StringBuilder();
        StringBuilder datosComprimidos = new StringBuilder();
        boolean leyendoBS = false; // Bandera para saber si estamos leyendo la cantidad de bits por bloque
        int bs = 0; // Cantidad de bits por bloque
        int ascii;
        Map<Integer, String> tablaHash = new HashMap<>(); // Tabla hash para almacenar los valores y sus rutas

        try (BufferedInputStream input = new BufferedInputStream(new FileInputStream(filePkz))) {
            File file = new File(filePkz);
            long size = file.length(); // Obtiene el tamaño del archivo
            ascii = input.read(); // Lee el primer byte (NF)
            System.out.println("  " + size + "\tSize");
            System.out.println("  " + ascii + "\tNF");

            // Calcula el tamaño del árbol según el valor de NF
            int tree = (ascii * 2 + 1) * 2;
            ascii = input.read(); // Lee el siguiente byte
            
            // Lee los datos del archivo comprimido
            while (ascii != -1) {
                if (tree == 0) {
                    System.out.println("  " + ascii + "\tBS");
                    bs = ascii; // Establece la cantidad de bits por bloque
                    leyendoBS = true;
                } else {
                    if (!leyendoBS) {
                        datosArbol.append(ascii).append(" "); // Si no estamos leyendo BS, estamos leyendo el árbol
                    } else {
                        String binario = String.format("%8s", Integer.toBinaryString(ascii)).replace(' ', '0');
                        datosComprimidos.append(binario); // Si estamos leyendo BS, almacenamos los bits comprimidos
                    }
                }
                tree--; // Decrementa el contador de bytes para leer el árbol
                ascii = input.read(); // Lee el siguiente byte
            }

            // Muestra los datos del árbol
            System.out.println("Datos del arbol: " + datosArbol.toString().trim());

            // Agrupa los bits comprimidos según la cantidad de bits por bloque
            String datosZipAgrupados = agruparBits(datosComprimidos.toString(), bs);
            System.out.println("Datos ZIP agrupados: " + datosZipAgrupados);

            // Construye el árbol de Huffman a partir de los datos del árbol
            Nodo raiz = construirArbol(datosArbol.toString().split(" "), tablaHash);
            System.out.println("Arbol de Huffman construido con exito.");

            // Muestra la tabla hash con las rutas de los bytes
            System.out.println("Tabla Hash con rutas:");
            for (Map.Entry<Integer, String> entry : tablaHash.entrySet()) {
                int valor = entry.getKey();
                System.out.println("Byte: " + valor + ", Ruta: " + entry.getValue());
            }

            // Traduce el mensaje comprimido utilizando la tabla hash
            byte[] archivoDecodificado = traducirMensaje(datosZipAgrupados, tablaHash);
            System.out.println("Archivo decodificado correctamente.");

            // Guarda el archivo decodificado
            String rutaArchivoResultado = "data/resultado3"
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + ".mp4"; //CAMBIAR RUTA
            guardarArchivoBinario(archivoDecodificado, rutaArchivoResultado);
            System.out.println("Archivo descomprimido guardado en: " + rutaArchivoResultado);

        } catch (IOException e) {
            e.printStackTrace(); // Manejo de errores
        }
    }

    // Método para agrupar los bits comprimidos según la cantidad de bits por bloque
    public static String agruparBits(String datosComprimidos, int bs) {
        StringBuilder resultado = new StringBuilder();
        int longitud = datosComprimidos.length();
        int bloques = longitud / bs; // Determina cuántos bloques se necesitan

        // Agrupa los bits en bloques de tamaño 'bs'
        for (int i = 0; i < bloques * bs; i += bs) {
            resultado.append(datosComprimidos.substring(i, i + bs));
        }
        return resultado.toString(); // Devuelve los datos agrupados
    }

    // Método que construye el árbol de Huffman a partir de los datos del árbol
    public static Nodo construirArbol(String[] bits, Map<Integer, String> tablaHash) {
        Nodo raiz = new Nodo(-1); // Crea el nodo raíz con un valor de -1 (no es una hoja)
        Nodo actual = raiz; // Nodo actual para recorrer el árbol

        // Recorre los bits del árbol y construye la estructura de nodos
        for (int i = 0; i < bits.length; i++) {
            String bit = bits[i];

            // Si encuentra un "0", crea un nuevo nodo interno
            if (bit.equals("0")) {
                if (i + 1 < bits.length && bits[i + 1].equals("0")) {
                    Nodo nuevoNodo = new Nodo(-1); // Crea un nuevo nodo interno
                    if (actual.izquierdo == null) {
                        actual.izquierdo = nuevoNodo;
                        nuevoNodo.padre = actual;
                    } else if (actual.derecho == null) {
                        actual.derecho = nuevoNodo;
                        nuevoNodo.padre = actual;
                    }

                    // Retrocede al nodo anterior si ambos hijos están ocupados
                    while (actual.ambosHijosOcupados() && actual.padre != null) {
                        actual = actual.padre;
                    }
                    actual = nuevoNodo;
                    i++; // Salta al siguiente bit
                }
            } else if (bit.equals("1")) { // Si encuentra un "1", es una hoja
                int valorHoja = Integer.parseInt(bits[++i]); // Lee el valor de la hoja (número asociado)
                if (actual.izquierdo == null) {
                    actual.izquierdo = new Nodo(valorHoja); // Crea la hoja izquierda
                    actual.izquierdo.padre = actual;
                    tablaHash.put(valorHoja, generarRuta(actual.izquierdo).substring(1)); // Añade la ruta a la tabla hash
                } else if (actual.derecho == null) {
                    actual.derecho = new Nodo(valorHoja); // Crea la hoja derecha
                    actual.derecho.padre = actual;
                    tablaHash.put(valorHoja, generarRuta(actual.derecho).substring(1)); // Añade la ruta a la tabla hash
                }

                // Retrocede al nodo anterior si ambos hijos están ocupados
                while (actual.ambosHijosOcupados() && actual.padre != null) {
                    actual = actual.padre;
                }
            }
        }

        return raiz; // Devuelve el nodo raíz del árbol de Huffman
    }

    // Método que genera la ruta (código binario) desde la raíz hasta un nodo específico
    public static String generarRuta(Nodo nodo) {
        StringBuilder ruta = new StringBuilder();
        Nodo actual = nodo;
        while (actual.padre != null) {
            // Si el nodo es hijo izquierdo, agrega "0", si es hijo derecho, agrega "1"
            ruta.insert(0, actual.padre.izquierdo == actual ? "0" : "1");
            actual = actual.padre;
        }
        return ruta.toString(); // Devuelve la ruta generada
    }

    // Método que traduce los datos comprimidos usando la tabla hash
    public static byte[] traducirMensaje(String datosComprimidos, Map<Integer, String> tablaHash) {
        Map<String, Integer> rutasInversas = new HashMap<>();
        // Invierte la tabla hash
        for (Map.Entry<Integer, String> entry : tablaHash.entrySet()) {
            rutasInversas.put(entry.getValue(), entry.getKey());  
        }

        ByteArrayOutputStream salida = new ByteArrayOutputStream(); // Flujo de salida para los datos decodificados
        StringBuilder rutaActual = new StringBuilder();

        // Recorrer los bits comprimidos
        for (char bit : datosComprimidos.toCharArray()) {
            rutaActual.append(bit); // Agrega el bit actual a la ruta
            // Si la ruta actual es un código completo, escribe el valor correspondiente en la salida
            if (rutasInversas.containsKey(rutaActual.toString())) {
                salida.write(rutasInversas.get(rutaActual.toString())); // Escribe el valor decodificado
                rutaActual.setLength(0); // Restablece la ruta
            }
        }

        return salida.toByteArray(); // Devuelve el archivo descomprimido
    }

    // Método que guarda el archivo descomprimido en formato binario
    public static void guardarArchivoBinario(byte[] contenido, String rutaArchivo) throws IOException {
        try (FileOutputStream salida = new FileOutputStream(rutaArchivo)) {
            salida.write(contenido); // Escribe el contenido binario en el archivo
        }
    }

    // Método principal de la aplicación
    public static void main(String[] args) {
        Descompresor descompresor = new Descompresor();
        descompresor.readPkz("data/Def Leppard - Hysteria (Instrumental w_ AI).mp4.pkz"); // Llama al método para descomprimir el archivo
    }
}
