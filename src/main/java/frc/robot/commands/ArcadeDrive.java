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
import frc.robot.Constants;
import frc.robot.RumbleTimerJoystick;
import frc.robot.commands.JoystickDeadband;

/**
 * Have the robot drive tank style.
 */
public class ArcadeDrive extends CommandBase {
  private final DriveTrain m_drivetrain;
  RumbleTimerJoystick m_RumbleTimerJoystick;
  final JoystickDeadband m_deadbander = new JoystickDeadband();
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
    final double m_leftJoystickValue;
    final double m_rightJoystickValue;
    final double m_leftJoystickSquare;
    final double m_rightJoystickQuarter;
    
  
  //  final double m_rightJoystickSquare;
    m_leftJoystickValue = m_RumbleTimerJoystick.getY(Hand.kLeft);
    m_rightJoystickValue = -m_RumbleTimerJoystick.getX(Hand.kRight);
    if (m_leftJoystickValue >= 0){
    m_leftJoystickSquare = m_leftJoystickValue * m_leftJoystickValue; }
    else {
    m_leftJoystickSquare = -(m_leftJoystickValue * m_leftJoystickValue); 
  }
  /*  if (m_rightJoystickValue >= 0){
    m_rightJoystickSquare = m_rightJoystickValue * m_rightJoystickValue;}
    else {
    m_rightJoystickSquare = -(m_rightJoystickValue * m_rightJoystickValue);
  } */
  m_rightJoystickQuarter = m_rightJoystickValue  * Constants.rotationInQuarter;
    m_drivetrain.drive(m_deadbander.deadband(m_leftJoystickSquare), m_deadbander.deadband(m_rightJoystickQuarter));

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