/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;

import java.util.HashMap;
import java.util.Map;

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

  private final Color kBlueTarget = ColorMatch.makeColor(.18, 0.44, 0.36);
  private final Color kGreenTarget = ColorMatch.makeColor(0.2, 0.53, 0.25);
  private final Color kRedTarget = ColorMatch.makeColor(0.43, 0.39, 0.18);
  private final Color kYellowTarget = ColorMatch.makeColor(0.31, 0.53, 0.15);

  public colorsensor() {

    colorMap.put(kBlueTarget, "Blue");
    m_colorMatcher.addColorMatch(kBlueTarget);
    colorMap.put(kGreenTarget, "Green");
    m_colorMatcher.addColorMatch(kGreenTarget);
    colorMap.put(kRedTarget, "Red");
    m_colorMatcher.addColorMatch(kRedTarget);
    colorMap.put(kYellowTarget, "Yellow");
    m_colorMatcher.addColorMatch(kYellowTarget);

    SmartDashboard.putNumber("Confidence", 0.5);
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

    /**
     * The sensor returns a raw IR value of the infrared light detected.
     */
    double IR = m_colorSensor.getIR();

    /**
     * Open Smart Dashboard or Shuffleboard to see the color detected by the sensor.
     */
    SmartDashboard.putNumber("Red", detectedColor.red);
    SmartDashboard.putNumber("Green", detectedColor.green);
    SmartDashboard.putNumber("Blue", detectedColor.blue);

    ColorMatchResult colorMatchResult = m_colorMatcher.matchClosestColor(detectedColor);

    SmartDashboard.putString("Color guess", colorMap.get(colorMatchResult.color));
    SmartDashboard.putNumber("Color guess confidence", colorMatchResult.confidence);

    SmartDashboard.putNumber("IR", IR);

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
}
