/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Kicker;

public class KickerConstantSpeedCommand extends CommandBase {
  private Kicker m_kicker;
  protected boolean m_powerCellDetected;
  /**
   * Creates a new KickerConstantSpeedCommand.
   */
  public KickerConstantSpeedCommand(Kicker subsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_kicker = subsystem;
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  @Override
  public void execute() {
    /*
     * Read the state of the Power Cell detector sensor in the kicker. If there's no
     * Power Cell inside the kicker then activate the kicker motor and try to
     * capture a Power Cell inside the kicker to prepare it for shooting, and to get
     * it out of the way of the four Power Cells that might eventually follow it.
     */
    m_powerCellDetected = m_kicker.getPowerCellDetected();

    /*
     * If there's no Power Cell in the kicker, then start the kicker motor and try
     * to get one into the mechanism.
     */
    if (!m_powerCellDetected) {
      m_kicker.setClosedLoopSpeed(Constants.kickerClosedLoopSpeed);
    }

  }
  @Override
  public void end(boolean interrupted) {
    m_kicker.setSpeed(0);
  }

  @Override
  public boolean isFinished() {
    /*
     * When a Power Cell is detected inside the kicker, the KickerCaptureCommand is
     * done with its job and it's time to end the command.
     */
    return m_powerCellDetected;
  }
}
