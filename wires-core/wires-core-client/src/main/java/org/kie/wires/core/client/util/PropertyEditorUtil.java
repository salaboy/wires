package org.kie.wires.core.client.util;

import java.util.List;

import com.google.common.collect.Lists;
import org.kie.uberfire.properties.editor.model.PropertyEditorCategory;
import org.kie.uberfire.properties.editor.model.PropertyEditorFieldInfo;
import org.kie.uberfire.properties.editor.model.PropertyEditorType;
import org.kie.wires.core.api.shapes.WiresBaseShape;

public class PropertyEditorUtil {

    private static final String ATTRIBUTES = "Attributes";
    private static final String POSITION_NODE = "Position Node";

    public static List<PropertyEditorCategory> createProperties( final WiresBaseShape shape ) {
        int priority = 0;
        PropertyEditorCategory position = new PropertyEditorCategory( POSITION_NODE, priority )
                .withField( new PropertyEditorFieldInfo( "X",
                                                         String.valueOf( shape.getX() ),
                                                         PropertyEditorType.NATURAL_NUMBER ) )
                .withField( new PropertyEditorFieldInfo( "Y",
                                                         String.valueOf( shape.getY() ),
                                                         PropertyEditorType.NATURAL_NUMBER ) );

        int monitorDefinitionPriority = 1;
        PropertyEditorCategory attributes = new PropertyEditorCategory( ATTRIBUTES, monitorDefinitionPriority )
                .withField( new PropertyEditorFieldInfo( "WIDTH",
                                                         String.valueOf( shape.getAttributes().getWidth() ),
                                                         PropertyEditorType.NATURAL_NUMBER ) )
                .withField( new PropertyEditorFieldInfo( "HEIGHT",
                                                         String.valueOf( shape.getAttributes().getHeight() ),
                                                         PropertyEditorType.NATURAL_NUMBER ) );

        return Lists.newArrayList( position, attributes );
    }

}
