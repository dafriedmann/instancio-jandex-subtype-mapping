import de.dafriedmann.foo.IBar;
import de.dafriedmann.foo.IFoo;
import de.dafriedmann.foo.impl.Bar;
import de.dafriedmann.foo.impl.Foo;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import static org.instancio.Select.all;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InstancioSubtypeTest {

    @Test
    void testInstancioSubtypingWithGeneratedProperties(){
        // If this test fails you forgot to generate the jandex index
        // see readme

        IFoo foo = Instancio.create(IFoo.class);
        IBar bar = Instancio.create(IBar.class);

        assertNotNull(foo);
        assertNotNull(bar);
        assertNotNull(bar.getFoo());
    }

    @Test
    void testInstancioSubtypingWithDedicatedImpl(){
        IFoo foo = Instancio.of(IFoo.class).subtype(all(IFoo.class), Foo.class).create();
        IBar bar = Instancio.of(IBar.class).subtype(all(IBar.class), Bar.class).create();

        assertNotNull(foo);
        assertNotNull(bar);
        assertNotNull(bar.getFoo());
    }

}
