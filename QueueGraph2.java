/*
La clase QueueGraph define la matriz que vamos a crear donde se guardan los nodos y sus valores.
Debe ser el robot

@author Diego Javier Rios Sanchez
 * @version .1 20171014   creacion
 * @version 1.0 20171210
 *
 */

import java.util.*;

class QueueGraph2 {
    private static final double INFINITO = Double.MAX_VALUE;
    //Propósito: Tener una cola de prioridad ordenada para gestionar los candidatos según su cercanía al origen
   // private static final Comparator<LightNode> LengthComp = Comparator.comparingDouble(LightNode::getLength);
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
     */
    QueueGraph2(int height, int width, ArrayList<String> matrixValues) {

        this.width = width;
        this.height = height;

        MatrixNodeList = new ArrayList<>(height * width);

       /*Inicializamos la lista de nodos y su valor transformando la lista de cadenas recibida por parámetro en el constructor
       en valores de los elementos de la lista de nodos*/
        //convertToNode(matrixValues);

        debug = new Debug();
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


}