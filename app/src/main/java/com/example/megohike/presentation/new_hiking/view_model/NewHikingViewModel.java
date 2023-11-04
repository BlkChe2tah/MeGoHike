package com.example.megohike.presentation.new_hiking.view_model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.megohike.common.InputDateConverter;
import com.example.megohike.common.InputDateFormatter;
import com.example.megohike.data.data_source.database.HikeInformationDatabase;
import com.example.megohike.data.data_source.database.entities.HikeInfo;
import com.example.megohike.domain.use_case.AddNewHikeInfoUseCase;

public class NewHikingViewModel extends ViewModel {

    final private AddNewHikeInfoUseCase useCase;
    final private MutableLiveData<String> latitude = new MutableLiveData<>(null);
    final private MutableLiveData<String> longitude = new MutableLiveData<>(null);
    final private MutableLiveData<Boolean> parkingAvailable = new MutableLiveData<>(false);
    final private MutableLiveData<Integer> hikingLevel = new MutableLiveData<>(0);
    final private MutableLiveData<String> name = new MutableLiveData<>(null);
    final private MutableLiveData<Boolean> nameState = new MutableLiveData<>(null);
    final private MutableLiveData<String> length = new MutableLiveData<>(null);
    final private MutableLiveData<Boolean> lengthState = new MutableLiveData<>(null);
    final private MutableLiveData<String> startDate = new MutableLiveData<>(null);
    final private MutableLiveData<Boolean> startDateState = new MutableLiveData<>(null);
    final private MutableLiveData<String> expectedDate = new MutableLiveData<>(null);
    final private MutableLiveData<Boolean> expectedDateState = new MutableLiveData<>(null);
    final private MediatorLiveData<Boolean> isBtnEnableState = new MediatorLiveData<>(false);
    final private MediatorLiveData<Boolean> saveHikeInfoState = new MediatorLiveData<>(null);
    public NewHikingViewModel(@NonNull AddNewHikeInfoUseCase useCase) {
        this.useCase = useCase;
        isBtnEnableState.addSource(nameState, state -> {isBtnEnableState.postValue(checkBtnState());});
        isBtnEnableState.addSource(lengthState, state -> {isBtnEnableState.postValue(checkBtnState());});
        isBtnEnableState.addSource(startDateState, state -> {isBtnEnableState.postValue(checkBtnState());});
        isBtnEnableState.addSource(expectedDateState, state -> {isBtnEnableState.postValue(checkBtnState());});
    }

    public void setHikingLevel(Integer level) {
        this.hikingLevel.postValue(level);
    }

    public void setParkingAvailable(Boolean isAvailable) {
        this.parkingAvailable.postValue(isAvailable);
    }

    public void setLatitude(String latitude) {
        this.latitude.postValue(latitude);
    }

    public void setLongitude(String longitude) {
        this.longitude.postValue(longitude);
    }

    public LiveData<Boolean> getNameState() {
        return nameState;
    }

    public void setName(String name) {
        this.name.postValue(name);
        this.nameState.postValue(isBlankString(name));
    }

    public LiveData<Boolean> getLengthState() {
        return lengthState;
    }

    public void setLength(String length) {
        this.length.postValue(length);
        this.lengthState.postValue(isBlankString(length));
    }

    public LiveData<Boolean> getStartDateState() {
        return startDateState;
    }

    public void setStartDate(String date) {
        this.startDate.postValue(date);
        this.startDateState.postValue(!InputDateFormatter.isFormatCorrect(date));
    }

    public LiveData<Boolean> getExpectedDateState() {
        return expectedDateState;
    }

    public void setExpectedDate(String date) {
        this.expectedDate.postValue(date);
        this.expectedDateState.postValue(!InputDateFormatter.isFormatCorrect(date));
    }

    public LiveData<Boolean> getIsBtnEnableState() {
        return isBtnEnableState;
    }
    public LiveData<Boolean> getSaveHikeInfoState() {
        return saveHikeInfoState;
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
            saveHikeInfoState.postValue(true);
        } catch (NumberFormatException e) {
            saveHikeInfoState.postValue(false);
        }
        });
    }

    private Boolean checkBtnState() {
        if (getNameState().getValue() == null || getLengthState().getValue() == null || getStartDateState().getValue() == null || getExpectedDateState().getValue() == null) {
            return false;
        }
        return !getNameState().getValue() && !getLengthState().getValue() && !getStartDateState().getValue() && !getExpectedDateState().getValue();
    }

    private Boolean isBlankString(String data) {
        return data != null && data.isBlank();
    }
}
