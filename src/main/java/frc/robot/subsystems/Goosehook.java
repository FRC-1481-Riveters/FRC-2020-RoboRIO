/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Goosehook extends SubsystemBase {
  private static WPI_TalonSRX m_goosehook = new WPI_TalonSRX
  (Constants.untitledGooseMotorCANId);
  /**
   * Creates a new Goosehook.
   */
  public Goosehook() {

  }
    public static void GoosehookSetMotorSpeed(double goosehookMotorSpeed) {
      m_goosehook.set(ControlMode.PercentOutput, goosehookMotorSpeed);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}