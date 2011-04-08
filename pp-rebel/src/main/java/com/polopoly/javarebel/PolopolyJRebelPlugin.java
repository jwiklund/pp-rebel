/**
 * Copyright (C) 2010 ZeroTurnaround OU
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License v2 as published by
 * the Free Software Foundation, with the additional requirement that
 * ZeroTurnaround OU must be prominently attributed in the program.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You can find a copy of GNU General Public License v2 from
 *   http://www.gnu.org/licenses/gpl-2.0.txt
 */

package com.polopoly.javarebel;

import org.zeroturnaround.javarebel.ClassResourceSource;
import org.zeroturnaround.javarebel.Integration;
import org.zeroturnaround.javarebel.IntegrationFactory;
import org.zeroturnaround.javarebel.Plugin;

/**
 * A plugin that loads content files from disk (hopefully)
 */
public class PolopolyJRebelPlugin implements Plugin {

  /**
   * Set up the integration (register CBPs)
   */
  public void preinit() {

    // Register the CBP
    Integration i = IntegrationFactory.getInstance();
    ClassLoader cl = PolopolyJRebelPlugin.class.getClassLoader();
    i.addIntegrationProcessor(cl, "com.polopoly.cm.client.impl.service2client.ContentBase", new ContentBaseProcessor());
//    
//    // Set up the reload listener
//    ReloaderFactory.getInstance().addClassReloadListener(
//      new ClassEventListener() {
//        public void onClassEvent(int eventType, Class klass) {
//
//          try {
//            Class abstractCanvasClass = Class.forName("org.zeroturnaround.javarebel.sdkDemo.AbstractCanvas");
//          
//            // Check if it is child of AbstractCanvas
//            if (abstractCanvasClass.isAssignableFrom(klass)) {
//              System.out.println("An AbstractCanvas implementation class was reloaded .. re-painting the canvas");
//              DemoAppConfigReloader.repaint();
//              LoggerFactory.getInstance().echo("Repainted the canvas");
//            }
//            
//          } catch (Exception e) {
//            LoggerFactory.getInstance().error(e);
//            System.out.println(e);
//          }
//        }
//
//        public int priority() {
//          return 0;
//        }
//      }
//    );

  }

  public boolean checkDependencies(ClassLoader classLoader, ClassResourceSource classResourceSource) {
    return classResourceSource.getClassResource("com.polopoly.cm.client.impl.service2client.ContentBase") != null;
  }

  public String getId() {
    return "pp-rebel";
  }

  public String getName() {
    return "Polopoly JRebel Plugin";
  }

  public String getDescription() {
    return "Loads Polopoly content files directly from the file system";
  }

  public String getAuthor() {
    return null;
  }

  public String getWebsite() {
    return null;
  }
}
