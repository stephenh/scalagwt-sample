package com.google.gwt.sample.foo.client;

import com.google.gwt.user.client.*;

public class FragmentB {
  public void go() {
    // instantiate BarSub, but don't make BarSub.go() live
    new BarSub();
    Window.alert("fine");
  }
}
