/**
 * Define la clase nodo y sus características
 *
 * @author (Diego Ríos Sánchez)
 * @version (0.2 20171027)
 */

public class LightNode
{
    private static final double INFINITO=Double.MAX_VALUE;
    private final double weight; // peso
    private final int index; // peso
    private final String name;
    private final String cellType;
    // instance variables
    private boolean explored;
    private LightNode father;
    private double length; // la distancia hasta el origen

    /**
     * Constructor for objects of class node
     */

    public LightNode(int index,double weight, String name, String cellType )
    {
        // initialise instance variables
        this.weight=weight;
        this.explored= false;
        this.name= name;
        this.length= INFINITO;
        this.cellType=cellType;
        this.index=index;

    }
    public int getIndex()
    {
        return index;
    }


    public void setExplored()
    {
        explored= true;
    }

    public boolean getExplored()
    {
        return   explored;
    }

    //Propósito: devolver el peso del nodo
    public double getLength()
    {
        return  length ;
    }

    //Propósito: establecer el peso del nodo
    public void setLength(double length)
    {
        this.length=length;
    }

    //Propósito: devolver el peso del nodo
    public double getWeight()
    {
        return  weight ;
    }

    //Propósito: devolver el peso del nodo
    public LightNode getFather()
    {
        return  father ;
    }

    public void setFather(LightNode father)
    {
        this.father=father;
    }

    public String getName()
    {
        return  name;
    }
    //Devuelve si es robot, obstáculo o salida
    public String getCellType()
    {
        return  cellType;
    }
}
