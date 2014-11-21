package br.usp.icmc.dilvan.swrlEditor.client.javafx;

public interface AsyncHandler<X> {
	
	void handleSuccess(X result);
	
	void handleFailure(Throwable caught);
	
}
