package com.google.gwt.sample.foo.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;

public class Foo implements EntryPoint {

  @Override
  public void onModuleLoad() {
    Button b1 = new Button("FragmentA");
    b1.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent arg0) {
        GWT.runAsync(new RunAsyncCallback() {
          public void onSuccess() {
            new FragmentA().go();
          }

          @Override
          public void onFailure(Throwable arg0) {
          }
        });
      }
    });

    Button b2 = new Button("FragmentB");
    b2.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent arg0) {
        GWT.runAsync(new RunAsyncCallback() {
          public void onSuccess() {
            new FragmentB().go();
          }
          
          @Override
          public void onFailure(Throwable arg0) {
          }
        });
      }
    });
    
    RootPanel.get().add(b1);
    RootPanel.get().add(b2);
  }

}
