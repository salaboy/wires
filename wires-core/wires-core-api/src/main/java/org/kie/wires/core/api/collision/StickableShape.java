/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.kie.wires.core.api.collision;

import java.util.List;

/**
 *
 * @author salaboy
 * AKA shapes with Magnets
 */
public interface StickableShape {
    List<Magnet> getMagnets();
    
    void attachControlPointToMagent(Magnet selectedMagnet);
    
    void showMagnetsPoints();
    void hideMagnetPoints();
}
