import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Archivero {
    public static List<Persona> leerArchivo(String archivo) {
        List<Persona> personas = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(archivo))) {
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
                String[] partes = linea.split(", ");
                String nombre = partes[0];
                int edad = Integer.parseInt(partes[1]);
                personas.add(new Persona(nombre, edad));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return personas;
    }

    public static void serializar(List<Persona> personas, String archivo) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(personas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Persona> deserializar(String archivo) {
        List<Persona> personas = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            personas = (List<Persona>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return personas;
    }

    public static void escribirArchivo(List<Persona> personas, String archivo) {
        try (PrintWriter writer = new PrintWriter(new File(archivo))) {
            for (Persona persona : personas) {
                writer.println(persona);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
