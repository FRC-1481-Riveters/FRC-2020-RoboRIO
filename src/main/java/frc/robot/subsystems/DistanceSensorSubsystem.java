/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.concurrent.ArrayBlockingQueue;

import org.usfirst.frc.team5461.robot.sensors.VL53L0XSensors;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DistanceSensorSubsystem extends SubsystemBase {
  private VL53L0XSensors distance;

  /**
   * Creates a new DistanceSensor.
   */
  public DistanceSensorSubsystem() {

    distance = new VL53L0XSensors();
    boolean success = false;

    success = distance.init();
    if (success) {
      System.out.println("VL53L0X sensors initialized.");
    } else {
      System.out.println("VL53L0X sensors NOT initialized!!!!!");
    }
  }

  @Override
  public void periodic() {
    Double[] distances = new Double[distance.arrayBlockingQueueList.size()];

    int i = 0;
    for (ArrayBlockingQueue<Integer> queue : distance.arrayBlockingQueueList) {
      Integer result = queue.poll();
      if (result != null) {
        distances[i] = Double.valueOf(result);
      }
      i++;
    }

    SmartDashboard.putNumberArray("distanceSensorValues", distances);
  }
}
