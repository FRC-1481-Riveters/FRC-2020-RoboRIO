/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.pseudoresonance.pixy2api.Pixy2;
import io.github.pseudoresonance.pixy2api.Pixy2CCC.Block;

public class CameraSubsystem extends SubsystemBase {

  private Pixy2 pixycam;
  ArrayList<Block> blocks;

  /**
   * Creates a new PixyCameraSubsystem.
   */
  public CameraSubsystem() {
    pixycam = Pixy2.createInstance(Pixy2.LinkType.SPI);
    pixycam.init(0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    int blockState = pixycam.getCCC().getBlocks(false, 255, 8); // run getBlocks with arguments to have the camera

    if (blockState >= 0) {
      // acquire target data
      blocks = pixycam.getCCC().getBlocks(); // assign the data to an ArrayList for convenience

      if (blocks.size() > 0) {
        double xcoord = blocks.get(0).getX(); // x position of the largest target
        double ycoord = blocks.get(0).getY(); // y position of the largest target

        String data = blocks.get(0).toString(); // string containing target info

        SmartDashboard.putBoolean("present", true); // show there is a target present
        SmartDashboard.putNumber("Xccord", xcoord);
        SmartDashboard.putNumber("Ycoord", ycoord);
        SmartDashboard.putString("Data", data);

      }
      
    SmartDashboard.putNumber("blocks detected", blocks.size()); // push to dashboard how many targets are detected
    }

  }
}
