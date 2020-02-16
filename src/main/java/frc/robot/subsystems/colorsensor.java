/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;

import java.util.HashMap;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;

import com.revrobotics.ColorSensorV3;
import frc.robot.Constants;

public class colorsensor extends SubsystemBase {
  /**
   * Creates a new colorsensor.
   */

  private final ColorSensorV3 m_colorSensor = new ColorSensorV3(Constants.i2cPortColorSensor);

  private final ColorMatch m_colorMatcher = new ColorMatch();
  private final HashMap<Color, String> colorMap = new HashMap<Color, String>();

  private final Color kBlueTarget = ColorMatch.makeColor(0.18, 0.44, 0.36);
  private final Color kGreenTarget = ColorMatch.makeColor(0.2, 0.53, 0.25);
  private final Color kRedTarget = ColorMatch.makeColor(0.43, 0.39, 0.18);
  private final Color kYellowTarget = ColorMatch.makeColor(0.31, 0.53, 0.15);

  public colorsensor() {

    /*
     * Create a hashmap of Colors and a string that describes them. This map is just
     * a thing that connects a Color to a string (a name). It's only for humans to
     * use, since the computer *already* knows what "color" it's found in a Color
     * variable (the return value from matchClosestColor). This is just for the
     * humans to connect an object like kBlueTarget to a string that can be printed
     * on e.g. SmartDashboard as "Blue". It's not necessary for the RoboRIO to do
     * its job in finding a color match.
     */
    colorMap.put(kBlueTarget, "B");
    colorMap.put(kGreenTarget, "G");
    colorMap.put(kRedTarget, "R");
    colorMap.put(kYellowTarget, "Y");

    /*
     * Setup the color matches. The matchClosestColor() function will ONLY look at
     * colors that have been "registered" (stored) with the function
     * addColorMatch(). This also means that matchClosestColor() will only return
     * color matches that registered; it doesn't know about any colors that you
     * don't tell it about. This line of code gets each color we know about (like
     * kBlueTarget) that was put() (the function put() stores a color and its string
     * name into a map, like colorMap) and adds it to the colorMatcher matching
     * array so matchClosestColor can match it. You can't see colorMatcher's color
     * matching array; it's a private variable that colorMatcher keeps to itself,
     * but you can add new colors to it using the addColorMatch() function.
     * 
     * If you don't add a given color with addColorMatch() then matchClosestColor()
     * WILL NEVER MATCH THAT MISSING COLOR!
     */

    colorMap.forEach((key, value) -> m_colorMatcher.addColorMatch(key));


  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    /**
     * The method GetColor() returns a normalized color value from the sensor and
     * can be useful if outputting the color to an RGB LED or similar. To read the
     * raw color, use GetRawColor().
     * 
     * The color sensor works best when within a few inches from an object in well
     * lit conditions (the built in LED is a big help here!). The farther an object
     * is the more light from the surroundings will bleed into the measurements and
     * make it difficult to accurately determine its color.
     */
    Color detectedColor = m_colorSensor.getColor();

    /*
     * Make the m_colorMatcher find the closest color that matches the numbers in
     * detectedColor. Since the numbers won't be *exactly* the same as the pure
     * color, matchClosestColor looks at each of the colors that were "registered"
     * (stored) with addColorMatch() and finds the one that matches the best. It
     * then puts that closest match into colorMatchResult, along with a "confidence"
     * value that represents how sure the software is that this is the a good match.
     * The higher the confidence (which is between 0.0 and 1.0), the better the
     * confidence. Think of it like a percent match. 100% is a pretty good match,
     * but 50% not so much.
     */
    ColorMatchResult colorMatchResult = m_colorMatcher.matchClosestColor(detectedColor);

    /*
     * Use the hashmap colorMap to "get" (lookup) the string associated with this
     * colorMatchResult and display it so humans can see which color the RoboRIO
     * detected with the color sensor.
     * 
     * Also, put the confidence up there so the humans know how "good" of a match
     * the color sensor found. Correct matches (like "red is red") with low
     * confidence numbers means the RGB numbers (coordinates) used to describe
     * kRedTarget need some work.
     */

    try {
      SmartDashboard.putString("Color guess", colorMap.get(colorMatchResult.color));
    } catch (Exception ex) {
      SmartDashboard.putString("Color guess", "Unknown");
    }

    SmartDashboard.putNumber("Color guess confidence", colorMatchResult.confidence);

    /**
     * In addition to RGB IR values, the color sensor can also return an infrared
     * proximity value. The chip contains an IR led which will emit IR pulses and
     * measure the intensity of the return. When an object is close the value of the
     * proximity will be large (max 2047 with default settings) and will approach
     * zero when the object is far away.
     * 
     * Proximity can be used to roughly approximate the distance of an object or
     * provide a threshold for when an object is close enough to provide accurate
     * color values.
     */
    int proximity = m_colorSensor.getProximity();

    SmartDashboard.putNumber("Proximity", proximity);

  }
  public String getOurColorSensor() {
    Color detectedColor = m_colorSensor.getColor();
    ColorMatchResult colorMatchResult = m_colorMatcher.matchClosestColor(detectedColor);

    try {
      return(colorMap.get(colorMatchResult.color));
    } catch (Exception ex) {
      return("Unknown");
    }
  }
}
