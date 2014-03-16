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
    double rocketMass; // kg
    String RM="";
    BufferedImage rocketCones;
    
    RM=JOptionPane.showInputDialog(null, "Enter the total mass of your rocket in grams:");
    rocketMass=Double.parseDouble(RM);
      
    //send off to Formula class
    Formula rocket = new Formula(rocketMass);
    System.out.println( mass.flightTime() );
    System.out.println( "x=" + rocket.createXPoints() );
    System.out.println( "y=" + rocket.createYPoints() );
  }
}

public class Formula
{
  // class variables
  double rocketMass;

  //pre-set values
  static double gravity         = 9.801, // in m/s^2
                waterMass       = 0.355., //in kg
                AirPressure     = 70., //in psi
                MassFlowRate    = 13.25., // in kg/sec ??
                ExitVelocity    = 27.05, // in m/sec?
                Thrust          = MassFlowRate * ExitVelocity, //in Newtons
                LaunchInitValue = 30., //in meters/second
                launchAngle     = 50.; // in degrees
  
  public Formula(double rocketMass_in)
  {
    rocketMass             = rocketMass_in;
    System.out.println("Inputs received");
    System.out.println("\t rocket mass: " + rocketMass + " kg");
  }
  
  // F_x = F_thrust cos(theta) - F_drag
  //    assume F_drag ~ 0
  public double xForce()
  {
    return Thrust*Math.cos( launchAngle );
  }
   
  // F_y = F_thrust sin(theta) - F_gravity
  public double yForce()
  {
    return Thrust * Math.sin(launchAngle) - rocketMass * gravity;
  }
  
  // vector addition here
  // F_net = \vec{F}_x + \vec{F}_y = \sqrt{F_x^2 + F_y^2}
  public double netForce()
  {
    return Math.pow( Math.pow(xForce(),2.) + Math.pow(yForce(),2.), 0.5);
  }

  // a = F_net / average mass of empty rocket bottle and water
  public double netAcceleration()
  {
    return netForce() / (( rocketMass + waterMass )/2.);
  }
   
  public double WaterExpelledFlightTime()
  {
    return waterMass / MassFlowRate;
  }

  // V_rocket = net acceleration * time when all water is expelled
  public double VRocket(){
    return netAcceleration() * WaterExpelledFlightTime();
  }
 
  // R = V_rocket^2 * sin(2*angle) * (1/g)
  public double Range()
  {
    return ( ( Math.pow(VRocket(),2.) * Math.sin(2*launchAngle) )/gravity );
  }
 
  // V_x = V * cos(angle)
  public double VRocketX()
  {
    return VRocket() * Math.cos( launchAngle );
  }

  // V_y = V * sin(angle)
  public double VRocketY()
  {
    return VRocket() * Math.sin( launchAngle );
  }

  //total time calculation
  // t_f = R/V_rocket_x + 2 * water expelled time
  public double flightTime()
  {
    return Range() / VRocketX() + 2*WaterExpelledFlightTime();
  }
 
  //graphing methods
  public double vertexXPoint()
  {
    // return  (total_time / 2)* (velocity in x)
    return 0.5 * flightTime() * VRocketX();
  }
  
  // this seems wrong -- not in paper, needs to be fixed???
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
    return ( (startY-Y) / Math.pow(startX-X, 2.) );
  }
   
  public ArrayList<Double> createYPoints(){
    GraphingFormulaCalculation();
    //initialize yPoints
    ArrayList<Double> yPoints = new ArrayList<Double>();
    //tt = total time
    int tt=(int)flightTime();//this is typecasting 3 = (int)3.2
    double y = 0;
    for(int t=tt; t > 0; t--)
    {
      // y = vi_y * t - 1/2 g t^2
      y = VRocketY() * t - ( 0.5* gravity * Math.pow(t, 2.) );
      //y = LaunchInitValue * Math.sin(launchAngle) * t - ( 0.5 * gravity * t**2. )
      yPoints.add(y);
    }
    return yPoints;
  }

  public ArrayList<Double> createXPoints()
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
      x = VRocketX() * t;
      //x = LaunchInitVal * Math.cos( launchAngle ) * t
      xPoints.add(x);
    }
    return xPoints;
  }
     
  public Double endX()
  {
    // x = vi_x * total_time
    return VRocketX() * flightTime();
  }

  //send off to graphics class
  Drawey obj=new Drawey(flightTime(), createYPoints(), createXPoints(), vertexXPoint(), vertexYPoint(), endX());
   
}
