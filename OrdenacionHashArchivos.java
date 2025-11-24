import java.io.*;
import java.util.*;

public class OrdenacionHashArchivos {
    
    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        try {
            // Directorio donde buscar archivos
            File directorio = new File("C:/Archivos/");
            
            if (!directorio.exists() || !directorio.isDirectory()) {
                System.out.println("El directorio no existe.");
                return;
            }
            
            // Listar archivos disponibles
            File[] archivos = directorio.listFiles();
            if (archivos == null || archivos.length == 0) {
                System.out.println("No hay archivos en ");
                return;
            }
            
            System.out.println("ARCHIVOS DISPONIBLES en:");
            for (int i = 0; i < archivos.length; i++) {
                if (archivos[i].isFile()) {
                    System.out.println((i + 1) + ". " + archivos[i].getName());
                }
            }
            
            // Seleccionar archivo
            System.out.print("Selecciona el número del archivo a ordenar: ");
            String inputSeleccion = reader.readLine();
            int seleccion = Integer.parseInt(inputSeleccion);
            
            if (seleccion < 1 || seleccion > archivos.length) {
                System.out.println("Selección inválida.");
                return;
            }
            
            File archivoEntrada = archivos[seleccion - 1];
            System.out.println("Archivo seleccionado: " + archivoEntrada.getName());
            
            // Nombre del archivo de salida
            System.out.print("Ingresa el nombre para el archivo ordenado (sin extensión): ");
            String nombreSalida = reader.readLine().trim();
            
            if (nombreSalida.isEmpty()) {
                nombreSalida = "hash_ordenado_" + archivoEntrada.getName();
            } else {
                nombreSalida += ".txt";
            }
            
            File archivoSalida = new File(directorio, nombreSalida);
            
            // Procesar y ordenar
            System.out.println("Iniciando Ordenación con Falsa Hash...");
            ordenarArchivoHash(archivoEntrada.getAbsolutePath(), archivoSalida.getAbsolutePath());
            
            System.out.println("¡Proceso completado!");
            System.out.println("Archivo ordenado guardado como: " + archivoSalida.getAbsolutePath());
            
        } catch (NumberFormatException e) {
            System.err.println("Error: Debes ingresar un número válido.");
        } catch (IOException e) {
            System.err.println("Error de entrada/salida: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                System.err.println("Error al cerrar BufferedReader: " + e.getMessage());
            }
        }
    }
    
    // Método principal que ordena un archivo usando Falsa Hash
    public static void ordenarArchivoHash(String rutaArchivoEntrada, String rutaArchivoSalida) {
        try {
            // Leer nombres del archivo
            List<String> nombres = leerArchivo(rutaArchivoEntrada);
            
            if (nombres.isEmpty()) {
                System.out.println("El archivo está vacío o no contiene datos válidos.");
                return;
            }
            
            System.out.println("Nombres leídos: " + nombres.size());
            
            // Mostrar array original
            System.out.println("DATOS ORIGINALES:");
            imprimirListaCompleta(nombres);
            
            // Aplicar Ordenación con Falsa Hash
            List<String> nombresOrdenados = ordenacionFalsaHash(nombres);
            
            // Guardar resultado ordenado
            guardarArchivo(nombresOrdenados, rutaArchivoSalida);
            
            // Mostrar array ordenado final
            System.out.println("DATOS ORDENADOS FINALES:");
            imprimirListaCompleta(nombresOrdenados);
            
        } catch (IOException e) {
            System.err.println("Error al procesar archivos: " + e.getMessage());
        }
    }
    
    // **ORDENACIÓN CON FALSA HASH - Explicación:**
    // Una "Falsa Hash" no es una verdadera tabla hash, sino una simulación
    // que usa el concepto de buckets (cubetas) para agrupar elementos
    // y luego ordenarlos individualmente
    
    private static List<String> ordenacionFalsaHash(List<String> nombres) {
        System.out.println("INICIANDO ORDENACIÓN CON HASH");
        System.out.println("");
        
        // Crear buckets basados en la primera letra (simulando función hash)
        Map<Character, List<String>> buckets = new TreeMap<>();
        
        System.out.println("PASO 1: Creando buckets por primera letra");
        
        // Distribuir nombres en buckets según su primera letra
        for (String nombre : nombres) {
            if (nombre == null || nombre.isEmpty()) continue;
            
            char primeraLetra = Character.toUpperCase(nombre.charAt(0));
            
            if (!buckets.containsKey(primeraLetra)) {
                buckets.put(primeraLetra, new ArrayList<>());
            }
            buckets.get(primeraLetra).add(nombre);
        }
        
        // Mostrar distribución inicial en buckets
        System.out.println("DISTRIBUCIÓN EN BUCKETS:");
        for (Map.Entry<Character, List<String>> entry : buckets.entrySet()) {
            System.out.println("   Bucket '" + entry.getKey() + "': " + entry.getValue().size() + " elementos");
        }
        
        System.out.println("PASO 2: Ordenando individualmente cada bucket");
        
        // Ordenar cada bucket individualmente
        for (Map.Entry<Character, List<String>> entry : buckets.entrySet()) {
            char letra = entry.getKey();
            List<String> bucket = entry.getValue();
            
            System.out.println("   Ordenando bucket '" + letra + "'...");
            Collections.sort(bucket);
            
            // Mostrar contenido del bucket ordenado
            System.out.print("   Bucket '" + letra + "' ordenado: ");
            for (int i = 0; i < Math.min(5, bucket.size()); i++) {
                System.out.print(bucket.get(i) + " ");
            }
            if (bucket.size() > 5) System.out.print("...");
            System.out.println();
        }
        
        System.out.println(" PASO 3: Combinando buckets ordenados");
        
        // Combinar todos los buckets ordenados
        List<String> resultado = new ArrayList<>();
        for (Map.Entry<Character, List<String>> entry : buckets.entrySet()) {
            resultado.addAll(entry.getValue());
        }
        
        System.out.println("Total de elementos en resultado: " + resultado.size());
        System.out.println("ORDENACIÓN CON HASH COMPLETADA");
        System.out.println("");
        
        return resultado;
    }
    
    
    // Método para imprimir la lista completa
    private static void imprimirListaCompleta(List<String> lista) {
        for (int i = 0; i < lista.size(); i++) {
            System.out.print(lista.get(i));
            if (i < lista.size() - 1) {
                System.out.print(" | ");
            }
            // Salto de línea cada 5 elementos para mejor visualización
            if ((i + 1) % 5 == 0) {
                System.out.println();
            }
        }
        System.out.println();
    }
    
    // Leer nombres desde archivo
    private static List<String> leerArchivo(String rutaArchivo) throws IOException {
        List<String> nombres = new ArrayList<>();
        BufferedReader fileReader = new BufferedReader(new FileReader(rutaArchivo));
        String linea;
        int lineasVacias = 0;
        
        while ((linea = fileReader.readLine()) != null) {
            linea = linea.trim();
            
            if (!linea.isEmpty()) {
                nombres.add(linea);
            } else {
                lineasVacias++;
            }
        }
        fileReader.close();
        
        if (lineasVacias > 0) {
            System.out.println("Se ignoraron " + lineasVacias + " líneas vacías.");
        }
        
        return nombres;
    }
    
    // Guardar nombres ordenados en archivo
    private static void guardarArchivo(List<String> nombres, String rutaArchivo) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo));
        for (String nombre : nombres) {
            writer.write(nombre);
            writer.newLine();
        }
        writer.close();
    }
}