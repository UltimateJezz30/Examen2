import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;


public class Main {
    private static List<Persona> personas = new ArrayList<>();

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;


        while (running) {
            System.out.println("Menú:");
            System.out.println("1. Añadir");
            System.out.println("2. Muestre");
            System.out.println("3. Elimine");
            System.out.println("4. Modifica");
            System.out.println("5. Exporte a textfile");
            System.out.println("6. Procese y exporta mayores de 18");
            System.out.println("7. Salida");
            System.out.print("Elija su Opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    agregarPersona(scanner);
                    break;
                case 2:
                    mostrarPersonas();
                    break;
                case 3:
                    eliminarPersona(scanner);
                    break;
                case 4:
                    modificarPersona(scanner);
                    break;
                case 5:
                    exportarPersonas();
                    break;
                case 6:
                    procesarYExportarPersonas();
                    break;
                case 7:
                    running = false;
                    break;
                default:
                    System.out.println("Opción inválida. Trate de nuevo.");
            }
        }
        scanner.close();
    }

    private static void iniciales(String nombreArchivo) {

        File archivo = new File(nombreArchivo);

        try {
            BufferedReader entrada = new BufferedReader(new FileReader(archivo));
            String lectura = entrada.readLine();
            while ( lectura != null) {
                System.out.println(lectura);
                lectura = entrada.readLine();
            }
            entrada.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace(System.out);
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }

    private static void leerArchivoTexto(String nombreArchivo) {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(nombreArchivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(", ");
                String nombre = partes[0];
                int edad = Integer.parseInt(partes[1]);
                personas.add(new Persona(nombre, edad));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void agregarPersona(Scanner scanner) throws IOException, ClassNotFoundException {
        System.out.print("Ingrese nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Ingrese edad: ");
        int edad = scanner.nextInt();
        scanner.nextLine();
        personas.add(new Persona(nombre, edad));
        serializarPersonas();
    }

    private static void mostrarPersonas() {
        if (personas.isEmpty()) {
            System.out.println("personasiniciales.txt");
        } else {
            personas.forEach(System.out::println);
            iniciales("personasiniciales.txt");
        }
    }

    private static void eliminarPersona(Scanner scanner) throws IOException, ClassNotFoundException {
        System.out.print("Ingrese uno a eliminar: ");
        String nombre = scanner.nextLine();
        personas.removeIf(p -> p.getNombre().equalsIgnoreCase(nombre));
        serializarPersonas();
    }

    private static void modificarPersona(Scanner scanner) throws IOException, ClassNotFoundException {
        System.out.print("Ingrese uno a modificar: ");
        String nombre = scanner.nextLine();
        for (Persona persona : personas) {
            if (persona.getNombre().equalsIgnoreCase(nombre)) {
                System.out.print("Ingrese nuevo nombre: ");
                persona.setNombre(scanner.nextLine());
                System.out.print("Ingrese nueva edad: ");
                persona.setEdad(scanner.nextInt());
                scanner.nextLine();
                serializarPersonas();
                return;
            }
        }
        System.out.println("Persona Inlocalizada.");
    }

    private static void exportarPersonas() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("personas.txt"))) {
            for (Persona persona : personas) {
                writer.write(persona.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void procesarYExportarPersonas() {
        List<Persona> personasProcesadas = personas.stream()
                .filter(p -> p.getEdad() >= 18)
                .map(p -> new Persona(p.getNombre().toUpperCase(), p.getEdad()))
                .sorted(Comparator.comparing(Persona::getNombre))
                .collect(Collectors.toList());

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("procesados.txt"))) {
            for (Persona persona : personasProcesadas) {
                writer.write(persona.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Cantidad de Procesados: " + personasProcesadas.size());
    }

    private static void serializarPersonas() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("personas.bin"))) {
            oos.writeObject(personas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void deserializarPersonas() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("personas.bin"))) {
            personas = (List<Persona>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
         e.printStackTrace();
        }
    }
}
