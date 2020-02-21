/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import javax.annotation.meta.When;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Indexer;
import irsensor.IRSensor;

public class IndexerCarryUpCommand extends CommandBase {
  private Indexer m_upperIndexer;
  /**
   * Creates a new indexerCarryUpCommand.
   */
  public IndexerCarryUpCommand(Indexer subsystem) {
    m_upperIndexer = subsystem;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  //m_upperIndexer.setClosedLoopPosition();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  //when ir sensor detects [distance] of balls, the indexer spins
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_upperIndexer.setClosedLoopSpeed(0.0); 
  }

  @Override
  public boolean isFinished() {
  //when encoder count reaches [number], indexer stops
  // Returns true when the command should end.
  return false;
  }
  }
