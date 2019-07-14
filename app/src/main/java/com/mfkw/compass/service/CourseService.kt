package com.mfkw.compass.service

import com.mfkw.compass.model.Coordinates
import java.lang.Math.*

class CourseService {
    fun computeCourse(current: Coordinates, target: Coordinates): Double {
        val lat2 = toRadians(target.latitude)
        val lat1 = toRadians(current.latitude)
        val lon12 = toRadians(target.longitude - current.longitude)
        val azimuth = atan(
            cos(lat2) * sin(lon12) /
                    (cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(lon12))
        )
        return when {
            lat2 > lat1 -> azimuth
            azimuth > 0 -> azimuth + Math.PI.toFloat()
            else -> azimuth - Math.PI.toFloat()
        }
    }

}
