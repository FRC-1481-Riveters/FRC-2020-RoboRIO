/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveTrain;

public class BreakInGearboxCommand extends CommandBase {
  private DriveTrain m_driveTrain;
  /**
   * Creates a new BreakInGearboxCommand.
   */
  public BreakInGearboxCommand(DriveTrain drivetrain) {
   m_driveTrain = drivetrain;
   addRequirements(m_driveTrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    SmartDashboard.putNumber("BreakInGearBoxSpeed",0.0);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double driveSpeed;
    driveSpeed = SmartDashboard.getNumber("BreakInGearBoxSpeed",0.0);

    m_driveTrain.drive(driveSpeed,0.0);
    
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
