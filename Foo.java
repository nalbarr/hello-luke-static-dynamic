public class Foo {
    public int x = 111;
    Foo() {
    }
    public void bar() {
        this.x = 222;
        System.out.println(String.format("Foo.bar(); x: %d", this.x));
    }    
}