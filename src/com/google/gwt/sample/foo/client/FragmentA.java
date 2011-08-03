package com.google.gwt.sample.foo.client;

public class FragmentA {
  public void go() {
    // instantiate Bar, make go live
    new Bar().go();
    // also claim Baz
    new Baz().go();
  }

}
