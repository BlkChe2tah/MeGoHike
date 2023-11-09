package com.example.megohike.presentation.new_observation.view_model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.megohike.common.InputDateConverter;
import com.example.megohike.common.InputDateFormatter;
import com.example.megohike.common.InputTimeFormatter;
import com.example.megohike.data.data_source.database.HikeInformationDatabase;
import com.example.megohike.data.data_source.database.entities.Observation;
import com.example.megohike.domain.use_case.AddNewObservationUseCase;

public class NewObservationViewModel extends ViewModel {

    final private AddNewObservationUseCase useCase;
    final private MutableLiveData<String> name = new MutableLiveData<>(null);
    final public LiveData<Boolean> isNameEmpty = Transformations.map(name, this::isBlankString);

    final private MutableLiveData<String> date = new MutableLiveData<>(null);
    final public LiveData<Boolean> isDateFormatCorrect = Transformations.map(date, this::checkDateFormatIncorrect);
    final private MutableLiveData<String> time = new MutableLiveData<>(null);
    final public LiveData<Boolean> isTimeFormatCorrect = Transformations.map(time, this::checkTimeFormatIncorrect);
    final private MutableLiveData<String> comment = new MutableLiveData<>(null);
    final private MediatorLiveData<Boolean> btnEnableState = new MediatorLiveData<>(false);
    final public LiveData<Boolean> getBtnEnableState = btnEnableState;
    final private MutableLiveData<Boolean> uiState = new MutableLiveData<>(null);
    final public LiveData<Boolean> getUiState = uiState;
    public NewObservationViewModel(@NonNull AddNewObservationUseCase useCase) {
        this.useCase = useCase;
        btnEnableState.addSource(isNameEmpty, state -> {btnEnableState.postValue(checkBtnState());});
        btnEnableState.addSource(isDateFormatCorrect, state -> {btnEnableState.postValue(checkBtnState());});
        btnEnableState.addSource(isTimeFormatCorrect, state -> {btnEnableState.postValue(checkBtnState());});
    }

    public void save(int hikeInfoId) {
        HikeInformationDatabase.databaseWriteExecutor.execute(() ->{
        try {
            final long convertTime = InputDateConverter.covertTime(String.format("%s %s", date.getValue(), time.getValue()));
            useCase.insertObservation(
                    new Observation(
                            0,
                            hikeInfoId,
                            name.getValue() == null ? "" : name.getValue(),
                            null,
                            convertTime,
                            comment.getValue()
                    )
            );
            uiState.postValue(true);
        } catch (NumberFormatException e) {
            uiState.postValue(false);
        }
        });
    }

    public void setName(String name) {
        this.name.postValue(name);
    }

    public void setDate(String date) {
        this.date.postValue(date);
    }

    public void setTime(String time) {
        this.time.postValue(time);
    }

    public void setComment(String period) {
        this.comment.postValue(period);
    }

    private Boolean checkBtnState() {
        final Boolean tIsNameEmpty = isNameEmpty.getValue();
        final Boolean tIsLengthEmpty = isDateFormatCorrect.getValue();
        final Boolean tIsDateIncorrect = isTimeFormatCorrect.getValue();
        if (tIsNameEmpty == null || tIsLengthEmpty == null || tIsDateIncorrect == null) {
            return false;
        }
        return !tIsNameEmpty && !tIsLengthEmpty && !tIsDateIncorrect;
    }

    private Boolean isBlankString(String data) {
        if (data == null || data.isEmpty()) return null;
        return data.isBlank();
    }

    private Boolean checkDateFormatIncorrect(String date) {
        final Boolean isCorrect = InputDateFormatter.isFormatCorrect(date);
        if (isCorrect == null || date.isEmpty()) return null;
        return !isCorrect;
    }

    private Boolean checkTimeFormatIncorrect(String time) {
        final Boolean isCorrect = InputTimeFormatter.isFormatCorrect(time);
        if (isCorrect == null || time.isEmpty()) return null;
        return !isCorrect;
    }
}
