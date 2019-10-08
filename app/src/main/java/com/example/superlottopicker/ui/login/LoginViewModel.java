package com.example.superlottopicker.ui.login;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Patterns;

import com.example.superlottopicker.data.LoginRepository;
import com.example.superlottopicker.data.Result;
import com.example.superlottopicker.data.model.LoggedInUser;
import com.example.superlottopicker.R;

public class LoginViewModel extends ViewModel {

	private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
	private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
	private LoginRepository loginRepository;
    final String mynotes = "luistam1959";
	LoginViewModel(LoginRepository loginRepository) {
		this.loginRepository = loginRepository;
	}

	LiveData<LoginFormState> getLoginFormState() {
		return loginFormState;
	}

	LiveData<LoginResult> getLoginResult() {
		return loginResult;
	}

	public void login(String username, String password) {
		// can be launched in a separate asynchronous job
		Result<LoggedInUser> result = loginRepository.login(username, password);

		if (result instanceof Result.Success) {
			LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
			loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
		} else {
			loginResult.setValue(new LoginResult(R.string.login_failed));
		}
	}

	public void loginDataChanged(String username, String password) {
		if (!isUserNameValid(username)) {
			loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));

		} else if (!isPasswordValid(password)) {
			loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
		} else {
			loginFormState.setValue(new LoginFormState(true));
		}
	}

	// A placeholder username validation check
	private boolean isUserNameValid(String username) {
		if (username == null) {
			return false;
		}
		if (username.contains("@")) {
			return Patterns.EMAIL_ADDRESS.matcher(username).matches();
		} else {
			return !username.trim().isEmpty();
		}
	}

	// A placeholder password validation check
	private boolean isPasswordValid(String password) {


		if(password != null && password.trim().length() > 5 ){
			if(password.equals(mynotes)){
				return true;
			}
		}
		return false;

		//return  password.trim().length() > 5 && password.equals(mynotes) ;

	}
}
