package de.dafriedmann.foo.impl;

import de.dafriedmann.foo.IBar;
import de.dafriedmann.foo.IFoo;

public class Bar implements IBar {

    private String shouldBeFilledByInstancio;

    @Override
    public void doBarStuff(){
        System.out.println("bar done");
    }

    @Override
    public IFoo getFoo() {
        return new Foo();
    }

}
