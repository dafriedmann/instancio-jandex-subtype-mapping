package de.dafriedmann.foo.impl;

import de.dafriedmann.foo.IFoo;

public class Foo implements IFoo {

    private String shouldBeFilledByInstancio;

    public void doFooStuff(){
        System.out.println("foo done");
    }

}
