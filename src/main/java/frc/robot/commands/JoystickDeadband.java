/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

/**
 * Add your docs here.
 */
public class JoystickDeadband {
    private double m_deadband = 0.05;
    
    public JoystickDeadband(double deadband) {
        m_deadband = deadband;
    }
    public JoystickDeadband() {
    }
    public double deadband(double joystickAxisValue) {
        if ((joystickAxisValue > m_deadband) || (joystickAxisValue < -m_deadband)) {
            return joystickAxisValue;
        }
        return 0;
    }
}
