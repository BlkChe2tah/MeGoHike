package com.example.megohike.presentation.new_equipment.view_model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.megohike.common.InputDateConverter;
import com.example.megohike.common.InputDateFormatter;
import com.example.megohike.common.InputTimeFormatter;
import com.example.megohike.data.data_source.database.HikeInformationDatabase;
import com.example.megohike.data.data_source.database.entities.Equipment;
import com.example.megohike.data.data_source.database.entities.Observation;
import com.example.megohike.domain.use_case.AddNewEquipmentUseCase;
import com.example.megohike.domain.use_case.AddNewObservationUseCase;

public class NewEquipmentViewModel extends ViewModel {

    final private AddNewEquipmentUseCase useCase;
    final private MutableLiveData<String> name = new MutableLiveData<>(null);
    final public LiveData<Boolean> isNameEmpty = Transformations.map(name, this::isBlankString);

    final private MutableLiveData<String> count = new MutableLiveData<>(null);
    final public LiveData<Boolean> isCountCorrect = Transformations.map(count, this::checkCountCorrect);
    final private MediatorLiveData<Boolean> btnEnableState = new MediatorLiveData<>(false);
    final public LiveData<Boolean> getBtnEnableState = btnEnableState;
    final private MutableLiveData<Boolean> uiState = new MutableLiveData<>(null);
    final public LiveData<Boolean> getUiState = uiState;
    public NewEquipmentViewModel(@NonNull AddNewEquipmentUseCase useCase) {
        this.useCase = useCase;
        btnEnableState.addSource(isNameEmpty, state -> {btnEnableState.postValue(checkBtnState());});
        btnEnableState.addSource(isCountCorrect, state -> {btnEnableState.postValue(checkBtnState());});
    }

    public void save(int hikeInfoId) {
        HikeInformationDatabase.databaseWriteExecutor.execute(() ->{
        try {
            useCase.insertEquipment(
                    new Equipment(
                            0,
                            hikeInfoId,
                            name.getValue() == null ? "" : name.getValue(),
                            count.getValue() == null ? 0 : Integer.parseInt(count.getValue())
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
    public void setCount(String count) {
        this.count.postValue(count);
    }

    private Boolean checkBtnState() {
        final Boolean tIsNameEmpty = isNameEmpty.getValue();
        final Boolean tIsCountCorrect = isCountCorrect.getValue();
        if (tIsNameEmpty == null || tIsCountCorrect == null) {
            return false;
        }
        return !tIsNameEmpty && !tIsCountCorrect;
    }

    private Boolean isBlankString(String data) {
        if (data == null || data.isEmpty()) return null;
        return data.isBlank();
    }

    private Boolean checkCountCorrect(String data) {
        if (data == null || data.isEmpty()) return null;
        return !(Integer.parseInt(data) >= 1);
    }

}
