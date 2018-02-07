package edu.neu.madcourse.binodthapachhetry.FindingAstro;

/**
 * Created by jarvis on 4/7/16.
 */
public class LowPassFilterLinearAccel {
    private static final String tag = LowPassFilterLinearAccel.class
            .getSimpleName();

    private float filterCoefficient = 0.5f;

    // Gravity and linear accelerations components for the
    // Wikipedia low-pass filter
    private float[] output = new float[]
            { 0, 0, 0 };

    private float[] gravity = new float[]
            { 0, 0, 0 };

    // Raw accelerometer data
    private float[] input = new float[]
            { 0, 0, 0 };

    /**
     * Add a sample.
     *
     * @param acceleration
     *            The acceleration data.
     * @return Returns the output of the filter.
     */
    public float[] addSamples(float[] acceleration)
    {
        System.arraycopy(acceleration, 0, input, 0, acceleration.length);

        float oneMinusCoeff = (1.0f - filterCoefficient);

        gravity[0] = filterCoefficient * gravity[0] + oneMinusCoeff * input[0];
        gravity[1] = filterCoefficient * gravity[1] + oneMinusCoeff * input[1];
        gravity[2] = filterCoefficient * gravity[2] + oneMinusCoeff * input[2];

        // Determine the linear acceleration
        output[0] = input[0] - gravity[0];
        output[1] = input[1] - gravity[1];
        output[2] = input[2] - gravity[2];

        return output;
    }

    /**
     * The complementary filter coefficient, a floating point value between 0-1,
     * exclusive of 0, inclusive of 1.
     *
     * @param filterCoefficient
     */
    public void setFilterCoefficient(float filterCoefficient)
    {
        this.filterCoefficient = filterCoefficient;
    }
}
