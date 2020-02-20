package irsensor;

import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Sendable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.DoubleSupplier;

public class IRSensor implements Sendable, DoubleSupplier {

  protected DoubleSupplier m_voltageSupplier;

  protected SensorConstants m_constants;

  public enum SensorType {
    GP2Y0A51SK0F, GP2Y0A41SK0F, GP2Y0A21YK0F, Unknown
  }

  /*
   * Setup a database of constants for different IR sensors. These sensors all
   * have similar transfer functions that relate the sensor's output voltage into
   * distance.
   */
  protected Map<SensorType, SensorConstants> m_sensorData = new HashMap<>() {

    private static final long serialVersionUID = 1L;

    {/* a b c d */
      /* 2cm to 15cm sensor https://docs.google.com/spreadsheets/d/1WDuqup1BjJ5tSHi0QAIiMzjmCvPXcLYf04QBY6rFUlM */
      put(SensorType.GP2Y0A51SK0F, new SensorConstants(0.09978106474, -0.2796664032, 0.4242351842, -0.06078793247));
      /* 4cm to 30cm sensor https://drive.google.com/open?id=1s9VOtxFxB0CnP3QuTGNOzM41Shyhu76YyYfZ8UnlbfQ */
      put(SensorType.GP2Y0A41SK0F, new SensorConstants(0.007868008148, -0.02931278423, 0.1183284175, -0.01775494828));
      /* 10cm to 80cm sensor https://drive.google.com/open?id=1EhguMsN7Y0s3V13kPuc1Tq-6Fl6-bCZ4UHqwouJhZaY */
      put(SensorType.GP2Y0A21YK0F, new SensorConstants(0.003276154772, -0.008135823685, 0.04831604976, -0.006248190417));
      put(SensorType.Unknown, new SensorConstants(0.0, 0.0, 1.0, 0.0));
    }
  };

  /*
   * Create a new IR Sensor that gets its voltage from some arbitrary voltage
   * supplier that implements the DoubleSupplier interface. For instance, if you
   * connect an IR Sensor to the analog input pin on a TalonSRX's 2x5 data port,
   * you'll have to use this particular constructor to get IRSensor to read and
   * process the Talon's input voltage.
   * 
   * Create a Function interface that will read the talon's analog input raw value
   * (which is 0 to 1023 between 0.0 to 3.3 volts) and converts it to a voltage
   * between 0.0 and 3.3 volts.
   * 
   * Create a DoubleSupplier that reads this talonSRX analog voltage through that
   * Function interface. This new DoubleSupplier (which is named voltageSupplier
   * here) is what the IRSensor understands how to read. IRSensor will read
   * voltageSupplier and convert that voltage (which is the TalonSRX's analog
   * input voltage) into a distance value!
   * 
   * 
   * Call sensor's getRangeCm function to get the distance in centimeters from the
   * voltage the TalonSRX measured at its analog port.
   */

  /*
   * import java.util.function.DoubleSupplier;
   * 
   * import java.util.function.Function;
   * 
   * ...
   * 
   * // for Talon SRXs:
   * 
   * Function<WPI_TalonSRX, Double> readAna = (talon) ->
   * (talon.getSensorCollection().getAnalogInRaw() / 310.0);
   * 
   * // OR for Spark Maxs:
   * 
   * Function<CANSparkMax,Double> readAna = (controller)
   * ->controller.getAnalog(AnalogMode.kAbsolute).getVoltage();
   * 
   * DoubleSupplier voltageSupplier = () -> readAna.apply(m_myMotorController);
   * 
   * IRSensor sensor = new IRSensor(IRSensor.SensorType.GP2Y0A41SK0F,
   * voltageSupplier);
   * 
   * double distance = sensor.getRangeCm();
   * 
   */

  public IRSensor(SensorType type, DoubleSupplier voltageSupplier) {
    m_constants = getConstant(type);
    m_voltageSupplier = voltageSupplier;
  }

  /*
   * Read the voltage at any of the RoboRIO's Analog input pins. Tell IRSensor to
   * create the AnalogInput object for you. Remember: each analog input can have
   * only ONE object!
   * 
   * for example:
   * 
   * IRSensor mySensor = new IRSensor(IRSensor.SensorType.GP2Y0A41SK0F,0); // Use
   * AnalogInput pin 0
   * 
   */

  public IRSensor(SensorType type, int analogChannelIndex) {
    m_constants = getConstant(type);
    m_voltageSupplier = new AnalogInputDoubleSupplier(analogChannelIndex);
  }
  /*
   * WARNING! If you try to create a new AnalogInput that's already on the same
   * pin as another AnalogInput you've already created... YOUR. ROBOT. WILL.
   * CRASH!
   */

  /*
   * If you've already created an AnalogInput channel before creating the
   * IRSensor, and you want to put an IRSensor on an AnalogInput channel, simply
   * pass the AnalogInput object to IRSensor with this constructor and IRSensor
   * will read from this channel without creating a new AnalogInput object.
   * 
   * For example:
   * 
   * import edu.wpi.first.wpilibj.AnalogInput;
   * 
   * ...
   * 
   * AnalogInput myAnaInput = new AnalogInput(0); // Read from input 0
   * 
   * IRSensor mySensor = new
   * IRSensor(IRSensor.SensorType.GP2Y0A41SK0F,myAnaInput);
   * 
   */
  public IRSensor(SensorType type, AnalogInput analogChannel) {
    m_constants = getConstant(type);
    m_voltageSupplier = new AnalogInputDoubleSupplier(analogChannel);
  }

  public double getRangeCm() {
    double voltage = m_voltageSupplier.getAsDouble();

    try {
      /*
       * Compute the distance estimate for this sensor's voltage by running it through
       * the following transfer function:
       * 
       * Distance(Voltage) = 1 / (a*Voltage^3 + b*Voltage^2 + c*Voltage + d)
       */
      double distance = 1.0 / ((m_constants.a * Math.pow(voltage, 3.0)) + (m_constants.b * Math.pow(voltage, 2.0))
          + (m_constants.c * voltage) + m_constants.d);

      return Math.max(distance, 0.0);

    } catch (ArithmeticException ex) {
      System.out.printf("IRSensor couldn't compute range from voltage %3.2f. Returned 0.0: %s", voltage, ex.toString());
    }

    return 0.0;
  }

  public double getAsDouble() {
    return getRangeCm();
  }

  private SensorConstants getConstant(SensorType type) {

    SensorConstants constantValues;

    try {
      constantValues = m_sensorData.get(type);
    } catch (Exception ex) {
      constantValues = m_sensorData.get(SensorType.Unknown);
    }

    return constantValues;
  }

  public void initSendable(SendableBuilder builder) {
    builder.setSmartDashboardType("IRSensor");
    builder.addDoubleProperty("Range (cm)", this::getRangeCm, null);
    builder.addDoubleProperty("RawVolts", m_voltageSupplier, null);
  }

  protected class AnalogInputDoubleSupplier implements DoubleSupplier {

    protected AnalogInput m_analogInput;

    public AnalogInputDoubleSupplier(AnalogInput analogInput) {
      m_analogInput = analogInput;
    }

    public AnalogInputDoubleSupplier(int analogInput) {
      m_analogInput = new AnalogInput(analogInput);
    }

    @Override
    public double getAsDouble() {
      return m_analogInput.getVoltage();
    }
  }

  protected class SensorConstants {
    /*
     * These are the linearization constants that will fit the sensor's distance
     * data to the inverse of a 3rd order polynomial to support the sensor's
     * transfer function of the form Distance in cm of Voltage in volts:
     * 
     * Distance(Voltage) = 1 / (a*Voltage^3 + b*Voltage^2 + c*voltage + d)
     * 
     * Why is this an *inverted* polynomial? Well, that's because the sensor's
     * nature is to generate a reciprocal relationship between the distance measure
     * and the sensor's output voltage. This inverted relationship further fits a
     * cubic function really well.
     */
    public double a; // a * x^3 +
    public double b; // b * x^2 +
    public double c; // c * x +
    public double d; // d

    public SensorConstants(double a, double b, double c, double d) {
      this.a = a;
      this.b = b;
      this.c = c;
      this.d = d;
    }
  }
}