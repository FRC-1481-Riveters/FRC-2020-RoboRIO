/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

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
   * Creates a new ExampleCommand.
   *
   * @param drivetrain The subsystem used by this command.
   * @param Joystick The joystick used by this command.
   */
  public ArcadeDrive(DriveTrain drivetrain, RumbleTimerJoystick Joystick) {
    m_drivetrain = drivetrain;
    addRequirements(drivetrain);

    m_RumbleTimerJoystick = Joystick;
  }
  // Called every time the scheduler runs while the command is scheduled.  
  @Override
  public void execute() {
    m_drivetrain.drive(m_RumbleTimerJoystick.getY(Hand.kLeft), m_RumbleTimerJoystick.getX(Hand.kRight));
  }

 // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_drivetrain.drive(0, 0);
  }
  
  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
