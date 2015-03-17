/**
 *
 * @author Diego Magagna
 */
package org.cytoscape.centiscape.internal;

import java.io.File;
import javax.swing.filechooser.FileFilter;

class Sif extends FileFilter {

  public boolean accept(File file) {
    if (file.isDirectory()) return true;
    String fname = file.getName().toLowerCase();
    return fname.endsWith("sif");
  }

  public String getDescription() {
    return "Files  .sif ";
  }
}
