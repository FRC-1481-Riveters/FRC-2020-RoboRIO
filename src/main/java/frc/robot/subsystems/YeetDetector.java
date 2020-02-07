/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.function.BooleanSupplier;


import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 * Add your docs here.
 */
public class YeetDetector {
    private double m_shooterIntendedSpeed = 10000.0;
    private long m_totalYeets = 0;

    private DescriptiveStatistics m_yeetWheelSpeedStatistics = new DescriptiveStatistics(5);
    private BooleanSupplier m_isAtSpeed;

    enum yeeterState {
        READY_TO_COUNT_YEETS, YEETED, INITIALIZING;
    };

    private yeeterState m_yeeterState = yeeterState.INITIALIZING;

    /*
     * Create a new YeetDetector. The YeetDetector accepts a BooleanSupplier that,
     * when invoked, will tell the YeetDetector if the shooter is at its designated
     * speed. Here's an example of creating a new YeetDetector inside of an object
     * that has a function called isAtSpeed() that returns a true if the shooter is
     * going the right speed, and false if it isn't:
     * 
     * private YeetDetector m_yeetDetector = new YeetDetector(this::isAtSpeed);
     * 
     * where the function isAtSpeed() is, for example, something like this:
     * 
     * public boolean isAtSpeed() { 
     * if (Math.abs( 
     * (getSpeed() - m_shooterIntendedSpeed) / m_shooterIntendedSpeed) <=
     * Constants.shooterIntendedSpeedTolerance) { 
     * return true;
     *  } else { 
     * return false; 
     *  } 
     * }
     * 
     * BooleanSuppliers are just stand-ins for a function that returns a boolean. So just pass
     * in a reference to the boolean-returning function when you call the constructor, and
     * BooleanSupplier will do the rest.
     * 
     */
    public YeetDetector(BooleanSupplier isAtSpeed) {
        m_isAtSpeed = isAtSpeed;
    }

    public long getTotalYeets() {
        return m_totalYeets;
    }

    private boolean isAtSpeed() {
        try {
            if (m_isAtSpeed != null) {
                return m_isAtSpeed.getAsBoolean();
            }
        } catch (Exception ex) {
        }
        return false;
    }

    /*
     * Call setNewSpeed with a new speed when the speed changes. Calling this
     * function resets the YeetDetector, so only call the setNewSpeed() function
     * when you are actually changing the speed setpoint. Don't do it continuously
     * in a loop because that will confuse YeetDetector and you won't get any yeet
     * detections.
     */
    public void setNewSpeed(double intendedSpeed) {

        m_shooterIntendedSpeed = intendedSpeed;
        m_yeeterState = yeeterState.INITIALIZING;
        m_yeetWheelSpeedStatistics.clear();
    }

    /*
     * Detect when the shooter has shot a POWER CELL.
     * 
     * Call detectYeets() periodically with the current speed of the shooter.
     * 
     * 
     * Use the reduction in shooter speed to detect when a POWER CELL has travelled
     * through the shooter. The shooter's wheel will slow down briefly when the ball
     * is traveling through the shooter.
     * 
     * 1) When a new speed is selected, wait until the shooter gets to that new
     * speed, then start acquiring speed samples to create a running average of the
     * speed. Use this running average to make the algorithm less sensitive to quick
     * transients in the motor's reported speed.
     * 
     * 2) When the speed has smoothed out, and enough samples are available to
     * compute a running average of the speed, change to the READY_TO_COUNT_YEETS
     * state. This state waits until the shooter's average speed drops by 10% below
     * the target speed. This means a ball has been shot through the shooter.
     * Increment the number of shots taken so other software can use this
     * information to control their behavior.
     * 
     * 3) Change to a new state, YEETED and wait until the shooter speeds up to its
     * target speed. This way, the yeet detector isn't getting average speed values
     * that aren't representive of the free-spinning speed (the speed that the
     * shooter spins at when it ISN'T shooting a POWER CELL, or is changing shooter
     * RPM targets.)
     * 
     * 4) If the shooter gets a new speed, reset the yeet detector by resetting the
     * running average speed and wait for the shooter to get back up to speed before
     * testing it to detect yeets. We don't want to make comparisons of the current
     * speed of the shooter to the previous m_shooterIntendedSpeed; they might not
     * be very close. So, reset everything and wait for the shooter to reach its new
     * speed before starting yeet detection again.
     */
    public void detectYeets(double currentRPM) {

        switch (m_yeeterState) {
        case READY_TO_COUNT_YEETS:
            m_yeetWheelSpeedStatistics.addValue(currentRPM);

            if (((m_yeetWheelSpeedStatistics.getMean() - m_shooterIntendedSpeed) / m_shooterIntendedSpeed) < -0.1) {
                m_totalYeets++;
                m_yeeterState = yeeterState.YEETED;
            }
            break;
        case YEETED:
            if (isAtSpeed()) {
                m_yeeterState = yeeterState.READY_TO_COUNT_YEETS;
            }

            break;
        case INITIALIZING:
            if (isAtSpeed()) {
                m_yeetWheelSpeedStatistics.addValue(currentRPM);
            }
            if (m_yeetWheelSpeedStatistics.getN() == m_yeetWheelSpeedStatistics.getWindowSize()) {
                m_yeeterState = yeeterState.READY_TO_COUNT_YEETS;
            }
            break;
        default:

            break;
        }
    }
}
