/*
  La clase QueueGraph define la matriz que vamos a crear donde se guardan los nodos y sus valores.
  Debe ser el robot

  @author Diego Javier Rios Sanchez
 * @version 1.0 20171014
 */


import java.util.*;

public class QueueGraph {
    private static final double INFINITO = Double.MAX_VALUE;
    //Propósito: Tener una cola de prioridad ordenada para gestionar los candidatos según su cercanía al origen
    private static final Comparator<LightNode> LengthComp = Comparator.comparingDouble(LightNode::getLength);
    // instance variables -
    private final int width; // Ancho matriz. cantidad columnas
    private final int height; //Alto matriz cantidad filas
    private final ArrayList<LightNode> MatrixNodeList;
    private final Debug debug;
    private int originNode; // Indice nodo origen
    private int exitNode; // Indice nodo salida
    private boolean hasSolution = false;// Variable booleana que indica si el algoritmo ha encontrado una solución
    private boolean trace = false;

    /**
     * Constructor de objectos de la clase Graph
     * recibe tamaño de matriz fila x columna y un ArraList de cadenas con los valores de los elementos de la matriz
     *
     */
    public QueueGraph(int height, int width, ArrayList<String> matrixValues) {

        this.width = width;
        this.height = height;

        MatrixNodeList = new ArrayList<>(height * width);

        /*Inicializamos la lista de nodos y su valor transformando la lista de cadenas recibida por parámetro en el constructor
        en valores de los elementos de la lista de nodos*/
        convertToNode(matrixValues);

        debug = new Debug();
    }

    //Propósito: Agregar los elementos del arraylist leído en fichero en una lista de objetos nodo del grafo
    private void convertToNode(ArrayList<String> matrixValues) {

        //roomType indica la tipología de la celda de la matriz
        String strFileLine = "", roomType = "";

        Iterator<String> it = matrixValues.iterator();//Iterador para recorrer todos los elementos del arraylist
        int row, column, contador, exitCount = 0, roboCount = 0, obstacleCount = 0;
        double currentWeigth = 0;
        // Para todos los elementos desde 0 hasta tamaño lista
        for (contador = 0; contador < (height * width); contador++) {
            // evaluar el tipo de datos que nos está llegando. Si es Obstáculo, Número, Robot o Salida.
            try {
                roomType = "";

                //Guardamos la string para el caso de que no sea numérico
                currentWeigth = Integer.parseInt(strFileLine = it.next());
                if (currentWeigth < 0) {
                    System.out.println("El sistema no soporta casillas con valor negativo");
                    System.exit(-1);
                }
                if (currentWeigth >= INFINITO) {
                    System.out.println("Error. Se ha superado el valor máximo coste de una casilla");
                    System.exit(-1);
                }

            } catch (Exception exc) {
                switch (strFileLine) {
                    case "O": // Obstáculo infranqueable
                        currentWeigth = INFINITO;
                        roomType = "Obstacle";
                        obstacleCount++;
                        break;

                    case "R": //inicio
                        currentWeigth = 0;
                        roomType = "Robot";
                        originNode = contador;
                        roboCount++;
                        break;

                    case "S": //salida

                        currentWeigth = 0;
                        roomType = "Exit";
                        //indicamos la posicion de la salida en la lista
                        exitNode = contador;
                        exitCount++;
                        break;

                    default:

                    {
                        System.out.println("El sistema no soporta casillas con caracteres desconocidos");
                        System.exit(-1);
                    }
                    currentWeigth = 0;
                    roomType = "Obstacle";
                }
            } finally {
                //Agregamos el nodo al grafo
                //peso, nombre i, tipo

                row = contador / width + 1;
                column = contador % width + 1;

                //comprobamos que la salida esté en un borde
                if (roomType == "Exit") {
                    if (!(row == 1 || row == height || column == 1 || column == width)) {
                        System.out.println("La salida no se encuentra en una de las casillas periféricas de la matriz");
                        System.exit(-1);
                    }
                }

                MatrixNodeList.add(new LightNode(contador, currentWeigth, roomType + "[" + row + "," + column + "]", roomType));

            }
        }
        //restricciones
        if (exitCount == 0 || exitCount > 1 || roboCount == 0 || roboCount > 1 || obstacleCount == 0
                || contador != (height * width)
                ) {
            System.out.println("Los valores de entrada no son correctos");
            System.out.println("Cantidad de entradas: " + roboCount);
            System.out.println("Cantidad de salidas: " + exitCount);
            System.out.println("Cantidad de obstáculos: " + obstacleCount);


            System.exit(-1);
        }

    }

    //Propósito: Obtener los elementos adyacentes a un indice de una matriz
    //La función recibe un indice de un array unidimensional y lo transforma a un elemento de una matriz
    //definida por la clase.
    //Parámetros: nodeIndex --> indice del elemento de la lista que
    //Devolución: Un array list de enteros con los indices de los elementos adyacentes al índice de entrada
    private ArrayList<Integer> getAdjacentNodes(int nodeIndex) {
        LightNode activeNode;
        //Creamos un array list de integer para guardar los índices
        // de los nodos adyacentes
        ArrayList<Integer> AdjacentNodeListIndexes = new ArrayList<>();

        int row, column; //variables de control fila /columna
        int numColumns = width; // ancho de la matriz

        int aux; // variable de control auxiliar que guarda los indices candidatos

        activeNode = MatrixNodeList.get(nodeIndex);//establecemos el nodo con el que estamos trabajando

        //Convertimos el indice del vector en un par de valores de identificación de elementos matriciales
        row = nodeIndex / numColumns;
        column = nodeIndex % numColumns;

        if (!activeNode.getCellType().equals("Obstacle")) // Si es obstáculo no agregamos adyacencia
            for (int auxRow = row - 1; auxRow < row + 2; auxRow++) // recorremos los elementos adyacentes al nodo en la matriz
                for (int auxColumn = column - 1; auxColumn < column + 2; auxColumn++) // recorremos todas las columnas
                {                                                                // adyacentes al nodo en la matriz
                    //obtenemos el valor del indice en la lista
                    aux = numColumns * auxRow + auxColumn;
                    //Comprobamos que la posicion del nodo adyacente esta dentro de los limites de la matriz
                    //y que no es el nodo activo
                    if (auxRow >= 0 && auxRow < height && auxColumn >= 0 && auxColumn < numColumns && aux != nodeIndex) {
                        //Agregamos el indice del nodo adyacente a la lista del nodo activo
                        //Excluimos los nodos ya explorados (tratados)
                        if (!MatrixNodeList.get(aux).getExplored())
                            AdjacentNodeListIndexes.add(aux);
                    }
                }
        return AdjacentNodeListIndexes;
    }

    //Propósito: Implementar Dijkstra
    public QueueGraph findWayOut() {

        //Inicializamos la clase de debug
        debug.start();
        if (trace) debug.setTrace();

        ArrayList<Integer> AdjacentNodeListIndexes;  //Control de los índices de los nodos adyacentes
        LightNode childNode, activeNode; //Variables locales para la gestión del nodo activo y sus nodos adyacentes

        //inicializamos el nodo activo con el nodo origen
        activeNode = MatrixNodeList.get(originNode);
        debug.print("inicicializamos el nodo activo con el nodo origen. Indice: " + originNode, 1);

        //La distancia desde origen a origen es 0
        activeNode.setLength(0);
        debug.print("La distancia desde origen a origen se establece en 0", 1);

        //Creamos una cola de prioridad para gestionar los candidatos ordenados por cercanía al origen
        Queue<LightNode> candidatepq = new PriorityQueue<>(3000, LengthComp);

        //Establecemos el origen como primer candidato y lo agregamos a la cola
        candidatepq.add(activeNode);
        debug.print("Establecemos el origen como primer candidato y lo agregamos a la cola de candidatos", 1);

        debug.print("Mientras queden nodos candidatos encolados", 1);

        long t1 = System.currentTimeMillis();
        // Hacer mientras existan nodos candidatos en la cola
        while (candidatepq.size() > 0) {
            //extraemos el nodo candidato y lo establecemos como nodo activo
            activeNode = candidatepq.poll();
            debug.print("Se extrae mejor candidato de la cola-->" + activeNode.getName(), 1);
            //lo marcamos como explorado
            activeNode.setExplored();
            debug.print("Candidato marcado como explorado", 1);
            //Guardamos los indices de los nodos adyacentes
            AdjacentNodeListIndexes = getAdjacentNodes(activeNode.getIndex());
            // Recorremos lista de los nodos adyacentes al nodo activo (candidato)
            debug.print("###Recorremos lista de los nodos adyacentes al nodo activo", 1);
            for (Integer AdjacentNodeListIndex : AdjacentNodeListIndexes) {
                //Obtenemos el nodo adyacente
                childNode = MatrixNodeList.get(AdjacentNodeListIndex);
                debug.print("Comprobando adyacente no explorado: " + childNode.getName(), 1);
                //Si el nodo adyacente no ha sido explorado y no es un obstaculo lo tratamos

                if (!childNode.getExplored() && !Objects.equals(childNode.getCellType(), "Obstacle")) {
                    double auxWeight = childNode.getWeight();//avoiding calls

                    //Si la distancia del nodo a origen es mayor que su peso más la distancia desde el padre,
                    //tomamos como distancia el nuevo valor. En el constructor de nodos la distancia inicial es infinito
                    if (childNode.getLength() > (auxWeight + activeNode.getLength())) {

                        debug.print("Ruta mejorada para " + childNode.getName(), 1);
                        debug.print("Distancia a origen de  " + childNode.getLength() + " es mayor que " + (auxWeight + activeNode.getLength()), 1);
                        debug.print("Nuevo nodo padre es : " + activeNode.getName(), 1);
                        //Establecemos la nueva distancia al origen desde el nodo adyacente
                        childNode.setLength(activeNode.getLength() + auxWeight);
                        //Establecemos la nueva ruta indicando quien es el padre del nodo adyacente
                        childNode.setFather(activeNode);
                        //Añadimos el nodo a la lista de candidatos
                        candidatepq.add(childNode);
                        if (childNode.getCellType().equals("Exit")) {

                            debug.print("He encontrado una salida!", 0);
                            debug.print("Tiempo en dijsktra:" + (System.currentTimeMillis() - t1) + "ms", 0);
                            hasSolution = true;
                            return this;
                        }

                    }
                }
            }
        }

        return this;
    }

    //Propósito:
    //Mostrar la salida recorriendo los nodos mediante su antecesor
    public List<String> showExit() {
        List<String> str = new ArrayList<>();
        LightNode allWayNode = MatrixNodeList.get(exitNode);
        String auxStr = "";
        //Si el algoritmo tiene solución la mostramos
        if (hasSolution) {
            while ((allWayNode = allWayNode.getFather()) != null) {
                //  debug.print( "Distancia de " + allWayNode.getName() + " al origen " + allWayNode.getLength(),0);

                str.add(allWayNode.getName());

                auxStr = allWayNode.getName() + "," + auxStr;
            }
            auxStr = auxStr + MatrixNodeList.get(exitNode).getName();
            debug.print(auxStr, 0);
            debug.print("Energía consumida: " + MatrixNodeList.get(exitNode).getLength(), 0);
        } else {
            debug.print("El problema no tiene solución", 0);
            System.out.println("El problema no tiene solución");
        }

        System.out.println("Fin del algoritmo");
        return str;
    }

    //Propósito: Establecer la traza a activa
    public void setTrace() {
        this.trace = true;
    }

    //Propósito: Establecer Fichero de salida
    public void setDebugFile(String debugFile) {
        debug.setDebugFilename(debugFile);
    }
}