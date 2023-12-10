import java.util.ArrayList;
import Jama.Matrix;


public class PetriNet 
{
    private Matrix incidence;
    private Matrix incidenceT;
    private Matrix currentMarking;
    private Matrix enableTrans;
    private Matrix pInvariants;
    
    



    public PetriNet(Matrix incidence, Matrix marking)
    {
        this.incidence = incidence;
        this.incidenceT = incidence.transpose();
        this.currentMarking = marking;
        this.enableTrans = new Matrix(incidence.getRowDimension(), 1);
        this.pInvariants = new Matrix(incidence.getRowDimension(), 1);
    }


    /*  ********************
        *  Public Methods  *
        ********************
     */
    /* Caculates state equation
     *  new marking = current marking + (incidence matrix * fire vector).
     */
    public Matrix stateEquation(Matrix v)
    {
        return currentMarking.plus(incidence.times(v));
    }
    //
    public boolean isTransitionEnabled(int transition)
    {
        return enableTrans.get(transition, 0) == 1;
    }
    //
    public boolean checkPInvariants()
    {
        return false;

    }
    
}
