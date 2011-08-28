package com.google.gwt.sample.mnemonics.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

class Mnemonics extends EntryPoint {

  def onModuleLoad() {
    val dict = List("Scala", "rocks", "Pack", "brocks")
    val coder = new Coder(dict)
    val b = new Button("Click me", (_: ClickEvent) => Window.alert("Mnemonics for \"7225276257\" are: \n" + coder.translate("7225276257")))
    RootPanel.get().add(b);
  }

  implicit def clickHandler(f: ClickEvent => Unit): ClickHandler = new ClickHandler {
    def onClick(event: ClickEvent) = f(event)
  }
}
