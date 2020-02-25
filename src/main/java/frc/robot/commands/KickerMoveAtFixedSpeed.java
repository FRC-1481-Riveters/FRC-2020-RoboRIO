/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Kicker;

public class KickerMoveAtFixedSpeed extends CommandBase {
  private Kicker m_kicker;
  private double m_speed;
  private int m_ticks;

  /**
   * Creates a new KickerMoveAtFixedSpeed.
   */
  public KickerMoveAtFixedSpeed(Kicker kicker, double speedRPM) {
    addRequirements(kicker);
    m_kicker = kicker;
    m_speed = speedRPM;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_kicker.setSpeed(m_speed * 1 / 5000.0); // TODO: Update to use closed loop speed interface
    m_ticks = 0;
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
    return (++m_ticks > 10); // TODO: Only return true when the speed has reached target speed.
  }
}
