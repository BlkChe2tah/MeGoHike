package com.example.megohike.presentation.new_hiking.view_model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.megohike.common.InputDateConverter;
import com.example.megohike.common.InputDateFormatter;
import com.example.megohike.data.data_source.database.HikeInformationDatabase;
import com.example.megohike.data.data_source.database.entities.HikeInfo;
import com.example.megohike.domain.use_case.AddNewHikeInfoUseCase;

public class NewHikingViewModel extends ViewModel {

    final private AddNewHikeInfoUseCase useCase;
    final private MutableLiveData<String> name = new MutableLiveData<>(null);
    final public LiveData<Boolean> isNameEmpty = Transformations.map(name, this::isBlankString);
    final private MutableLiveData<String> length = new MutableLiveData<>(null);
    final public LiveData<Boolean> isLengthEmpty = Transformations.map(length, this::isBlankString);
    final private MutableLiveData<String> latitude = new MutableLiveData<>(null);
    final private MutableLiveData<String> longitude = new MutableLiveData<>(null);
    final private MutableLiveData<String> startDate = new MutableLiveData<>(null);
    final public LiveData<Boolean> isStartDateFormatCorrect = Transformations.map(startDate, this::checkDateFormatIncorrect);
    final private MutableLiveData<String> expectedDate = new MutableLiveData<>(null);
    final public LiveData<Boolean> isExpectedDateFormatCorrect = Transformations.map(expectedDate, this::checkDateFormatIncorrect);
    final private MutableLiveData<Integer> hikingLevel = new MutableLiveData<>(0);
    final private MutableLiveData<Boolean> parkingAvailable = new MutableLiveData<>(false);
    final private MediatorLiveData<Boolean> btnEnableState = new MediatorLiveData<>(false);
    final public LiveData<Boolean> getBtnEnableState = btnEnableState;
    final private MutableLiveData<Boolean> uiState = new MutableLiveData<>(null);
    final public LiveData<Boolean> getUiState = uiState;
    public NewHikingViewModel(@NonNull AddNewHikeInfoUseCase useCase) {
        this.useCase = useCase;
        btnEnableState.addSource(isNameEmpty, state -> {btnEnableState.postValue(checkBtnState());});
        btnEnableState.addSource(isLengthEmpty, state -> {btnEnableState.postValue(checkBtnState());});
        btnEnableState.addSource(isStartDateFormatCorrect, state -> {btnEnableState.postValue(checkBtnState());});
        btnEnableState.addSource(isExpectedDateFormatCorrect, state -> {btnEnableState.postValue(checkBtnState());});
    }

    public void setName(String name) {
        this.name.postValue(name);
    }

    public void setLength(String length) {
        this.length.postValue(length);
    }

    public void setLatitude(String latitude) {
        this.latitude.postValue(latitude);
    }

    public void setLongitude(String longitude) {
        this.longitude.postValue(longitude);
    }

    public void setStartDate(String date) {
        this.startDate.postValue(date);
    }

    public void setExpectedDate(String date) {
        this.expectedDate.postValue(date);
    }

    public void setHikingLevel(Integer level) {
        this.hikingLevel.postValue(level);
    }

    public void setParkingAvailable(Boolean isAvailable) {
        this.parkingAvailable.postValue(isAvailable);
    }

    public void save() {
        HikeInformationDatabase.databaseWriteExecutor.execute(() ->{
        try {
            final long startTime = InputDateConverter.covert(startDate.getValue());
            final long expectedTime = InputDateConverter.covert(expectedDate.getValue());
            final int parkingAvailableValue = parkingAvailable.getValue() != null && parkingAvailable.getValue() ? 1 : 0;
            final HikeInfo data = new HikeInfo(
                    0,
                    name.getValue() == null ? "" : name.getValue(),
                    latitude.getValue(),
                    longitude.getValue(),
                    startTime,
                    expectedTime,
                    parkingAvailableValue,
                    length.getValue() == null ? 0 :  Double.parseDouble(length.getValue()),
                    hikingLevel.getValue() == null ? 0 : hikingLevel.getValue()
            );
            useCase.insertHikeInfo(data);
            uiState.postValue(true);
        } catch (NumberFormatException e) {
            uiState.postValue(false);
        }
        });
    }

    private Boolean checkBtnState() {
        final Boolean tIsNameEmpty = isNameEmpty.getValue();
        final Boolean tIsLengthEmpty = isLengthEmpty.getValue();
        final Boolean tIsDateIncorrect = isStartDateFormatCorrect.getValue();
        final Boolean tIsExpectedDateIncorrect = isExpectedDateFormatCorrect.getValue();
        if (tIsNameEmpty == null || tIsLengthEmpty == null || tIsDateIncorrect == null || tIsExpectedDateIncorrect == null) {
            return false;
        }
        return !tIsNameEmpty && !tIsLengthEmpty && !tIsDateIncorrect && !tIsExpectedDateIncorrect;
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
}
