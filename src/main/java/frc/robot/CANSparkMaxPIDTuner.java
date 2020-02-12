/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.revrobotics.CANError;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;

/**
 * Add your docs here.
 */
public class CANSparkMaxPIDTuner extends CANSparkMax implements Sendable, AutoCloseable {

    public class CANPIDControllerTunable extends CANPIDController {

        CANSparkMax m_device;

        public CANPIDControllerTunable(CANSparkMax device) {
            super(device);
            m_device = device;
        }

        public CANError setReference(double value, ControlType ctrl, int pidSlot, double arbFeedforward,
                ArbFFUnits arbFFUnits) {
            return CANError.kOk; //m_device.setpointCommand(value, ctrl, pidSlot, arbFeedforward, arbFFUnits.value);
        }

    }

    private static int m_instances;
    private boolean m_isEnabled;

    /**
     * @return An object for interfacing with the integrated PID controller.
     */
    public CANPIDControllerTunable getPIDController() {
        return new CANPIDControllerTunable(this);
    }

    public CANSparkMaxPIDTuner(int deviceID, MotorType type) {
        super(deviceID, type);

        m_instances++;
        SendableRegistry.addLW(this, "PIDController", m_instances);
    }

    @Override
    public void close() {
        SendableRegistry.remove(this);
    }

    public double getP() {
        return this.getPIDController().getP();
    }

    public void setP(double value) {
        this.getPIDController().setP(value);
    }

    public double getI() {
        return this.getPIDController().getI();
    }

    public void setI(double value) {
        this.getPIDController().setI(value);
    }

    public double getD() {
        return this.getPIDController().getD();
    }

    public void setD(double value) {
        this.getPIDController().setD(value);
    }

    public double getSetpoint() {
        return get();
    }

    public void setSetpoint(double value) {
        set(value);
    }

    public void setNewEnable(boolean value) {
        if (value) {
            setSetpoint(getSetpoint());
        } else {
            stopMotor();
        }

        m_isEnabled = value;
    }

    public boolean getIsEnabled() {
        return m_isEnabled;
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("PIDController");
        builder.setActuator(true);

        builder.addDoubleProperty("p", this::getP, this::setP);
        builder.addDoubleProperty("i", this::getI, this::setI);
        builder.addDoubleProperty("d", this::getD, this::setD);
        builder.addDoubleProperty("setpoint", this::getSetpoint, this::setSetpoint);
        builder.addBooleanProperty("enabled", this::getIsEnabled, this::setNewEnable);
    }

}
