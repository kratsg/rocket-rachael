import java.util.*;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Main {
  public static void main(String[] args) {
    
    Scanner indata=new Scanner(System.in);
    double rocketMass;
    double noseCone=.29;
    String RM="";
    String NT="";
    int numFins;
    int numTrials;
    BufferedImage rocketCones;
    
    RM=JOptionPane.showInputDialog(null, "Enter the total mass of your rocket in grams:");
    rocketMass=Double.parseDouble(RM);
    
    NT=JOptionPane.showInputDialog(null, "Enter the number of trials you want to run:");
      numTrials=Integer.parseInt(NT);
      
    //send off to Formula class
    Formula mass=new Formula(rocketMass, numTrials, noseCone);
    System.out.println(mass.time());
    System.out.println("x="+mass.createXPoints());
    System.out.println("y="+mass.createYPoints());
  }
}

import java.util.ArrayList;
public class Formula {
  
  //not set values
  int numTrials;
  double dragForce, launchAngle, yForces, rocketMass, range, netForce, netAcceleration, finalTime;
  double rocketVelocity, noseCone, timeofExpulsion,  xComponentofVelocity;
  
  //ArrayList of forces in the x direction
  ArrayList<Double> xForces = new ArrayList<Double>();
  
  //pre-set values
  double forceOfGravityOnRocket=9.81*rocketMass; 
  static double waterMass=.355;//in g
  static double gravityAcceleration=9.8; //in m/s^2
  static int AirPressure=70;//in psi
  static double MassFlowRate=13.25;
  static double ExitVelocity= 27.05;
  static int Thrust=358; //in Newtons
  static int LaunchInitValue=30; //in meters/second
  
  //calculation of points variables
  double a, h, k;
  ArrayList<Double> yPoints = new ArrayList<Double>();
  ArrayList<Double> xPoints = new ArrayList<Double>();
  double y=1;
  
  public Formula(double x, int trials, double noseConeForce){
    rocketMass=x;
    numTrials=trials; 
    noseCone=noseConeForce;
  }
  
   ArrayList<Double> xForce(){
     dragForce=rocketMass;
    for (int q=1; q<=numTrials; q++)
    {   double x=Thrust*Math.cos(50)-dragForce;
      dragForce=q+dragForce;
      xForces.add(x);
    }
    return xForces; 
  }
   
   public double yForce(){
     double q=Thrust*Math.sin(50)-forceOfGravityOnRocket;
     double z=q*(rocketMass+waterMass);
     double yForces=z*gravityAcceleration;
     return yForces; 
     }
   
   public double netForces(){
     double xForcesSquare = 0;
     xForces=xForce();
     yForces=yForce();
     double yForcesSquare=(yForces*yForces);
     for(int e=0; e<xForces.size(); e++){
     xForcesSquare=(xForces.get(e)*xForces.get(e));
     }
     netForce=Math.sqrt(xForcesSquare+yForcesSquare)-noseCone;
     return netForce;
     }
  
   public double netAcceleration(){
     netForce=netForces();
     netAcceleration=netForce*rocketMass;
     return netAcceleration;
     }
     
   public double WaterExpelledTime(){
    timeofExpulsion=waterMass/MassFlowRate;
     return timeofExpulsion;
   }
   
   public double Range(){
     netAcceleration=netAcceleration();
     timeofExpulsion=WaterExpelledTime();
     double z=netAcceleration*timeofExpulsion*netAcceleration*timeofExpulsion;
     range=(2*Math.sin(50))*z;
     range=range/gravityAcceleration;
     return range;
   }
   
   public double xComponent(){
     double temp=Math.cos(50);
     xComponentofVelocity=(LaunchInitValue*temp);
     return xComponentofVelocity;
   }
   //final time calculation
   public double time(){
    Range();
    double a=netAcceleration();
    double tempTime=LaunchInitValue/a;
     tempTime*=1;
     
    finalTime=tempTime;
     return finalTime;
     }
   
   //graphing methods
   public double vertexXPoint(){
     double fTime=time();
     double q=fTime/2;
     double r=q*LaunchInitValue*Math.cos(50);
     double vertexX=r;
     return vertexX;
   }
   
   public double vertexYPoint(){
     double fTime=time();
     double vertexY=((LaunchInitValue/2)*fTime)*2;
     return vertexY*-1;
   }

   
   public double GraphingFormulaCalculation(){
     finalTime=time();
      double Y=vertexYPoint();
      double X=vertexXPoint();

     double startX=0*0*Math.cos(50);
     double startY=0*0*Math.sin(50);
     
     a=(startY-Y)/((startX-X)*(startX-X));
     return a;
   }
   
   ArrayList<Double> createYPoints(){
     GraphingFormulaCalculation();
     int t=(int)time();
    for(int x=t; x>=1; x--){
      y=LaunchInitValue*x*Math.sin(50)-(.5*9.8*(x*x));
      yPoints.add(y);
    }
     return yPoints;
     }
   ArrayList<Double> createXPoints(){
     GraphingFormulaCalculation();
     int t=(int)time();
     for(int x=t; x>=1; x--){
      double xPosition=LaunchInitValue*x*Math.cos(50);
      xPoints.add(xPosition);
     }
     return xPoints;
   }
     
   public Double endX(){
     double t=time();
     double endx=LaunchInitValue*t*Math.cos(50);
     return endx;
   }
  //send off to graphics class
      Drawey obj=new Drawey(time(), createYPoints(), createXPoints(), vertexXPoint(), vertexYPoint(), endX());
   
}
