/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Elevator extends SubsystemBase {
  private static WPI_TalonSRX m_elevator = new WPI_TalonSRX
  (Constants.winchMotorElevatorCANId);
  private static WPI_TalonSRX m_elevator2 = new WPI_TalonSRX
  (Constants.winchMotorElevator2CANId);
  public int m_currentElevatorPosition;
  /**
   * Creates a new Elevator.
   */
  
  public Elevator() {
    SmartDashboard.putData(m_elevator);
    m_elevator.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder,
        Constants.kPIDLoopIdx, Constants.kTimeoutMs);

        m_elevator.config_kF(0, 0.0);
        m_elevator.config_kP(0, 0.1);
        m_elevator.config_kI(0, 0.0);
        m_elevator.config_kD(0, 0.0);
            /* Ensure sensor is positive when output is positive */
        m_elevator.setSensorPhase(Constants.kElevatorSensorPhase);

        m_elevator.configNominalOutputForward(0, Constants.kTimeoutMs);
        m_elevator.configNominalOutputReverse(0, Constants.kTimeoutMs);
        m_elevator.configPeakOutputForward(1, Constants.kTimeoutMs);
        m_elevator.configPeakOutputReverse(-1, Constants.kTimeoutMs);

       // m_elevator2.setInverted(true);
        m_elevator2.follow(m_elevator);
  }
  //public void zeroElevatorPosition() {
  //  m_elevator.getSensorCollection().setQuadraturePosition(0, Constants.kTimeoutMs);
  //}
  public void climbToPosition(int encoderCount) {
    m_elevator.set(ControlMode.Position, encoderCount);
  }
  public void stopElevator() {
    m_elevator.set(ControlMode.PercentOutput, 0);
  }
  public int getActualPosition() {
    /*
     * This value is converted with a negative sign to switch the way the sensor
     * reports rotation. It's backwards compared to how the code expects the sensor
     * to rotate.
     */
    return m_elevator.getSelectedSensorPosition();
  }
  @Override
  public void periodic() {
    m_currentElevatorPosition = getActualPosition();
    SmartDashboard.putNumber("Current Elevator Position",m_currentElevatorPosition);
  }
}
