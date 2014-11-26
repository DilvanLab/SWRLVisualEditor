package br.usp.icmc.dilvan.swrlEditor.client.javafx;

public interface AsyncHandler<X> {

    void handleFailure(Throwable caught);

    void handleSuccess(X result);

}
