/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rafkind.masterofmagic.ui.framework

import org.newdawn.slick._;

class Screen(backgroundImage:Image) extends Container {
  
  override def render(graphics:Graphics):Unit = {
    backgroundImage.draw(0, 0);

    super.render(graphics);
  }
}