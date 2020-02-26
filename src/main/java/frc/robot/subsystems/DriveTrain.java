/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import java.util.function.DoubleSupplier;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class DriveTrain extends SubsystemBase implements DoubleSupplier {
  /**
   * The DriveTrain subsystem incorporates the actuators attached to the robots
   * chassis.
   */
  // TalonSRX _leftMaster = new TalonSRX(15);
  // TalonSRX _rightMaster = new TalonSRX(1);
  CANSparkMax m_leftLead = new CANSparkMax(Constants.frontLeftMotor, MotorType.kBrushless);
  CANSparkMax m_leftFollower = new CANSparkMax(Constants.rearLeftMotor, MotorType.kBrushless);
  CANSparkMax m_rightLead = new CANSparkMax(Constants.frontRightMotor, MotorType.kBrushless);
  CANSparkMax m_rightFollower = new CANSparkMax(Constants.rearRightMotor, MotorType.kBrushless);

  private final DifferentialDrive m_drive = new DifferentialDrive(m_leftLead, m_rightLead);

  protected CANEncoder m_leftDriveEncoder;
  protected CANEncoder m_rightDriveEncoder;

  protected CANPIDController m_leftPIDController;
  protected CANPIDController m_rightPIDController;

  /**
   * Create a new drive train subsystem.
   */
  public DriveTrain() {
    m_leftFollower.restoreFactoryDefaults();
    m_leftLead.restoreFactoryDefaults();
    m_rightFollower.restoreFactoryDefaults();
    m_rightLead.restoreFactoryDefaults();

    m_leftLead.setIdleMode(IdleMode.kCoast);
    m_leftLead.setOpenLoopRampRate(Constants.driveMotorRampRate); // numbers = seconds until full speed

    m_rightLead.setIdleMode(IdleMode.kCoast);
    m_rightLead.setOpenLoopRampRate(Constants.driveMotorRampRate); // numbers = seconds until full speed

    m_leftFollower.setIdleMode(IdleMode.kCoast);
    m_leftFollower.setOpenLoopRampRate(Constants.driveMotorRampRate); // numbers = seconds until full speed

    m_rightFollower.setIdleMode(IdleMode.kCoast);
    m_rightFollower.setOpenLoopRampRate(Constants.driveMotorRampRate); // numbers = seconds until full speed

    m_leftLead.setClosedLoopRampRate(Constants.closedLoopRampRate);
    m_rightLead.setClosedLoopRampRate(Constants.closedLoopRampRate);

    m_leftFollower.follow(m_leftLead);
    m_rightFollower.follow(m_rightLead);

    m_leftDriveEncoder = m_leftLead.getEncoder();
    m_rightDriveEncoder = m_rightLead.getEncoder();

    m_leftPIDController = m_leftLead.getPIDController();
    m_rightPIDController = m_rightLead.getPIDController();

    m_leftPIDController.setP(Constants.kDriveGains.kP);
    m_leftPIDController.setI(Constants.kDriveGains.kI);
    m_leftPIDController.setD(Constants.kDriveGains.kD);
    m_leftPIDController.setFF(Constants.kDriveGains.kF);

    m_rightPIDController.setP(Constants.kDriveGains.kP);
    m_rightPIDController.setI(Constants.kDriveGains.kI);
    m_rightPIDController.setD(Constants.kDriveGains.kD);
    m_rightPIDController.setFF(Constants.kDriveGains.kF);

  }

  /**
   * Arcade drive method
   *
   * @param xSpeed    The robot's speed along the X axis [-1.0..1.0]. Forward is
   *                  positive.
   * @param zRotation The robot's rotation rate around the Z axis [-1.0..1.0].
   *                  Clockwise is positive.
   */
  public void drive(double xSpeed, double zRotation) {
    m_drive.arcadeDrive(xSpeed, zRotation);
  }

  /**
   * Tank drive at a PID-driven velocity method.
   *
   */
  public void driveAtSpeed(double leftSpeedInMetersPerSecond, double rightSpeedInMetersPerSecond) {

    /*
     * The Spark Maxs by default deal with speeds in RPM. So, convert the meters per
     * second that this routine accepts for each of the motor speeds into RPM before
     * telling each of the motors to spin at that speed.
     */

    double leftMotorRPM = meterPerSecondToRPM(leftSpeedInMetersPerSecond);
    m_leftPIDController.setReference(leftMotorRPM, ControlType.kVelocity);

    /*
     * By convention, the Right motor is inverted. So, invert this motor while under
     * direction control of this PID
     */
    double rightMotorRPM = meterPerSecondToRPM(-1.0 * rightSpeedInMetersPerSecond);
    m_rightPIDController.setReference(rightMotorRPM, ControlType.kVelocity);
  }

  protected double meterPerSecondToRPM(double metersPerSecond) {
    double RPM;

    try {
      double metersPerMinute = metersPerSecond * 60.0;
      double wheelRevolutionsPerMeter = 1.0 / (Constants.driveTireDiameterInMeters * Math.PI);

      RPM = metersPerMinute * wheelRevolutionsPerMeter;

    } catch (Exception ex) {
      RPM = 0.0;
    }

    return RPM;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  /*
   * Compute the distance the robot has travelled since its last reset
   *
   * @return distanceTravelled The cumulative distance the robot has travelled.
   * Reference Constants.driveTrainInchesPerEncoderCounts for units
   * 
   * Note that the motors and encoders on the right side of the robot are, by
   * convention, considered to be running backwards. Compensate for this by
   * subtracing this negative number. This way, you're subtracting a negative
   * value, which is adding a positive value!
   */
  @Override
  public double getAsDouble() {
    double averageNEORevolutionsTravelled = (m_leftDriveEncoder.getPosition() - m_rightDriveEncoder.getPosition())
        / 2.0;

    return (averageNEORevolutionsTravelled * Constants.driveTrainInchesPerEncoderCounts);
  }
}
