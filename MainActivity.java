package com.example.jonahphonehost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.graphics.Color;
import android.graphics.Typeface;

import com.example.jonahphonehost.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements SensorEventListener, LocationListener {

    // Used to load the 'jonahphonehost' library on application startup.
    static {
        System.loadLibrary("jonahphonehost");
    }

    private ActivityMainBinding binding;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor gyroscope;
    private LocationManager locationManager;

    private TextView accelerometerData;
    private TextView gyroscopeData;
    private TextView gpsData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        accelerometerData = binding.accelerometerData;
        gyroscopeData = binding.gyroscopeData;
        gpsData = binding.gpsData;

        // Set text color and typeface for accelerometer data
        accelerometerData.setTextColor(Color.GREEN);
        accelerometerData.setTypeface(accelerometerData.getTypeface(), Typeface.BOLD);

        // Set text color and typeface for gyroscope data
        gyroscopeData.setTextColor(Color.GREEN);
        gyroscopeData.setTypeface(gyroscopeData.getTypeface(), Typeface.BOLD);

        // Set text color and typeface for GPS data
        gpsData.setTextColor(Color.GREEN);
        gpsData.setTypeface(gpsData.getTypeface(), Typeface.BOLD);

        // Initialize SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Initialize accelerometer
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Initialize gyroscope
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        // Initialize LocationManager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register accelerometer and gyroscope listeners
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);

        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister accelerometer and gyroscope listeners
        sensorManager.unregisterListener(this);

        // Remove location updates
        locationManager.removeUpdates(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Check sensor type
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // Update accelerometer data
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // Format the values to two decimal places
            String formattedX = String.format("%.2f", x);
            String formattedY = String.format("%.2f", y);
            String formattedZ = String.format("%.2f", z);

            accelerometerData.setText("Accelerometer Data:\nX: " + formattedX + "\nY: " + formattedY + "\nZ: " + formattedZ);
        } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            // Update gyroscope data
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // Format the values to two decimal places
            String formattedX = String.format("%.2f", x);
            String formattedY = String.format("%.2f", y);
            String formattedZ = String.format("%.2f", z);

            gyroscopeData.setText("Gyroscope Data:\nX: " + formattedX + "\nY: " + formattedY + "\nZ: " + formattedZ);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do nothing
    }

    @Override
    public void onLocationChanged(Location location) {
        // Update GPS data
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        // Format the values to six decimal places
        String formattedLatitude = String.format("%.6f", latitude);
        String formattedLongitude = String.format("%.6f", longitude);

        gpsData.setText("GPS Data:\nLatitude: " + formattedLatitude + "\nLongitude: " + formattedLongitude);
    }

    @Override
    public void onProviderEnabled(String provider) {
        // Do nothing
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Do nothing
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Do nothing
    }

    /**
     * A native method that is implemented by the 'jonahphonehost' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}