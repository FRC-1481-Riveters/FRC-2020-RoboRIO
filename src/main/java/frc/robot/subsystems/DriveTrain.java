/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class DriveTrain extends SubsystemBase {
  /**
   * The DriveTrain subsystem incorporates the  actuators attached to the robots chassis.
   */
 // TalonSRX _leftMaster = new TalonSRX(15);
  //TalonSRX _rightMaster = new TalonSRX(1);
  CANSparkMax m_leftLead = new CANSparkMax(Constants.frontLeftMotor, MotorType.kBrushless );
	CANSparkMax m_leftFollower = new CANSparkMax(Constants.rearLeftMotor, MotorType.kBrushless);
  CANSparkMax m_rightLead = new CANSparkMax(Constants.frontRightMotor, MotorType.kBrushless);
  CANSparkMax m_rightFollower = new CANSparkMax(Constants.rearRightMotor, MotorType.kBrushless);
  

  private final DifferentialDrive m_drive = new DifferentialDrive(m_leftLead, m_rightLead);

 
  /**
   * Create a new drive train subsystem.
   */
  public DriveTrain() {
    m_leftFollower.restoreFactoryDefaults();
    m_leftLead.restoreFactoryDefaults();
    m_rightFollower.restoreFactoryDefaults();
    m_rightLead.restoreFactoryDefaults();

    m_leftLead.setClosedLoopRampRate(Constants.closedLoopRampRate);
    m_rightLead.setClosedLoopRampRate(Constants.closedLoopRampRate);

    

    m_leftFollower.follow(m_leftLead);
    m_rightFollower.follow(m_rightLead);


    }

  /**
   * Arcade style driving for the DriveTrain.
   *
   * @param left  Speed in range [-1,1]
   * @param right Speed in range [-1,1]
   */
  public void drive(double left, double right) {
    m_drive.arcadeDrive(left, -right);
  }

  @Override
  public void periodic() {
  // This method will be called once per scheduler run
  }
}
