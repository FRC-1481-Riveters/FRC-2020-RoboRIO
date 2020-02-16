/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.CameraSubsystem;


public class CycleCameraFeedCommand extends InstantCommand {

  protected CameraSubsystem m_cameraSubsystem;
  
  public CycleCameraFeedCommand(CameraSubsystem subsystem) {
    m_cameraSubsystem = subsystem;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_cameraSubsystem.selectNextDriverCameraFeed();
  }

  @Override
  public boolean runsWhenDisabled() {
    return true;
  }
}
