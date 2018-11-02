package eu.kudan.ar.di;

import android.content.Context;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;

import com.jme3.math.Vector3f;

import commonlib.location.AngleToNorthCalculator;
import commonlib.location.LocationDistanceCalculator;
import commonlib.model.BuildingModelSampleImpl;
import commonlib.model.texture.Texturizer;
import commonlib.model.texture.TexturizerModelBlack;
import commonlib.rotation.ModelRotator;
import commonlib.rotation.Rotator;
import commonlib.rotation.VectorRotator;
import commonlib.storage.FloatMeanRingBuffer;
import commonlib.storage.LocationMeanRingbufferImp;
import commonlib.storage.RingBufferImpl;
import eu.kudan.ar.ar.ARBuildingsPositioner;
import eu.kudan.ar.ar.position.GPSBuildingPositioner;
import eu.kudan.ar.ar.position.location.LocationFilter;
import eu.kudan.ar.ar.position.location.NorthSensorListener;
import eu.kudan.ar.ar.position.location.PhysicalNorthInitializer;
import eu.kudan.kudan.ARAPIKey;
import eu.kudan.kudan.ARGyroManager;
import eu.kudan.kudan.ARModelImporter;
import eu.kudan.kudan.ARModelNode;

public class ProjectInitializer {
    public static ARBuildingsPositioner initGPSSingleBuildingSolution(Context context){
        KudanDevAPIKey();

        ARGyroManager gyroManager = ARGyroManager.getInstance();
        gyroManager.initialise();
        ARModelImporter importer = new ARModelImporter();
        importer.loadFromAsset("ARBuilding.armodel");
        ARModelNode model = importer.getNode();
        gyroManager.getWorld().addChild(model);


        model.rotateByRadians((float) Math.toRadians(-100), 0,1,0);


        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);


        AngleToNorthCalculator angleCalculator = new AngleToNorthCalculator();
        Texturizer blackTexture = new TexturizerModelBlack();
        BuildingModelSampleImpl buildingModel = new BuildingModelSampleImpl(model, blackTexture);
        ModelRotator modelRotator = new ModelRotator(model);
        LocationDistanceCalculator locationDistanceCalculator = new LocationDistanceCalculator();
        RingBufferImpl<Location> locationMean = new LocationMeanRingbufferImp(5);
        LocationFilter locationFilter = new LocationFilter(locationMean, locationManager);
        RingBufferImpl<Float> angleRingBuffer = new FloatMeanRingBuffer(10);
        NorthSensorListener northSensorListener = new NorthSensorListener(sensorManager, angleRingBuffer);
        Rotator<Vector3f> vector3fRotator = new VectorRotator(new Vector3f());

        PhysicalNorthInitializer physicalNorthInitializer = new PhysicalNorthInitializer(northSensorListener, vector3fRotator);

        GPSBuildingPositioner gpsPositioner = new GPSBuildingPositioner(locationFilter, physicalNorthInitializer, locationDistanceCalculator, buildingModel);
        return new ARBuildingsPositioner(gpsPositioner);
    }

    private static void KudanDevAPIKey(){
        ARAPIKey key = ARAPIKey.getInstance();
        // TODO: Using the development key, change in the future?
        key.setAPIKey("agWZcpYLYjBxCbWf2qZx6k+PWISqeGtFCqKaZwYtwS+kdn1HKiQAmsJ55STRBe9BqCw3VwG6qL+ESI5ntTF/iV/uekLG3PCokaUE0/uTzqhaYlxRdmuNBIduzBCjq3mV2na+gy3ffHH9Ipc7eIN0geTj3p+ppsmK0U399iGmN38ndIh6k2y16cByWIecMSU3yw3Ztw7gHRqf83hVhZ5T2ACGK4SNkQhhdKp+CTaR5W3amYCJBgwumqFqNFyI9UniuMk70T/cQObRQum2U51OjjbMfmEAwIBt8Q8jD2yACzye6K4/1O4pZhbGEbiDeLrAfxqMwBAe5o6vnYIilGNnpDhfi3wOHhRaqtLOVvB58GUIFTnAPvmYFVnLWRJmCUZ9FJNDyX3ALCl/alFEWh+A/a6NFjcwLGKI9drPuGG4ONFg4p0l+p3b9DZoLzszlmWAflI/UFzQa++kQn3/sclO9i0vPnpi0LWoABm5vGswLVAIX/0k6384GXxfkADI6fjGtf62XJ5ImaVDiiREa9mabWEQGoifghQG1sGNDYgBIYEpiaLsVzOfTALpe20Q7kFCMjedJImQhhuLtEK1BXfXJEed1QqUOsG9IeKxKk28GbOtOF9w3yrSF3gnJslzZxF2kEF3C6ckog8byagS+4p37FJmbpPsiKNH1Qm0LuouGcQ=");
    }
}
