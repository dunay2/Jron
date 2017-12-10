/*
  Clase que gestiona y hace de controladora del proyecto.

  @author Diego Javier Ríos Sánchez
 * @version 0.3 20171028
 */


import java.util.ArrayList;
import java.util.Objects;

class Robot {

    //constantes
    private static final int NUM_CONFIG_LINES = 2;
    private static boolean trace;
    private final ArrayList<String> textMatrix;
    private final String[] parameters = new String[NUM_CONFIG_LINES];
    private final String outFile;

    /**
     * //* Constructor de objetos de la clase Robot
     * // * Recibe tres parámetros de entrada
     * //  * String inFile, nombre del fichero de entrada
     * // * String outFile , nombre del fichero de salida
     * // * boolean ... trace como parámetro opcional un valor booleano
     * //* que indica si se desea realizar traza
     */

    private Robot(String inFile, String outFile) {

        //Declaramos un objeto de tipo HandleFile
        this.outFile = outFile;
        HandleFile handleFile = new HandleFile(inFile, outFile);

        //Procedemos a la lectura del archivo de datos
        try {
            handleFile.readFile();
        } catch (Exception e) {
            //Mostrar error
            System.out.println(e.getMessage());
            System.exit(-1);
        } finally {
            //Extraemos los datos del fichero de texto y los guardamos en textMatrix, arraylist de string
            textMatrix = handleFile.getMatrix();

            //Extraemos los parámetros del array
            for (int i = 0; i < NUM_CONFIG_LINES; i++)//leemos los parametros
                parameters[i] = textMatrix.get(i);

            //Eliminamos los elementos que son parámetros de textMatrix
            for (int i = 0; i < NUM_CONFIG_LINES; i++)
                textMatrix.remove(0);

        }
    }

    //main: entrada al proyecto
    public static void main(String args[]) {
        //controlamos el número de parámetros: 1 || 2 || 3
        //   validar primero que todos los parametros estan ok

        if (args.length > 0) {
            String string = args[0];
            switch (args.length) {
                case 1:
                    if (string.indexOf("-h") == 0) {
                        printHelp(); //muestra la ayuda y salimos
                    } else {
                        System.out.println("Asumimos que tenemos el fichero de entrada" + string.indexOf("-h"));
                        Robot robot = new Robot(args[0], "");
                        robot.explore();

                    }
                    break;
                case 2:
                    if (string.indexOf("-h") == 0) {
                        printHelp(); //muestra la ayuda y salimos
                        break;

                    }

                    if (string.indexOf("-t") == 0) {
                        System.out.println("Asumimos que tenemos la entrada de fichero con traza en pantalla");
                        Robot robot = new Robot(args[1], "");
                        setTrace();
                        robot.explore();

                    } else {
                        System.out.println("Asumimos que tenemos la entrada de dos ficheros, entrada y salida");
                        Robot robot = new Robot(args[0], args[1]);
                        robot.explore();

                    }
                    break;
                case 3:
                    if (string.indexOf('-') == 0) {

                        if (Objects.equals(string, "-t")) {
                            System.out.println("Asumimos que tenemos entrada de dos ficheros y salida de traza en fichero");
                            Robot robot = new Robot(args[1], args[2]);
                            setTrace();
                            robot.explore();
                            break;
                        }

                        if (Objects.equals(string, "-h")) {
                            printHelp(); //muestra la ayuda y salimos
                            break;
                        }

                        //que salte al mensaje de error
                    }

                default:
                    System.out.println("Entrada no válida");

            }

        } else {
            System.out.println("Entrada no válida");

        }
    }

    private static void setTrace() {
        trace = true;
    }

    //Propósito: mostrar la ayuda
    private static void printHelp() {
        System.out.println("SINTAXIS");
        System.out.println("robot [-t][-h][fichero_entrada] [fichero_salida]");
        System.out.println("-t Traza la aplicación del algoritmo a los datos");
        System.out.println("-h Muestra esta ayuda");
        System.out.println("fichero_entrada Nombre del fichero de entrada");
        System.out.println("fichero_salida Nombre del fichero de salida");
    }

    //Propósito:
    //Llamar al constructor del grafo, cargar los nodos y buscar la salida
    private void explore() {
        QueueGraph graph = new QueueGraph
                (Integer.parseInt(parameters[0]), Integer.parseInt(parameters[1]), textMatrix);
        //activamos la traza si se ha solicitado
        if (trace) graph.setTrace();

        graph.setDebugFile(outFile);

        // Establecemos la distancia desde los vértices a la salida mediante dijkstra
        graph.findWayOut();

        //mostramos la salida
        graph.showExit();

        // if (outFile!= null)
        // handleFile.writeToFile(ArrayList<String> text);

    }
}
