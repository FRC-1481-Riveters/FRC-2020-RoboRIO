/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class PowerCellArm extends SubsystemBase {
  private static WPI_TalonSRX m_powerCellArmLead = new WPI_TalonSRX(Constants.powerCellArmLeadCANId);
  private static WPI_TalonSRX m_powerCellArmFollow = new WPI_TalonSRX(Constants.powerCellArmFollowCANId);
  /**
   * Creates a new PowerCellArm.
   */
  public PowerCellArm() {

    m_powerCellArmFollow.setInverted(true);
    m_powerCellArmFollow.follow(m_powerCellArmLead);

    SmartDashboard.putData(m_powerCellArmLead);
    m_powerCellArmLead.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder,
        Constants.kPIDLoopIdx, Constants.kTimeoutMs);

        m_powerCellArmLead.config_kF(0, 0.0);
        m_powerCellArmLead.config_kP(0, 0.8);
        m_powerCellArmLead.config_kI(0, 0.0);
        m_powerCellArmLead.config_kD(0, 0.0);
  }
  public int getActualPosition() {
    /*
     * This value is converted with a negative sign to switch the way the sensor
     * reports rotation. It's backwards compared to how the code expects the sensor
     * to rotate.
     */
    return m_powerCellArmLead.getSelectedSensorPosition();
  }
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
