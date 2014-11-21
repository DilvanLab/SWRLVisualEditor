package br.usp.icmc.dilvan.swrlEditor.client.javafx;

import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class AbstractAsyncHandler<X> implements AsyncHandler<X>, AsyncCallback<X> {

	@Override
	public void onSuccess(X result) {
		handleSuccess(result);
	}

	@Override
	public void onFailure(Throwable caught) {
		handleFailure(caught);
	}

	@Override
	public abstract void handleFailure(Throwable caught);

	@Override
	public abstract void handleSuccess(X result);
}
