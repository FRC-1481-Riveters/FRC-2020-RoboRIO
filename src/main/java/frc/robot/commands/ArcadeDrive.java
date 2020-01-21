package frc.robot.commands;

/*----------------------------------------------------------------------------*/

/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveTrain;
import frc.robot.RobotContainer.RumbleTimerJoystick;

/**
 * Have the robot drive tank style.
 */
public class ArcadeDrive extends CommandBase {
  private final DriveTrain m_drivetrain;
  RumbleTimerJoystick m_RumbleTimerJoystick;

  /**
   * Creates a new TankDrive command.
   *
   * @param left       The control input for the left side of the drive
   * @param right      The control input for the right sight of the drive
   * @param drivetrain The drivetrain subsystem to drive
   */

  public ArcadeDrive(DriveTrain drivetrain, RumbleTimerJoystick Joystick) {
    m_drivetrain = drivetrain;
    addRequirements(drivetrain);

    m_RumbleTimerJoystick = Joystick;
  }
  // Called repeatedly when this Command is scheduled to run
  @Override
  public void execute() {
    m_drivetrain.drive(m_RumbleTimerJoystick.getX(Hand.kRight), m_RumbleTimerJoystick.getY(Hand.kLeft));
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  public boolean isFinished() {
    return false; // Runs until interrupted
  }

  // Called once after isFinished returns true
  @Override
  public void end(boolean interrupted) {
    m_drivetrain.drive(0, 0);
  }
}