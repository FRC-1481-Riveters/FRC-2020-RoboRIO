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

public class Elevator extends SubsystemBase {
  private static WPI_TalonSRX m_elevator = new WPI_TalonSRX
  (Constants.winchMotorElevatorCANId);
  /**
   * Creates a new Elevator.
   */
  
  public Elevator() {

  }
  public void zeroElevatorPosition() {
    m_elevator.getSensorCollection().setQuadraturePosition(0, Constants.kTimeoutMs);
  }
  public void climbToPosition(int encoderCount) {
    m_elevator.set(ControlMode.Position, encoderCount);
  }
    
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
