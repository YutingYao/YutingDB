import java.util.concurrent.ArrayBlockingQueue;

public class TestClass {
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
	    System.out.println(test1());
	    System.out.println(test2());
	}
    
	public static String test1() {
	    String a = "";
	    try {
		a += "try";
		return a;
	    } finally {
		a += "finally";
	    }
	}
    
	public static StringBuilder test2() {
	    StringBuilder a = new StringBuilder();
	    try {
		b.append("try");
		return b;
	    } finally {
		b.append("finally");
	    }
	}
    }
    