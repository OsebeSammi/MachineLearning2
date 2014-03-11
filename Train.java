package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.ejml.simple.SimpleMatrix;

import javax.swing.*;
import java.io.File;
import java.io.*;
import java.util.ArrayList;
import java.lang.NumberFormatException;

/**
 * Created with IntelliJ IDEA.
 * User: sammi
 * Date: 2/27/14
 * Time: 11:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class Train
{
    BufferedReader read;
    String line;
    String[] content;
    boolean useError = false;
    double useErrorValue;
    int myBreak = 1000;
    //matrices needed
    SimpleMatrix neurons,normalisedNeuron,inputOfHidden,outputOfHidden,inputOfOutput,tempMatrix,tempMatrix2,output,input,matrixD;
    double outputOfOutput;
    SimpleMatrix weightAfterInput,weightAfterHidden;


    int numberOfHiddenNeurons;

    int countColumn=0;
    int countRow=0;
    int maxColumns=0,maxRows=0;

    double divisor = 1;
    double error1 = 0, error2 = 0;
    double d = 0;

    ArrayList <String> title = new ArrayList<String>();


    Boolean readTitle = false;
    Boolean matrix = false;

    public Train(File file)
    {
        try
        {
            read = new BufferedReader(new FileReader(file));

            //determining number of columns and rows
            while((line = read.readLine())!=null)
            {

                content = line.split(" ");


                if(!matrix)
                {
                    maxColumns = content.length;
                    matrix = true;
                }

                    maxRows++;
            }



            neurons = new SimpleMatrix(maxRows,maxColumns+1);

            //again
            read = new BufferedReader(new FileReader(file));

            while((line = read.readLine())!=null)
            {

                content = line.split(" ");

                    //adding training data to the matrix
                    for(countColumn=0;countColumn<content.length;countColumn++)
                    {
                        //filling a row with data

                        neurons.set(countRow,countColumn,Double.parseDouble(content[countColumn]));

                        //System.out.println(Double.parseDouble(content[countColumn]));
                    }
                    countRow++;
            }

            read.close();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        catch (NumberFormatException nfe)
        {
            nfe.printStackTrace();
        }


        System.out.print("csv read successfully,wait for the summary \n\n");


    }

    public void normalise()
    {
        double temp;

        while(true)
        {
            temp = neurons.elementMaxAbs()/divisor;
            if(temp<1)
            {
                break;
            }
            else
            {
                divisor*=10;
            }
        }

        normalisedNeuron = new SimpleMatrix(neurons);
    }




    public void backPropagation()
    {
        //normalise
        normalise();
        //System.out.println(neurons.elementMaxAbs());
        //System.out.println(normalisedNeuron.elementMaxAbs());

        numberOfHiddenNeurons = (int) ((maxColumns)*0.67);


        //System.out.println(maxColumns);
        //System.out.println(numberOfHiddenNeurons);


        //normalisedNeuron.print();
        input = normalisedNeuron.extractMatrix(0,maxRows,0,maxColumns-1);
        output = normalisedNeuron.extractMatrix(0,maxRows,maxColumns-1,maxColumns);

        //input.print();
        //output.print();


        maxColumns = input.numCols();
        maxRows =  input.numRows();


        //setting the difference in errors
        error1 = error2;

        //creating weight matrices
        weightAfterInput = new SimpleMatrix(maxColumns,numberOfHiddenNeurons);
        weightAfterHidden = new SimpleMatrix(numberOfHiddenNeurons,1);



        for(int y=0;y<maxColumns;y++)
        {
            for(int x=0;x<numberOfHiddenNeurons;x++)
            {
                weightAfterInput.set(y,x, 1);
            }

        }




        for(int y=0;y<numberOfHiddenNeurons;y++)
        {
            weightAfterHidden.set(y,0, 1);
        }



        //looping through the input
        for(int row=0;row<maxRows;row++)
        {
            tempMatrix = new SimpleMatrix(input.extractMatrix(row,row+1,0,maxColumns));//here it holds input at row i



            //step 5 Getting input of Hidden layer


            //input.extractMatrix(0, 1, 0, maxColumns).transpose().print();
            inputOfHidden = new SimpleMatrix(weightAfterInput.transpose().mult(tempMatrix.transpose()));



            //step 6 output of hidden
            outputOfHidden = new SimpleMatrix(inputOfHidden.numRows(),inputOfHidden.numCols());



            for(int y=0;y<inputOfHidden.numRows();y++)
            {
                for(int x=0;x<inputOfHidden.numCols();x++)
                {
                    outputOfHidden.set(y,x,(1/(1+Math.exp(inputOfHidden.get(y,x)))));
                }
            }


            //step 7

            //weightAfterHidden.print();
            //outputOfHidden.print();
            inputOfOutput = weightAfterHidden.transpose().mult(outputOfHidden);


            //inputOfOutput.print();
            //step 8
            outputOfOutput = 1/(1+Math.exp(inputOfOutput.get(0,0)));

            //step 9
            error2 = output.get(row,0) - outputOfOutput;

            //if(error1>1)
            {
                error2 = Math.pow(error2,2);
            }




            //step 10
            d = Math.sqrt(error2)*outputOfOutput*(1 - outputOfOutput);

            //step 11
            tempMatrix = new SimpleMatrix(outputOfHidden); //here it holds output of hidden that will be multiplied by learning rate and difference in weights

            for (int y=0;y<tempMatrix.numRows();y++)
            {
                tempMatrix.set(y,0,(tempMatrix.get(y,0)*d*0.6));//learning rate is 0.6
            }



            //step 12
            tempMatrix = new SimpleMatrix(weightAfterHidden.plus(tempMatrix));//here it holds the new hidden weight
            //tempMatrix.print();

            //step 13
            tempMatrix2 = new SimpleMatrix(weightAfterHidden);
            for (int y=0;y<tempMatrix2.numRows();y++)
            {
                tempMatrix2.set(y,0,(tempMatrix2.get(y,0)*d));//learning rate is 0.6
            }



            matrixD = new SimpleMatrix(tempMatrix2);
            for (int y=0;y<matrixD.numRows();y++)
            {
                matrixD.set(y,0,(tempMatrix2.get(y,0)*outputOfHidden.get(y,0)*tempMatrix.get(y,0)));//learning rate is 0.6
            }



            //input multiply by matrixD

            tempMatrix2 = new SimpleMatrix(input.extractMatrix(row,row+1,0,maxColumns).transpose().mult(matrixD.transpose()));//has the value of [X] from formula

            for (int y=0;y<tempMatrix2.numRows();y++)
            {
                tempMatrix2.set(y,0,(tempMatrix2.get(y,0)*0.6));//learning rate is 0.6
            }



            //tempMatrix2.print();
            //weightAfterInput.print();

            tempMatrix2 = weightAfterInput.plus(tempMatrix2); //new weight after input

            weightAfterInput = tempMatrix2;
            weightAfterHidden = tempMatrix;



            //System.out.println("Error "+error2);
            //System.out.println("Output "+outputOfOutput);
            //weightAfterInput.print();
            //

            //System.out.println("Weight Matrices");
            //weightAfterInput.print();
            //weightAfterHidden.print();
            //System.out.println("Error difference"+(error2 - error1));
            System.out.println("Out put"+output.get(row,0)+ " Out put of training"+outputOfOutput);

            if(error2 == error1)
            {
                System.out.println("Constant error");

                myBreak--;
                if(myBreak<5)
                {
                    useError = true;
                    useErrorValue = error2;
                    JOptionPane.showMessageDialog(null,"Trained successfully");
                    break;
                }

            }
            else if((error2 - error1) < 0.00000001)
            {
                System.out.println("Change in error is approaching zero");

                myBreak--;
                if(myBreak<5)
                {
                    useError = true;
                    useErrorValue = error2;
                    JOptionPane.showMessageDialog(null,"Trained successfully");
                    break;
                }

            }

            else if(error2<error1)
            {
                System.out.println("Continue "+error2 + " " +error1);
                                continue;
            }
            if(row+2 > maxRows)
            {
                row = 0;
            }

        }
    }

    public double work(SimpleMatrix inputFromUser)
    {
        inputOfHidden = weightAfterInput.transpose().mult(inputFromUser);

        inputOfOutput = weightAfterHidden.transpose().mult(outputOfHidden);

        outputOfOutput = 1/(1+Math.exp(inputOfOutput.get(0,0)));

        if(useError)
        {
            outputOfOutput -= useErrorValue;
        }
        if(divisor!=1)
        {
            outputOfOutput *= divisor;
        }

        return outputOfOutput;
    }
}
