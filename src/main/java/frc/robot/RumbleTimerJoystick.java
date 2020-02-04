/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Subsystem;

/**
 * Build a new class that looks a LOT like the XboxController class, but adds a
 * programmable rumbleTime timer so the joystick can be commanded to rumble for
 * a fixed length of time. This informs the operator or driver of important
 * events they should know about.
 * 
 * The RumbleTimerJoystick needs a periodic call so it can send a command to the
 * physical joystick, through the driver station, to turn off the rumbler.
 * 
 * Provide this periodic call by masquerading as a Subsystem on the robot.
 * Subsystems: 1) Have a method called "register()" that automatically informs
 * the scheduler to periodically call a method named "periodic()" every 20 ms 2)
 * Always have a function called "periodic()" that gets called automatically by
 * the scheduler after they're registered.
 * 
 */
public class RumbleTimerJoystick extends XboxController implements Subsystem {

    private long m_timeStampTimeout;

    public RumbleTimerJoystick(int port) {
        super(port);

        /*
         * Register this object as a Subsystem so its "periodic()" function gets called
         * periodically, just like a Subsystem gets called.
         * 
         * This is the easiest way to get the robot to call our code every 20 ms so we
         * can manage the joystick's rumbler and turn it off when it's done rumbling.
         * 
         * register() is a default function that simply calls the CommandScheduler's
         * registerSubsystem() method. This method accepts a Subsystem object to be
         * scheduled every 20 ms by the scheduler.
         * 
         * Since RumbleTimerJoystick implements the Subsystem interface, it looks like a
         * Subsystem object to the Scheduler! Scheduler doesn't know the difference and
         * doesn't care. It just cares that there's a periodic() method it can call
         * every 20 ms (which RumbleTimerJoystick is happy to provide.)
         */
        register();
    }

    /*
     * rumbleTime() accepts a long that represents the number of milliseconds to
     * rumble the joystick. After that length of time elapses, the
     * RumbleTimerJoystick automatically turns off the rumbler; the command or
     * subsystem doesn't have to remember that it needs to turn off the joystick's
     * rumbler. RumbleTimerJoystick handles everything automatically.
     */
    public void rumbleTime(long durationMilliseconds) {

        long newTimestamp = durationMilliseconds + System.currentTimeMillis();

        m_timeStampTimeout = Math.max(m_timeStampTimeout, newTimestamp);

        setRumble(RumbleType.kLeftRumble, 1.0);
        setRumble(RumbleType.kRightRumble, 1.0);

    }

    public void periodic() {
        if (m_timeStampTimeout != 0 && System.currentTimeMillis() > m_timeStampTimeout) {
            setRumble(RumbleType.kLeftRumble, 0);
            setRumble(RumbleType.kRightRumble, 0);
            m_timeStampTimeout = 0;
        }
    }
}