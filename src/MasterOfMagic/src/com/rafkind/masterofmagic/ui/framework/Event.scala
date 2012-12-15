/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rafkind.masterofmagic.ui.framework

case class EventDescriptor(val name:String) {
  def spawn(component:Component[_], payload:Any) = 
    new Event(this, component, false, payload);
}

class Event(val descriptor:EventDescriptor, 
            val component:Component[_], 
            var consumed:Boolean,
            val payload:Any) {

  def consume() = 
    consumed = true;
}

case class PropertyEventPayload(  
  val whatChanged:Tuple2[ComponentProperty, Any])
    
case class MouseClickedEventPayload(
  val button:Int,
  val x:Int,
  val y:Int,
  val clickCount:Int)

case class MouseEventPayload(
  val button:Int,
  val x:Int,
  val y:Int)

case class KeyPressedEventPayload(
  val key:Int,
  val ch:Char)
    
object Event {  
  val PROPERTY_CHANGED = EventDescriptor("property_changed");
  val MOUSE_CLICKED = EventDescriptor("mouse_clicked");
  val MOUSE_PRESSED = EventDescriptor("mouse_pressed");
  val MOUSE_RELEASED = EventDescriptor("mouse_released");
  val KEY_PRESSED = EventDescriptor("key_pressed");
}
