/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.kie.wires.client.shapes.collision;

import java.util.List;

/**
 *
 * @author salaboy
 * AKA shapes with Magnets
 */
public interface StickableShape {
    List<Magnet> getMagnets();
    void showMagnetsPoints();
    void hideMagnetPoints();
}
