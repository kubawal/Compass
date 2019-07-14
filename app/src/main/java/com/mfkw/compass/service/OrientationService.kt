package com.mfkw.compass.service

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

private const val SAMPLING_RATE_US = 100000

class OrientationService(private val sensorManager: SensorManager) {
    private var lastAccelerometerReading: FloatArray? = null
    private var lastMagnetometerReading: FloatArray? = null
    private var currentCallback: (Float) -> Unit = {}
    private var accelerometerListener: SensorEventListener? = null
    private var magnetometerListener: SensorEventListener? = null
    private val computationArray1 = FloatArray(9)
    private val computationArray2 = FloatArray(3)

    fun startOrientationUpdates(callback: (Float) -> Unit) {
        val accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        accelerometerListener = sensorEventListenerFor { onAccelerometerReading(it.values) }
        magnetometerListener = sensorEventListenerFor { onMagnetometerReading(it.values) }
        currentCallback = callback

        sensorManager.registerListener(
            accelerometerListener,
            accelerometerSensor,
            SAMPLING_RATE_US
        )
        sensorManager.registerListener(
            magnetometerListener,
            magnetometerSensor,
            SAMPLING_RATE_US
        )
    }

    fun stopOrientationUpdates() {
        sensorManager.unregisterListener(accelerometerListener)
        sensorManager.unregisterListener(magnetometerListener)
        lastAccelerometerReading = null
        lastMagnetometerReading = null
        accelerometerListener = null
        magnetometerListener = null
    }

    private fun sensorEventListenerFor(callback: (SensorEvent) -> Unit) =
        object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
            override fun onSensorChanged(event: SensorEvent?) {
                if (event != null)
                    callback(event)
            }
        }

    private fun onAccelerometerReading(reading: FloatArray) {
        lastAccelerometerReading = reading
        updateOrientation()
    }

    private fun onMagnetometerReading(reading: FloatArray) {
        lastMagnetometerReading = reading
        updateOrientation()
    }

    private fun updateOrientation() {
        if (lastAccelerometerReading != null && lastMagnetometerReading != null)
            currentCallback(computeOrientation())
    }

    private fun computeOrientation(): Float {
        SensorManager.getRotationMatrix(computationArray1, null, lastAccelerometerReading, lastMagnetometerReading)
        SensorManager.getOrientation(computationArray1, computationArray2)
        return computationArray2[0]
    }
}
