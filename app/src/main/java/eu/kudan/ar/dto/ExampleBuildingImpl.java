package eu.kudan.ar.dto;

import android.location.Location;

import eu.kudan.kudan.ARLightMaterial;
import eu.kudan.kudan.ARMeshNode;
import eu.kudan.kudan.ARModelImporter;
import eu.kudan.kudan.ARModelNode;

public class ExampleBuildingImpl implements Building {
    private ARModelNode myModel;
    private Location location;
    public static final double ALTITUDE = 450;
    public static final double LATITUDE = 47.143353;
    public static final double LONGITUDE = 8.432946;

    public ExampleBuildingImpl(){
        location = new Location("");
        location.setAccuracy(0);
        location.setAltitude(ALTITUDE);
        location.setLongitude(LONGITUDE);
        location.setLatitude(LATITUDE);

        ARModelImporter importer = new ARModelImporter();
        importer.loadFromAsset("ARBuilding.armodel");
        myModel = importer.getNode();
        myModel.rotateByDegrees(-60, 0,1,0 );
        myModel.scaleByUniform(1f);



        ARLightMaterial concreteMaterial = new ARLightMaterial();
        concreteMaterial.setAmbient(1f, 1f, 1f);

        for(ARMeshNode meshNode : myModel.getMeshNodes()) {
            meshNode.setMaterial(concreteMaterial);
        }
    }

    @Override
    public ARModelNode getKudanModelNode() {
        return myModel;
    }

    @Override
    public Location getBuildingLocation() {
        return location;
    }
}
