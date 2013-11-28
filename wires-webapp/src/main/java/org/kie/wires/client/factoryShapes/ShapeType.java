package org.kie.wires.client.factoryShapes;
/**
 * At this moment this enum defines all the shapes with its categories for the stencil
 */
public enum ShapeType {
    //connectors
    LINE(ShapeCategory.CONNECTORS),
    
    //shapes
    CIRCLE(ShapeCategory.SHAPES), 
    RECTANGLE(ShapeCategory.SHAPES);
    
    

    private ShapeType(ShapeCategory category) {
        this.category = category;
    }

    private ShapeCategory category;

    public ShapeCategory getCategory() {
        return category;
    }

}