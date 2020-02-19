/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Shooter extends SubsystemBase implements Sendable {
  private CANSparkMax m_motor;
  private CANPIDController m_pidController;
  private CANEncoder m_encoder;
  private double m_shooterIntendedSpeed = 10000.0;
  protected YeetDetector m_yeetDetector= new YeetDetector(this::isAtSpeed);
    /**
   * Creates a new sparkMaxTest.
   */
  public Shooter() {

    // initialize motor
    m_motor = new CANSparkMax(Constants.shooterMotorControllerCANId, MotorType.kBrushless);

    /**
     * The RestoreFactoryDefaults method can be used to reset the configuration
     * parameters in the SPARK MAX to their factory default state. If no argument is
     * passed, these parameters will not persist between power cycles
     */
    m_motor.restoreFactoryDefaults();

    /*
     * Common interface for inverting direction of a speed controller.
     * 
     * Parameters: isInverted - The state of inversion, true is inverted.
     */
    m_motor.setInverted(true);

    /**
     * In order to use PID functionality for a controller, a CANPIDController object
     * is constructed by calling the getPIDController() method on an existing
     * CANSparkMax object
     */
    m_pidController = m_motor.getPIDController();

    // Encoder object created to display position values
    m_encoder = m_motor.getEncoder();

    // set PID coefficients
    m_pidController.setP(2.2e-4);
    m_pidController.setI(.00000005);
    m_pidController.setD(0.00001);
    m_pidController.setFF(1.5e-4);

    setClosedLoopSpeed(0.0);


  }

  public double getSpeed() {
    return m_encoder.getVelocity();
  }

  public void setClosedLoopSpeed(double RPM) {
    /**
     * PIDController objects are commanded to a set point using the SetReference()
     * method.
     * 
     * The first parameter is the value of the set point, whose units vary depending
     * on the control type set in the second parameter.
     * 
     * The second parameter is the control type can be set to one of four
     * parameters: com.revrobotics.ControlType.kDutyCycle
     * com.revrobotics.ControlType.kPosition com.revrobotics.ControlType.kVelocity
     * com.revrobotics.ControlType.kVoltage
     */
    m_shooterIntendedSpeed = RPM;

    if (m_shooterIntendedSpeed > 10.0) {
    m_pidController.setReference(RPM, ControlType.kVelocity);
    } else {
      m_motor.set(0.0);
    }

    m_yeetDetector.setNewSpeed(m_shooterIntendedSpeed);
  }

  public boolean isAtSpeed() {
    if (Math.abs(
        (getSpeed() - m_shooterIntendedSpeed) / m_shooterIntendedSpeed) <= Constants.shooterIntendedSpeedTolerance) {
      return true;
    } else {
      return false;
    }
  }

  public long getTotalYeets() {
    return m_yeetDetector.getTotalYeets();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    m_yeetDetector.detectYeets(getSpeed());
  }

  public void initSendable(SendableBuilder builder) {
    super.initSendable(builder);
    builder.setActuator(true);

    builder.addDoubleProperty("ClosedLoopSpeed(RPM)", this::getSpeed, this::setClosedLoopSpeed);
    builder.addDoubleProperty("CommandedSpeed(RPM)", () -> {
      return m_shooterIntendedSpeed;
    }, null);
    builder.addDoubleProperty("OpenLoopSpeed(normalized)", m_motor::get, m_motor::set);
    builder.addDoubleProperty("kP", m_pidController::getP, m_pidController::setP);
    builder.addDoubleProperty("kI", m_pidController::getI, m_pidController::setI);
    builder.addDoubleProperty("kD", m_pidController::getD, m_pidController::setD);
    builder.addDoubleProperty("kF", m_pidController::getFF, m_pidController::setFF);
    builder.addDoubleProperty("MotorOutputDrive", m_motor::getAppliedOutput, null);
    builder.addDoubleProperty("Detected Yeets",this::getTotalYeets,null);
  }

}
