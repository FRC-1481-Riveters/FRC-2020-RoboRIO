/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.util.Units;
import edu.wpi.first.wpilibj2.command.TrapezoidProfileCommand;
import frc.robot.Constants;
import frc.robot.subsystems.DriveTrain;

public class AutonRobotDriveTurnDegreez extends TrapezoidProfileCommand {
  /**
   * Creates a new AutonRobotDriveTurnDegreez.
   */
  public AutonRobotDriveTurnDegreez(DriveTrain driveTrain, double degreezToTurn){
    super(        
      // The motion profile to be executed
    new TrapezoidProfile(
        // The motion profile constraints
        new TrapezoidProfile.Constraints(Constants.kMaxSpeedMetersPerSecond,
            Constants.kMaxAccelerationMetersPerSecondSquared),
        // Goal state
        new TrapezoidProfile.State(Units.feetToMeters(degreezToTurn / 100), 0),
        // Initial state
        new TrapezoidProfile.State()),
    state -> {
      driveTrain.driveAtSpeed(state.velocity, -state.velocity);
    }, driveTrain);
  }

}
