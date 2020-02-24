/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;


import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Elevator;

public class lowerElevator extends CommandBase {
  public Elevator m_Elevator;
  public int m_targetElevatorPosition;
  public int m_currentElevatorPosition;
  /**
   * Creates a new raiseElevator.
   */
  public lowerElevator(Elevator Subsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_Elevator =  Subsystem;
    addRequirements(Subsystem);
    
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  m_targetElevatorPosition = Constants.minWinchEncoderCounts;
  m_Elevator.climbToPosition(m_targetElevatorPosition);
  SmartDashboard.putNumber("Target Elevator Position",m_targetElevatorPosition);
 //m_currentElevatorPosition = m_Elevator.getSensorCollection().getQuadraturePosition(0, Constants.kTimeoutMs);
}
  

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_Elevator.stopElevator();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
