/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.Constants;
import frc.robot.subsystems.Intake;

public class IntakePositionPowerCellCommand extends PIDCommand {
  /**
   * Creates a new IntakePositionPowerCellCommand.
   */
  public IntakePositionPowerCellCommand(double howCloseIsThePowerCell, Intake m_intake) {
    super(new PIDController(Constants.kGains.kP, Constants.kGains.kI, Constants.kGains.kD),
    m_intake.getDistanceToPowerCellMeasurer(),
    howCloseIsThePowerCell,
    output -> m_intake.setSpeed(output),
    m_intake
    );
    // Use addRequirements() here to declare subsystem dependencies.
    getController()
      .setTolerance(Constants.howCloseIsThePowerCellTolerance);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return getController().atSetpoint();
  }
}
