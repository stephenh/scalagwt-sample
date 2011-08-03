package com.google.gwt.sample.foo.client;

// instantiated but not called from FragmentB
public class BarSub extends Bar {
  @Override
  public void go() {
    // refer to BazSub, only live with FragmentA+FragmentB
    new BazSub().go();
  }
}
