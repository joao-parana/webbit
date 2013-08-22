package mngmt;

public class NotImplementedException extends RuntimeException {
	NotImplementedException() {
		super("Feature Not Implemented yet");
	}

	NotImplementedException(String msg) {
		super("Feature Not Implemented yet - " + msg);
	}
}
