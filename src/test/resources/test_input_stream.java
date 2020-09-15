public class HelloStream extends Test {
	public static final java.lang.String text1 = "test";
	
	public static HelloStream instance;
	public java.lang.String thisText;
	
	public HelloStream(java.lang.String text) {
		instance = this;
		this.thisText = text;
	}
	
	public static void main(String[] args) {
		System.out.println("hello");
		java.lang.String text = "goodbye";
		System.out.println(text);
		System.out.println(text1);
		System.out.println(instance.thisText);
	}
}