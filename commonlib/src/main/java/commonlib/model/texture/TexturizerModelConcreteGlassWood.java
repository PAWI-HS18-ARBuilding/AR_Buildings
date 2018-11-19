package commonlib.model.texture;

import eu.kudan.kudan.ARLightMaterial;
import eu.kudan.kudan.ARModelNode;
import eu.kudan.kudan.ARTexture2D;

public class TexturizerModelConcreteGlassWood implements Texturizer {
    @Override
    public ARModelNode setTexture(ARModelNode model) {
        ARTexture2D concreteTexture = new ARTexture2D();
        ARTexture2D glassTexture = new ARTexture2D();
        ARTexture2D woodTexture = new ARTexture2D();

        concreteTexture.loadFromAsset("concrete.jpg");
        glassTexture.loadFromAsset("glass.jpg");
        woodTexture.loadFromAsset("wood.jpg");

        ARLightMaterial concreteMaterial = new ARLightMaterial();
        concreteMaterial.setTexture(concreteTexture);
        concreteMaterial.setAmbient(0.8f, 0.8f, 0.8f);

        ARLightMaterial woodMaterial = new ARLightMaterial();
        woodMaterial.setTexture(woodTexture);
        woodMaterial.setAmbient(0.8f, 0.8f, 0.8f);

        ARLightMaterial glassMaterial = new ARLightMaterial();
        glassMaterial.setTexture(glassTexture);
        glassMaterial.setAlpha(0.4f);
        glassMaterial.setReflectivity(0.5f);
        glassMaterial.setAmbient(0.8f,0.8f,0.8f);

        model.getMeshNodes().forEach(arMeshNode -> arMeshNode.setMaterial(concreteMaterial));
        model.getMeshNodes().forEach(
                arMeshNode -> {if (arMeshNode.getName().contains("glass")) arMeshNode.setMaterial(glassMaterial);});
        model.getMeshNodes().forEach(
                arMeshNode -> {if (arMeshNode.getName().contains("wood")) arMeshNode.setMaterial(woodMaterial);});
        return model;
    }
}
