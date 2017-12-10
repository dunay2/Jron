/*
  Proposito: Leer y escribir archivos

  @author (Diego Javier Rios Sanchez)
 * @version (0.91 20171028)
 */

import java.io.*;
import java.util.ArrayList;

//Constructor

class HandleFile
{
    private final String inFile;// Fichero de entrada
    private final String outFile;// Fichero de salida
    private ArrayList<String> textMatrix; //Lista con los datos del archivo de entrada

    /**
     * Constructor de objetos de la clase HandleFile
     * Parámetros de entrada:
     *  in_file --> Fichero de entrada. Tipo String 
     *  out_file  --> Fichero de salida. Tipo String 
     */

    public HandleFile(String in_file,String out_file)
    {
        File fic;
        // initialice instance variables
        this.inFile=in_file;
        this.outFile=out_file;
        fic = new File(out_file);
        fic.delete();
    }

    //Propósito: Leer un archivo línea a línea y agregar cada línea al arraylist textMatrix 
    public Exception readFile() throws  IOException
    {
        String line;//variable que contiene la línea que contiene la línea leída

        LineNumberReader lineNumberReader; // variable de tipo lineNumberReader por si queremos ir a líneas determinadas
        textMatrix = new ArrayList<>(); // inicializamos texMatrix
        lineNumberReader=  new LineNumberReader(new FileReader(inFile)); // inicializamos lineNumberReader pasando el fichero de entrada

        //Leemos hasta final de archivo
        try {
            while ((line= lineNumberReader.readLine()) != null)
            {
                textMatrix.add (line);
            }
        }
        catch(Exception e){
            return e;            // Devolvemos una excepción
        }

            lineNumberReader.close();//Fin de lectura y cierre de fichero
            return null;

    }

    /**
     * Propósito: devolver en un arraylist de string las líneas de un fichero leído
     *
     * param  file_io_path  valor String que establece el path del fichero de entrada
     * en el atributo privado _out_file
     * return     ArrayList<String>
     */

    public  ArrayList<String> getMatrix()
    {
        return  textMatrix;
    }

    public void writeLine(String text ){
        PrintWriter  out=null;
        try{

               out = new PrintWriter(new BufferedWriter(new FileWriter(outFile, true)));
             out.println(text);
             }
     catch (IOException e) {
    System.out.println("FILE NOT FOUND EXCEPTION!");
    e.printStackTrace();
}
    finally{
    if(out != null){
        out.close();
    }
}
}
}