package org.kie.wires.client.factoryShapes;

public enum ShapeType {
    LINE(1), CIRCLE(2), RECTANGLE(2);
    
    private ShapeType(Integer category){
        this.category = category;
    }
    
    private Integer category;

    public Integer getCategory() {
        return category;
    }
    
    
}
