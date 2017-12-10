import java.util.Objects;

/**
 ** Propósito: establecer el modo debug para el proyecto
 *
 * @author Diego Javier Ríos Sanchez
 * @version (0.9 20171027)
 */

class Debug
{
    // instance variables -

    /**
     * Constructor for objects of class debug
     */

    private String _switch;
    private HandleFile handleFile;
    private String fileName;//indica el nombre de fichero de output. si está vacío escribe en terminal
    private boolean trace=false;


public void setDebugFilename(String fileName )
    {

        this.fileName= fileName;
    }
    public Debug()
    {
        // initialise instance variables
        _switch="off";

    }

    public void start()
    {
        // initialise instance variables
        _switch="on";
        handleFile= new HandleFile("",fileName);

    }

    /**
     * Propósito: imprimir en pantalla o en fichero
     * Parámetros: text,String --> texto a imprimir;   debugLevel,int
     * indica la prioridad 0 imprime siempre; 1 imprime si hay traza activada
//
     */

    private void _print(String text)
    {
        if (Objects.equals(fileName, ""))
        {
            System.out.println (text);
        }
        else
        {

            handleFile.writeLine(text);
        }

    }
    public void print(String  text, int debugLevel)
    {
        //solo imprimimos si el debug está activo
        if (_switch.equals("on"))
        {   //mensajes que se imprimen siempre

            //mensajes que solo se activan si son obligatorios
            // o son mensajes de traza y la traza esta activa

            if  ( debugLevel==0 || debugLevel ==1 && trace)
            {
                _print(text);
            }
        }

    }
    public void setTrace( ) {
        this.trace = true;
    }

}