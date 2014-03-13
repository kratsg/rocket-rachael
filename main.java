import java.util.*;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import java.util.ArrayList;

public class Main
{
  public static void main(String[] args)
  {
    Scanner indata = new Scanner(System.in);
    double rocketMass = 0., noseCone = .29; // kg
    String RM="", NT="";
    int numFins, numTrials;
    BufferedImage rocketCones;
    
    RM=JOptionPane.showInputDialog(null, "Enter the total mass of your rocket in grams:");
    rocketMass=Double.parseDouble(RM);
    
    NT=JOptionPane.showInputDialog(null, "Enter the number of trials you want to run:");
    numTrials=Integer.parseInt(NT);
      
    //send off to Formula class
    Formula mass = new Formula(rocketMass, numTrials, noseCone);
    System.out.println( mass.flightTime() );
    System.out.println( "x=" + mass.createXPoints() );
    System.out.println( "y=" + mass.createYPoints() );
  }
}

public class Formula
{
  // class variables
  double rocketMass, noseCone;
  int numTrials;

  //pre-set values
  static double gravity         = 9.801, // in m/s^2
                waterMass       = .355/1000., //in kg
                AirPressure     = 70., //in psi
                MassFlowRate    = 13.25/1000., // in kg/sec ??
                ExitVelocity    = 27.05, // in m/sec?
                Thrust          = 358., //in Newtons
                LaunchInitValue = 30., //in meters/second
                launchAngle     = 50.; // in degrees
  
  public Formula(double rocketMass, int numTrials, double noseCone)
  {
    rocketMass             = rocketMass;
    numTrials              = numTrials; 
    noseCone               = noseCone;
  }
  
  public ArrayList<Double> xForce()
  {
    double x;
    ArrayList<Double> xForces = new ArrayList<Double>();

    double dragForce = rocketMass; // why is it just rocket mass? that's not a force
    for(int q=1; q<=numTrials; q++)
    {
      x = Thrust * Math.cos( launchAngle ) - dragForce;
      dragForce += q; // same as dragForce = q + dragForce, looks better
      xForces.add(x);
    }
    return xForces; 
  }
   
  public double yForce()
  {
    double q       = Thrust * Math.sin( launchAngle ) - rocketMass * gravity;
    double z       = q * ( rocketMass + waterMass );
    double yForces = z * gravity;
    return yForces; 
  }
   
  public double netForces()
  {
    xForces = xForce();
    yForces = yForce();
    double yForcesSquare = yForces**2., xForcesSquare = 0.;
    for(int e=0; e<xForces.size(); e++)
    {
      xForcesSquare += xForces.get(e)**2.;// you probably meant to sum up all
    }
    netForce = Math.sqrt( xForcesSquare + yForcesSquare ) - noseCone; // noseCone?
    return netForce;
  }

  public double netAcceleration()
  {
    return netForces() / rocketMass;
  }
   
  public double WaterExpelledFlightTime()
  {
    return waterMass / MassFlowRate;
  }
 
  public double Range()
  {
    double z = (netAcceleration() * WaterExpelledFlightTime() )**2.;
    double range = ( 2. * Math.sin( launchAngle ) ) * z;
    range /= gravity; // same as range = range/gravity
    return range;
  }
 
  public double xComponentVel()
  {
    return LaunchInitValue * Math.cos( launchAngle );
  }

  public double yComponentVel()
  {
    return LaunchInitValue * Math.sin( launchAngle );
  }

  //total time calculation
  public double flightTime()
  {
    double range     = Range();
    double a         = netAcceleration();
    double tempTime  = LaunchInitValue / a;
    double finalTime = tempTime;
    // you don't do anything with range or a ???
    return finalTime;
  }
 
  //graphing methods
  public double vertexXPoint()
  {
    // return  (total_time / 2)* (velocity in x)
    return 0.5 * flightTime() * xComponentVel();
  }
  
  // this seems wrong
  public double vertexYPoint()
  {
    return -1. * ( (LaunchInitValue / 2. ) * flightTime() ) * 2;
  }

 
  public double GraphingFormulaCalculation()
  {
    double finalTime = flightTime(),
           X         = vertexXPoint(),
           Y         = vertexYPoint(),
           startX    = 0.0,
           startY    = 0.0;
    return ( (startY-Y) / (startX-X)**2. );
  }
   
  ArrayList<Double> createYPoints(){
    GraphingFormulaCalculation();
    //initialize yPoints
    ArrayList<Double> yPoints = new ArrayList<Double>();
    //tt = total time
    int tt=(int)flightTime();//this is typecasting 3 = (int)3.2
    double y = 0;
    for(int t=tt; t > 0; t--)
    {
      // y = vi_y * t - 1/2 g t^2
      y = yComponentVel() * t - ( 0.5* gravity * t**2. );
      yPoints.add(y);
    }
    return yPoints;
  }

  ArrayList<Double> createXPoints()
  {
    GraphingFormulaCalculation();
    //initialize xPoints
    ArrayList<Double> xPoints = new ArrayList<Double>();
    //tt = total time
    int tt=(int)flightTime();
    double x = 0;
    for(int t = tt; t > 0; t--)
    {
      // x = vi_x * t
      x = xComponentVel() * t;
      xPoints.add(xPosition);
    }
    return xPoints;
  }
     
  public Double endX()
  {
    // x = vi_x * total_time
    return xComponentVel() * flightTime();
  }

  //send off to graphics class
  Drawey obj=new Drawey(flightTime(), createYPoints(), createXPoints(), vertexXPoint(), vertexYPoint(), endX());
   
}
